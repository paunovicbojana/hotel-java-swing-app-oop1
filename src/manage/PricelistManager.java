package manage;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import entity.Pricelist;
import entity.Service;
import entity.AService;
import enums.ServiceType;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.time.LocalDate;

public class PricelistManager {
	private HashMap<Integer, Pricelist> pricelistList;
	private ServiceManager serviceManager;
	private String pathPricelistFile;
	
	public PricelistManager(String pathPricelistFile, ServiceManager serviceManager) {
		pricelistList = new HashMap<Integer, Pricelist>();
		this.serviceManager = serviceManager;
		this.pathPricelistFile = pathPricelistFile;
		
    }
	
	public HashMap<Integer, Pricelist> getPricelistList() {
		return pricelistList;
	}
	
	public void setPricelistList(HashMap<Integer, Pricelist> pricelistList) {
        this.pricelistList = pricelistList;
	}
	
	public int generatePricelistID() {
		int ID = pricelistList.size();
		if (ID == 0) {
			return ID;
		}
		return ID;
	}
	
	
	public int generatePricelist(String name, LocalDate dateFrom, LocalDate dateTo, int priority) {
        Pricelist pricelist = new Pricelist(generatePricelistID(), name, dateFrom, dateTo, false, priority);
        pricelistList.put(pricelist.getId(), pricelist);
        return pricelist.getId();
    }
	
	public void addToPricelist(int ID, String name, ServiceType type, double price) {
		Pricelist pricelist = pricelistList.get(ID);
		AService service = new AService(pricelist.getServices().size(), name, type, price, false, ID);
		pricelist.getServices().put(service.getId(), service);
	}
	
	public void removeFromPricelist(int ID, int serviceID) {
		Pricelist pricelist = pricelistList.get(ID);
		pricelist.getServices().remove(serviceID);
	}
	
	public boolean saveData() {
	    if (this.pathPricelistFile == null) {
	        System.err.println("File path is null.");
	        return false;
	    }

	    try (PrintWriter pw = new PrintWriter(new FileWriter(this.pathPricelistFile, false))) {
	        for (Pricelist pricelist : pricelistList.values()) {
	            pw.println(pricelist.toFileString());
	        }
	        return true;
	    } catch (IOException e) {
	        System.err.println("Failed to save data: " + e.getMessage());
	        return false;
	    }
	}

	
	public boolean loadData() {
		try {
			BufferedReader br = new BufferedReader(new FileReader(this.pathPricelistFile));
			String linija = null;
			while ((linija = br.readLine()) != null) {
				String [] tokens = linija.split(",");
				Pricelist pricelist = new Pricelist(Integer.parseInt(tokens[0]), tokens[1],LocalDate.parse(tokens[2]), LocalDate.parse(tokens[3]), Boolean.parseBoolean(tokens[4]), Integer.parseInt(tokens[5]));
				pricelistList.put(Integer.parseInt(tokens[0]), pricelist);
		}
			br.close();
			addServiceToPricelist();
		} catch (IOException e) {
			return false;
		}
		return true;
			
	}
	
	public void addServiceToPricelist() {
		for (Pricelist pricelist : pricelistList.values()) {
            for (Service service : serviceManager.getServiceList().values()) {
                if (service.getPricelistID() == pricelist.getId()) {
                	int size = pricelist.getServices().size();
                    pricelist.getServices().put(size, service);
                }
            }
        }
		
	}
	
	public List<LocalDate> getDatesBetween(LocalDate startDate, LocalDate endDate) {
	    List<LocalDate> dates = new ArrayList<>();
	    LocalDate currentDate = startDate;

	    while (!currentDate.isEqual(endDate)) {
	        dates.add(currentDate);
	        currentDate = currentDate.plusDays(1);
	    }

	    return dates;
	}
}

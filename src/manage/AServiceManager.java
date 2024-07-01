package manage;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.HashMap;

import enums.ServiceType;
import entity.AService;
import entity.Service;

public class AServiceManager {
	
	private HashMap<Integer, AService> serviceTypeList;
	private String pathServiceFile;
	private HashMap<Integer, Service> serviceList;

	public AServiceManager(String pathServiceFile, HashMap<Integer, Service> serviceList) {
		this.pathServiceFile = pathServiceFile;
		this.serviceTypeList = new HashMap<Integer, AService>();
		this.serviceList = serviceList;
	}

	public HashMap<Integer, AService> getServiceList() {
		return serviceTypeList;
	}
	
	public void setServiceList(HashMap<Integer, AService> serviceTypeList) {
		this.serviceTypeList = serviceTypeList;
	}
	
	public int generateServiceID() {
		int ID = serviceTypeList.size();
		if (ID == 0) {
			return ID;
		}
		return ID;
	}
	
	public void deleteService(int ID) {
		serviceTypeList.get(ID).setDeleted(true);
	}
	
	public AService getService(int ID) {
		return serviceTypeList.get(ID);
	}
	
	public int addServiceType(String serviceName, ServiceType serviceType, double price, boolean isDeleted, int pricelistID) {
		AService service = new AService(generateServiceID(), serviceName, serviceType, price, false, pricelistID);
		serviceTypeList.put(service.getId(), service);
		serviceList.put(service.getId(), service);
		return service.getId();
	}
	
	public void printServices() {
		for (AService service : serviceTypeList.values()) {
			System.out.println(service.getServiceName());
		}
	}
	
	public boolean saveData() {
	    if (this.pathServiceFile == null) {
	        System.err.println("File path is null.");
	        return false;
	    }

	    try (PrintWriter pw = new PrintWriter(new FileWriter(this.pathServiceFile, false))) {
	        for (AService service : serviceTypeList.values()) {
	            pw.println(service.toFileString());
	        }
	        return true;
	    } catch (IOException e) {
	        System.err.println("Failed to save data: " + e.getMessage());
	        return false;
	    }
	}

	
	public boolean loadData() {
		try {
			BufferedReader br = new BufferedReader(new FileReader(this.pathServiceFile));
			String linija = null;
			while ((linija = br.readLine()) != null) {
				String [] tokens = linija.split(",");
				AService service = new AService(Integer.parseInt(tokens[0]), tokens[1], ServiceType.valueOf(tokens[2]), Double.parseDouble(tokens[3]), Boolean.parseBoolean(tokens[4]), Integer.parseInt(tokens[5]));
				this.serviceTypeList.put(Integer.parseInt(tokens[0]), service);
				int size = serviceList.size() ;
				this.serviceList.put(size, service);
			}
			
			br.close();
		} catch (IOException e) {
			return false;
		}
		return true;
			
	}
}

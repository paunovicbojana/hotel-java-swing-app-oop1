package manage;
import entity.*;
import enums.Gender;
import enums.LevelOfEducation;
import enums.Role;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.time.LocalDate;
import java.util.HashMap;

public class AdministratorManager {
	private HashMap<Integer, Administrator> administratorList;
	private StaffManager staffManager;
	private String pathAdminFile;
	
	public AdministratorManager(String pathAdminFile) {
		administratorList = new HashMap<Integer, Administrator>();
		this.pathAdminFile = pathAdminFile;
	}
	
	public HashMap<Integer, Administrator> getAdministratorList() {
		return administratorList;
	}
	
	public void setAdministratorList(HashMap<Integer, Administrator> administratorList) {
		this.administratorList = administratorList;
	}
	
	public StaffManager getStaffManager() {
		return staffManager;
	}
	
	public void setStaffManager(StaffManager staffManager) {
		this.staffManager = staffManager;
	}
	
	public void setPathManagerFile(String pathManagerFile) {
		this.pathAdminFile = pathManagerFile;
	}
	
	public String getPathManagerFile() {
		return pathAdminFile;
	}
	
	public int generateAdminID() {
		int ID = administratorList.size();
		if (ID == 0) {
			return ID;
		}
		return ID;
	}
	
	public void deleteAdministrator(int ID) {
		getAdministratorByID(ID).setDeleted(true);
	}

	public Administrator getAdministratorByID(int ID) {
		return administratorList.get(ID);
	}
	
	public int addAdministrator(String firstName, String lastName, Gender gender, LocalDate dateOfBirth, String phone, String address, String username, String password, LevelOfEducation levelOfEducation, int yearsOfService, double salary, Role role, boolean isDeleted) {
		Administrator admin = new Administrator(generateAdminID(), firstName, lastName, gender, dateOfBirth, phone, address, username, password, levelOfEducation, yearsOfService, salary, role, false);
		administratorList.put(admin.getId(), admin);
		return admin.getId();
	}
	
	public void printAdministrators() {
	    System.out.println("Administrators:");
	    for (Administrator admin : administratorList.values()) {
	        System.out.println(admin.toString());
	    }
	}
	
	public boolean saveData() {
	    if (this.pathAdminFile == null) {
	        System.err.println("File path is null.");
	        return false;
	    }

	    try (PrintWriter pw = new PrintWriter(new FileWriter(this.pathAdminFile, false))) {
	        for (Administrator admin : administratorList.values()) {
	            pw.println(admin.toFileString());
	        }
	        return true;
	    } catch (IOException e) {
	        System.err.println("Failed to save data: " + e.getMessage());
	        return false;
	    }
	}

	
	public boolean loadData() {
		try {
			BufferedReader br = new BufferedReader(new FileReader(this.pathAdminFile));
			String linija = null;
			while ((linija = br.readLine()) != null) {
				String [] tokens = linija.split(",");
			
				Administrator admin = new Administrator(Integer.parseInt(tokens[0]), tokens[1], tokens[2], Gender.valueOf(tokens[3]), LocalDate.parse(tokens[4]), tokens[5], tokens[6], tokens[7], tokens[8], LevelOfEducation.valueOf(tokens[9]), Integer.parseInt(tokens[10]), Double.parseDouble(tokens[11]), Role.ADMINISTRATOR, Boolean.parseBoolean(tokens[13]));
				administratorList.put(Integer.parseInt(tokens[0]), admin);
		}
			br.close();
		} catch (IOException e) {
			return false;
		}
		return true;
			
	}
}

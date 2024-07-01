package manage;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.time.LocalDate;
import java.util.HashMap;

import entity.Receptionist;
import enums.Gender;
import enums.LevelOfEducation;
import enums.Role;

public class ReceptionistManager {
	private HashMap<Integer,Receptionist>receptionistList;
	private StaffManager staffManager;
	private GuestManager guestManager;
	private String pathReceptionistFile;
	private String pathGuestFile;
	
	public ReceptionistManager(String pathReceptionistFile, String pathGuestFile) {
		receptionistList = new HashMap<Integer, Receptionist>();
		this.pathGuestFile = pathGuestFile;
		guestManager = new GuestManager(pathGuestFile);
		this.pathReceptionistFile = pathReceptionistFile;
	}
	
	public HashMap<Integer, Receptionist> getReceptionistList() {
		return receptionistList;
	}
	
	public void setReceptionistList(HashMap<Integer, Receptionist> receptionistList) {
		this.receptionistList = receptionistList;
	}
	
	public int generateReceptionistID() {
		int ID = receptionistList.size();
		if (ID == 0) {
			return ID;
		}
		return ID;
	}
	
	public void deleteReceptionist(int ID) {
		getReceptionistByID(ID).setDeleted(true);
	}

	public Receptionist getReceptionistByID(int ID) {
		return receptionistList.get(ID);
	}
	
	public int addReceptionist(String firstName, String lastName, Gender gender, LocalDate dateOfBirth, String phone, String address, String username, String password, LevelOfEducation levelOfEducation, int yearsOfService, double salary, Role role, boolean isDeleted) {
		Receptionist receptionist = new Receptionist(generateReceptionistID(), firstName, lastName, gender, dateOfBirth, phone, address, username, password, levelOfEducation, yearsOfService, salary, role, false);
		receptionistList.put(receptionist.getId(), receptionist);
		return receptionist.getId();
	}
	
	public StaffManager getStaffManager() {
		return staffManager;
	}
	
	public void setStaffManager(StaffManager staffManager) {
		this.staffManager = staffManager;
	}
	
	public void printReceptionists() {
		System.out.println("Receptionists:");
		for (Receptionist receptionist : receptionistList.values()) {
			System.out.println(receptionist.toString());
		}
	}
	
	public GuestManager getGuestManager() {
		return guestManager;
	}
	
	public void setGuestManager(GuestManager guestManager) {
		this.guestManager = guestManager;
	}
	
	public boolean saveData() {
	    if (this.pathReceptionistFile == null) {
	        System.err.println("File path is null.");
	        return false;
	    }

	    try (PrintWriter pw = new PrintWriter(new FileWriter(this.pathReceptionistFile, false))) {
	        for (Receptionist receptionist : receptionistList.values()) {
	            pw.println(receptionist.toFileString());
	        }
	        return true;
	    } catch (IOException e) {
	        System.err.println("Failed to save data: " + e.getMessage());
	        return false;
	    }
	}

	
	public boolean loadData() {
		try {
			BufferedReader br = new BufferedReader(new FileReader(this.pathReceptionistFile));
			String linija = null;
			while ((linija = br.readLine()) != null) {
				String [] tokens = linija.split(",");
				this.receptionistList.put(Integer.parseInt(tokens[0]), new Receptionist(Integer.parseInt(tokens[0]), tokens[1], tokens[2], Gender.valueOf(tokens[3]), LocalDate.parse(tokens[4]), tokens[5], tokens[6], tokens[7], tokens[8], LevelOfEducation.valueOf(tokens[9]), Integer.parseInt(tokens[10]), Double.parseDouble(tokens[11]), Role.RECEPTIONIST, Boolean.parseBoolean(tokens[13])));
			}
			br.close();
		} catch (IOException e) {
			return false;
		}
		return true;
			
	}
	
	
}

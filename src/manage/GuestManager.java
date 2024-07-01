package manage;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.time.LocalDate;
import java.util.HashMap;

import entity.Administrator;
import entity.Guest;
import enums.Gender;
import enums.LevelOfEducation;
import enums.Role;

public class GuestManager {

	private HashMap<Integer, Guest> guestList;
	private String pathGuestFile;
	
	public GuestManager(String pathGuestFile) {
		guestList = new HashMap<Integer, Guest>();
		this.pathGuestFile = pathGuestFile;
	}
	
	public HashMap<Integer, Guest> getGuestList() {
		return guestList;
	}
	
	public void setGuestList(HashMap<Integer, Guest> guestList) {
		this.guestList = guestList;
	}
	
	public int generateGuestID() {
		int ID = guestList.size();
		if (ID == 0) {
			return ID;
		}
		return ID;
	}
	
	public void deleteGuest(int ID) {
		getGuestByID(ID).setDeleted(true);
	}

	public Guest getGuestByID(int ID) {
		return guestList.get(ID);
	}
	
	public int addGuest(String firstName, String lastName, Gender gender, LocalDate dateOfBirth, String phone, String address, String username, String password, boolean isDeleted, double expenses) {
		Guest guest = new Guest(generateGuestID(), firstName, lastName, gender, dateOfBirth, phone, address, username, password, false, expenses);
		this.guestList.put(guest.getId(), guest);
		return guest.getId();
	}
	
	public void printGuests() {
		System.out.println("Guests:");
		for (Guest guest : guestList.values()) {
			System.out.println(guest.toString());
		}
	}
	
	public boolean saveData() {
	    if (this.pathGuestFile == null) {
	        System.err.println("File path is null.");
	        return false;
	    }

	    try (PrintWriter pw = new PrintWriter(new FileWriter(this.pathGuestFile, false))) {
	        for (Guest guest : guestList.values()) {
	            pw.println(guest.toFileString());
	        }
	        return true;
	    } catch (IOException e) {
	        System.err.println("Failed to save data: " + e.getMessage());
	        return false;
	    }
	}

	
	
	
	public boolean loadData() {
		try {
			BufferedReader br = new BufferedReader(new FileReader(this.pathGuestFile));
			String linija = null;
			while ((linija = br.readLine()) != null) {
				String [] tokens = linija.split(",");
			
				Guest guest = new Guest(Integer.parseInt(tokens[0]), tokens[1], tokens[2], Gender.valueOf(tokens[3]), LocalDate.parse(tokens[4]), tokens[5], tokens[6], tokens[7], tokens[8], Boolean.parseBoolean(tokens[9]), Double.parseDouble(tokens[10]));
				guestList.put(Integer.parseInt(tokens[0]), guest);
		}
			br.close();
		} catch (IOException e) {
			return false;
		}
		return true;
			
	}


}

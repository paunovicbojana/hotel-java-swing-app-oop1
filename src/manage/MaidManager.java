package manage;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import entity.Maid;
import entity.Room;
import enums.Gender;
import enums.LevelOfEducation;
import enums.Role;

public class MaidManager {
	
	private HashMap<Integer, Maid> maidList;
	
	private StaffManager staffManager;
	private String pathMaidFile;
	private HashMap<Maid, Integer> maidRoomCount;
	
	public MaidManager(String pathMaidFile) {
		maidList = new HashMap<Integer, Maid>();
		this.pathMaidFile = pathMaidFile;
		this.maidRoomCount = new HashMap<Maid, Integer>();
	}

	public HashMap<Integer, Maid> getMaidList() {
		return maidList;
	}
	
	public void setMaidList(HashMap<Integer, Maid> maidList) {
		this.maidList = maidList;
	}
	
	public String getPathMaidFile() {
		return pathMaidFile;
	}
	
	public void setPathMaidFile(String pathMaidFile) {
		this.pathMaidFile = pathMaidFile;
	}
	
	public HashMap<Maid, Integer> getMaidRoomCount() {
		return maidRoomCount;
	}
	
	public void setMaidRoomCount(HashMap<Maid, Integer> maidRoomCount) {
		this.maidRoomCount = maidRoomCount;
	}
	
	public int generateMaidID() {
		int ID = maidList.size();
		if (ID == 0) {
			return ID;
		}
		return ID;
	}
	
	public void deleteMaid(int ID) {
		getMaidByID(ID).setDeleted(true);
	}

	public Maid getMaidByID(int ID) {
		return maidList.get(ID);
	}
	
	public int addMaid(String firstName, String lastName, Gender gender, LocalDate dateOfBirth, String phone, String address, String username, String password, LevelOfEducation levelOfEducation, int yearsOfService, double salary, Role role, boolean isDeleted, LocalDate date, int count) {
		ArrayList<Room> roomListMaid = new ArrayList<Room>();
		Maid maid = new Maid(generateMaidID(), firstName, lastName, gender, dateOfBirth, phone, address, username, password, levelOfEducation, yearsOfService, salary, role, roomListMaid ,false, date, count);
		maidList.put(maid.getId(), maid);
		return maid.getId();
	}
	
	public StaffManager getStaffManager() {
		return staffManager;
	}
	
	public void setStaffManager(StaffManager staffManager) {
		this.staffManager = staffManager;
	}
	
	public void printMaids() {
		System.out.println("\nMaids:");
		for (Maid maid : maidList.values()) {
			System.out.println(maid.toString());
		}
	}
	
	public boolean saveData() {
	    if (this.pathMaidFile == null) {
	        System.err.println("File path is null.");
	        return false;
	    }

	    try (PrintWriter pw = new PrintWriter(new FileWriter(this.pathMaidFile, false))) {
	        for (Maid maid : maidList.values()) {
	            pw.println(maid.toFileString());
	        }
	        return true;
	    } catch (IOException e) {
	        System.err.println("Failed to save data: " + e.getMessage());
	        return false;
	    }
	}

	
	public boolean loadData(RoomManager roomManager) {
		try {
			maidRoomCount.clear();
			maidList.clear();
			BufferedReader br = new BufferedReader(new FileReader(this.pathMaidFile));
			String linija = null;
			while ((linija = br.readLine()) != null) {
				String [] tokens = linija.split(",");
				ArrayList<Room> roomListMaid = new ArrayList<Room>();
				if (!tokens[13].equals("")) {
				for (String room : tokens[13].split(";")) {
					roomListMaid.add(roomManager.getRoom(Integer.parseInt(room)));
				}}
			else {
				roomListMaid = new ArrayList<Room>();
			}
				this.maidList.put(Integer.parseInt(tokens[0]), new Maid(Integer.parseInt(tokens[0]), tokens[1], tokens[2], Gender.valueOf(tokens[3]), LocalDate.parse(tokens[4]), tokens[5], tokens[6], tokens[7], tokens[8], LevelOfEducation.valueOf(tokens[9]), Integer.parseInt(tokens[10]), Double.parseDouble(tokens[11]), Role.MAID, roomListMaid ,Boolean.parseBoolean(tokens[14]), LocalDate.parse(tokens[15]), Integer.parseInt(tokens[16])));
				
			}
			
			br.close();
		} catch (IOException e) {
			return false;
		}
		return true;
			
	}
	
	public Maid assignMaidToRoom(Room room) {
		int min = Integer.MAX_VALUE;
		for (Maid maid1 : maidList.values()) {
			maidRoomCount.put(maid1, maid1.getRoomListMaid().size());
			if (maid1.getRoomListMaid().size() < min) {
				min = maid1.getRoomListMaid().size();
			}
		}
		
		for (Maid maid1 : maidRoomCount.keySet()) {
			if (maidRoomCount.get(maid1) == min) {
				maid1.getRoomListMaid().add(room);
				return maid1;
			}
		}
		
		return null;
	}
}

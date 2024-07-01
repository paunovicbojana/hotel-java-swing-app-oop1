package manage;
import entity.Room;
import entity.RoomAdditionalService;
import entity.RoomType;
import entity.Service;
import enums.RoomStatus;
import enums.ServiceType;
import manage.RoomManager;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.HashMap;

public class RoomManager {
	
	private HashMap<Integer, Room> roomList;
	private HashMap<Integer, RoomType> roomTypeList;
	private HashMap<RoomType, Integer> roomTypeCount;
	private String pathRoomFile;
	private String pathRoomTypeFile;
	private HashMap<Integer, Service> serviceList;
	private RoomAdditionalServiceManager roomAdditionalServiceManager;
	
	public RoomManager(String pathRoomFile, String pathRoomTypeFile, HashMap<Integer, Service> serviceList, RoomAdditionalServiceManager roomAdditionalServiceManager) {
		roomList = new HashMap<Integer, Room>();
		roomTypeList = new HashMap<Integer, RoomType>();
		roomTypeCount = new HashMap<RoomType, Integer>();
		this.pathRoomFile = pathRoomFile;
		this.pathRoomTypeFile = pathRoomTypeFile;
		this.serviceList = serviceList;
		this.roomAdditionalServiceManager = roomAdditionalServiceManager;
	}
	
	public HashMap<Integer, Room> getRoomList(){
		return roomList;
	}

	public void setRoomList(HashMap<Integer, Room> roomList) {
		this.roomList = roomList;
	}
	
	public int generateRoomID() {
		int ID = roomList.size();
		if (ID == 0) {
			return (ID + 1);
		}
		return (ID + 1);
	}
	
	public int generateRoomTypeID() {
		int ID = roomTypeList.size();
		if (ID == 0) {
			return ID;
		}
		return ID;
	}

	public Room getRoom(int ID) {
		return roomList.get(ID);
	}
	
	public Room getRoomID(int ID) {
		return roomList.get(ID);
	}
	
	public int addRoom(RoomType roomType, RoomStatus roomStatus, ArrayList<RoomAdditionalService> additionalServices, boolean isDeleted) {
		Room room = new Room(generateRoomID(), roomType, roomStatus, additionalServices, isDeleted);
		roomList.put(room.getRoomNumber(), room);
		if (!roomTypeCount.containsKey(roomType)) {
			roomTypeCount.put(roomType, 1);
			return room.getRoomNumber();
		}
		roomTypeCount.put(roomType, roomTypeCount.get(roomType) + 1);
		return room.getRoomNumber();
	}
	
	
	public int addRoom(int id, RoomType roomType, RoomStatus roomStatus, ArrayList<RoomAdditionalService> additionalServices, boolean isDeleted) {
		Room room = new Room(id, roomType, roomStatus, additionalServices, isDeleted);
		roomList.put(room.getRoomNumber(), room);
		if (!roomTypeCount.containsKey(roomType)) {
			roomTypeCount.put(roomType, 1);
			return room.getRoomNumber();
		}
		roomTypeCount.put(roomType, roomTypeCount.get(roomType) + 1);
		return room.getRoomNumber();
	}
	
	public void printRooms() {
		for (Room room : roomList.values()) {
			System.out.println(room.toString());
		}
	}
	
	public RoomType getRoomType(int ID) {
		return roomTypeList.get(ID);
	}


	public RoomStatus getRoomStatus(int ID) {
		Room room = roomList.get(ID);
		RoomStatus roomStatus = room.getRoomStatus();
		return roomStatus;
	}


	public void setRoomStatus(int ID, RoomStatus roomStatus) {
		Room room = roomList.get(ID);
		room.roomStatus = roomStatus;
	}


	public void setRoomType(int ID, RoomType roomType) {
		Room room = roomList.get(ID);
		room.roomType = roomType;
	}
	
	public void deleteRoomType(int ID) {
		roomTypeList.get(ID).setDeleted(true);
	}
	
	
	public int addRoomType(String typeName, int beds, int capacity, double pricePerNight, boolean isDeleted, int pricelistID, ServiceType serviceType) {
		RoomType roomType = new RoomType(generateRoomTypeID(), typeName, beds, capacity, pricePerNight, false, pricelistID, serviceType);
        roomTypeList.put(roomType.getId(), roomType);
        serviceList.put(roomType.getId(), roomType);
        return roomType.getId();
    }
	
	
	public boolean saveData() {
	    if (this.pathRoomFile == null) {
	        System.err.println("File path is null.");
	        return false;
	    }

	    try (PrintWriter pw = new PrintWriter(new FileWriter(this.pathRoomFile, false))) {
	        for (Room room : roomList.values()) {
	            pw.println(room.toFileString());
	        }
	        return true;
	    } catch (IOException e) {
	        System.err.println("Failed to save data: " + e.getMessage());
	        return false;
	    }
	}

	
	public boolean loadData() {
		try {
			roomTypeCount.clear();
			
			BufferedReader br = new BufferedReader(new FileReader(this.pathRoomFile));
			String linija = null;
			while ((linija = br.readLine()) != null) {
				String [] tokens = linija.split(",");
				
                int rtName = Integer.parseInt(tokens[1]);
                int roomID = Integer.parseInt(tokens[0]);
                RoomType roomType = roomTypeList.get(rtName);
                ArrayList<RoomAdditionalService> additionalServices = new ArrayList<RoomAdditionalService>();
                if (!tokens[3].equals("null")) {
					String[] additionalServicesTokens = tokens[3].split(";");
					for (String additionalService : additionalServicesTokens) {
						additionalServices.add(roomAdditionalServiceManager.getRServiceList()
								.get(Integer.parseInt(additionalService)));
					}
                }
				else {
					additionalServices = null;
				}
                
				addRoom(roomID, roomType, RoomStatus.valueOf(tokens[2]), additionalServices, Boolean.parseBoolean(tokens[4]));
			}
			br.close();
			
		} catch (IOException e) {
			return false;
		}
		return true;
			
	}
	
	public boolean saveRTData() {
	    if (this.pathRoomTypeFile == null) {
	        System.err.println("File path is null.");
	        return false;
	    }

	    try (PrintWriter pw = new PrintWriter(new FileWriter(this.pathRoomTypeFile, false))) {
	        for (RoomType room : roomTypeList.values()) {
	            pw.println(room.toFileString());
	        }
	        return true;
	    } catch (IOException e) {
	        System.err.println("Failed to save data: " + e.getMessage());
	        return false;
	    }
	}

	
	public boolean loadRTData() {
		try {
			BufferedReader br = new BufferedReader(new FileReader(this.pathRoomTypeFile));
			String linija = null;
			while ((linija = br.readLine()) != null) {
				String [] tokens = linija.split(",");
				
				RoomType room = new RoomType(Integer.parseInt(tokens[0]), tokens[1], Integer.parseInt(tokens[2]), Integer.parseInt(tokens[3]), Double.parseDouble(tokens[4]), Boolean.parseBoolean(tokens[5]), Integer.parseInt(tokens[6]), ServiceType.valueOf(tokens[7]));
				roomTypeList.put(Integer.parseInt(tokens[0]), room);
				int size = serviceList.size();
				serviceList.put(size, room);
				
		}
			
			br.close();
		} catch (IOException e) {
			return false;
		}
		return true;
			
	}
	
	public HashMap<Integer, RoomType> getRoomTypeList() {
		return roomTypeList;
	}
	
	public void setRoomTypeList(HashMap<Integer, RoomType> roomTypeList) {
		this.roomTypeList = roomTypeList;
	}
	
	
	public HashMap<RoomType, Integer> getRoomTypeCount() {
		return roomTypeCount;
	}
	
	public void setRoomTypeCount(HashMap<RoomType, Integer> roomTypeCount) {
		this.roomTypeCount = roomTypeCount;
	}
	
	
}

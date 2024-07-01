package entity;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;

import enums.RoomStatus;
import enums.ServiceType;
import manage.RoomAdditionalServiceManager;
public class Room {
	
	protected int roomNumber; 
    public RoomType roomType;
    public RoomStatus roomStatus;
    public ServiceType serviceType;
    protected HashMap<Integer, ArrayList<LocalDate>> reservations;
    protected Set<LocalDate> occupiedDates;
    protected ArrayList<RoomAdditionalService> additionalServices;
    protected boolean isDeleted;
	
	public Room(int roomNumber, RoomType roomType, RoomStatus roomStatus, ArrayList<RoomAdditionalService> additionalServices, boolean isDeleted) {
		this.roomNumber = roomNumber;
		this.roomType = roomType;
		this.roomStatus = roomStatus;
		this.serviceType = ServiceType.ROOM_SERVICE;
		this.reservations = new HashMap<Integer, ArrayList<LocalDate>>();
		this.occupiedDates = new HashSet<LocalDate>();
		this.additionalServices = additionalServices;
		this.isDeleted = isDeleted;
	}
	
	
	public String toString() {
		
        return roomNumber + "";
    }
	
	public int getRoomNumber() {
		return roomNumber;
	}
	
	public void setRoomNumber(int roomNumber) {
		this.roomNumber = roomNumber;
	}
	
	public RoomType getRoomType() {
		return roomType;
	}


	public RoomStatus getRoomStatus() {
		return roomStatus;
	}


	public void setRoomStatus(RoomStatus roomStatus) {
		this.roomStatus = roomStatus;
	}


	public void setRoomType(RoomType roomType) {
		this.roomType = roomType;
	}
	
	public void setReservations(HashMap<Integer, ArrayList<LocalDate>> reservations) {
		this.reservations = reservations;
	}
	
	public HashMap<Integer, ArrayList<LocalDate>> getReservations() {
		return reservations;
	}
	
//	public boolean isAvailable(LocalDate checkInDate, LocalDate checkOutDate) {
//	    for (ArrayList<LocalDate> reservationDates : reservations.values()) {
//	    	if (reservationDates.size()==0) {markRoomAsOccupied(checkInDate, checkOutDate);
//		    return true;}
//	        LocalDate reservationCheckIn = reservationDates.get(0); 
//	        LocalDate reservationCheckOut = reservationDates.get(1);
//	      
//	        
//	        if (reservationCheckIn == null || reservationCheckOut == null ){
//            	return false;
//            }
//	        else if (!checkOutDate.isBefore(reservationCheckIn) && !checkInDate.isAfter(reservationCheckOut)) {
//	            return false;
//	        }
//	    }
//	    markRoomAsOccupied(checkInDate, checkOutDate);
//	    return true;
//	}
//
//	private void markRoomAsOccupied(LocalDate checkInDate, LocalDate checkOutDate) {
//	    for (LocalDate date = checkInDate; !date.isAfter(checkOutDate); date = date.plusDays(1)) {
//	        occupiedDates.add(date);
//	    }
//	}
	
	
	
	public String toFileString() {
		StringBuilder stringBuilder = new StringBuilder();
		
		stringBuilder.append(roomNumber);
		stringBuilder.append(",");
		stringBuilder.append(roomType.getId());
		stringBuilder.append(",");
		stringBuilder.append(roomStatus.toString());
		stringBuilder.append(",");
		
		
		if (additionalServices != null) {
			
			for (RoomAdditionalService additionalService : additionalServices) {
				stringBuilder.append(additionalService.getId());
				stringBuilder.append(";");
			}
		}
		else {
			stringBuilder.append("null");
		}
		stringBuilder.append(",");
		stringBuilder.append(isDeleted);
			
		return stringBuilder.toString();
	}
	
	public boolean isDeleted() {
		return isDeleted;
	}


	public void setDeleted(boolean isDeleted) {
		this.isDeleted = isDeleted;
	}


	public ArrayList<RoomAdditionalService> getAdditionalServices() {
		return additionalServices;
	}


	public void setAdditionalServices(ArrayList<RoomAdditionalService> additionalServices) {
		this.additionalServices = additionalServices;
	}


	@Override
	public boolean equals(Object obj) {
		if (obj instanceof Room) {
			Room room = (Room) obj;
			return roomNumber == room.roomNumber;
		}
		return false;
	}
}

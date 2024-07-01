package entity;

import java.time.LocalDate;
import java.util.ArrayList;

import enums.ReservationStatus;

public class Reservation {
	protected int id;
	protected Guest guest;
    protected Room room;
    protected LocalDate checkInDate;
    protected LocalDate checkOutDate;
    protected ReservationStatus reservationStatus;
    protected int numOfGuest;
    protected RoomType roomType;
    protected ArrayList<AService> ad;
    protected ArrayList<RoomAdditionalService> adRoom;
    protected double price;
    protected LocalDate date;
    protected Maid maid;
    LocalDate checkOutDate2;
    
	
	public Reservation(
		int id, Guest guest, Room room, 
		RoomType roomType, int numOfGuest, 
		LocalDate checkInDate, LocalDate checkOutDate, 
		ArrayList<AService> ad , ReservationStatus reservationStatus, 
		double price, LocalDate date,
		ArrayList<RoomAdditionalService> adRoom,
		LocalDate checkOutDate2,
		Maid maid
	) {
		this.id = id;
		this.guest = guest;
        this.room = room;
        this.numOfGuest = numOfGuest;
        this.checkInDate = checkInDate;
        this.checkOutDate = checkOutDate;
        this.reservationStatus = reservationStatus;
        this.roomType = roomType;
        this.ad = ad;
        this.price = price;
        this.date = date;
        this.adRoom = adRoom;
        this.checkOutDate2 = checkOutDate2;
        this.maid = maid;
    }
	
	public Maid getMaid() {
		return maid;
	}

	public void setMaid(Maid maid) {
		this.maid = maid;
	}

	public LocalDate getCheckOutDate2() {
		return checkOutDate2;
	}

	public void setCheckOutDate2(LocalDate checkOutDate2) {
		this.checkOutDate2 = checkOutDate2;
	}

	public ArrayList<RoomAdditionalService> getAdRoom() {
		return adRoom;
	}
	
	public void setAdRoom(ArrayList<RoomAdditionalService> adRoom) {
		this.adRoom = adRoom;
	}

	public Guest getGuest() {
		return guest;
	}

	public void setGuest(Guest guest) {
		this.guest = guest;
	}

	public Room getRoom() {
		return room;
	}

	public void setRoom(Room room) {
		this.room = room;
	}

	public LocalDate getCheckInDate() {
		return checkInDate;
	}

	public void setCheckInDate(LocalDate checkInDate) {
		this.checkInDate = checkInDate;
	}

	public LocalDate getCheckOutDate() {
		return checkOutDate;
	}

	public void setCheckOutDate(LocalDate checkOutDate) {
		this.checkOutDate = checkOutDate;
	}

	public ReservationStatus getReservationStatus() {
		return reservationStatus;
	}

	public void setReservationStatus(ReservationStatus reservationStatus) {
		this.reservationStatus = reservationStatus;
	}
	
	public int getId() {
		return id;
	}
	
	public void setId(int id) {
		this.id = id;
	}
	
	public int getNumOfGuest() {
		return numOfGuest;
	}
	
	public void setNumOfGuest(int numOfGuest) {
		this.numOfGuest = numOfGuest;
	}
	
	public RoomType getRoomType() {
		return roomType;
	}
	
	public void setRoomType(RoomType roomType) {
		this.roomType = roomType;
	}
	
	public ArrayList<AService> getAd() {
		return ad;
	}
	
	public void setAd(ArrayList<AService> ad) {
		this.ad = ad;
	}
	
	public double getPrice() {
		return price;
	}
	
	public void setPrice(double price) {
		this.price = price;
	}
	
	public LocalDate getDate() {
		return date;
	}
	
	public void setDate(LocalDate date) {
		this.date = date;
	}
	
	
	public String toFileString() {
		
		StringBuilder sb = new StringBuilder();
		sb.append(id);
		sb.append(",");
		sb.append(guest.getId());
		sb.append(",");
		if (room == null) {sb.append("0");} else {sb.append(room.toString());}
		
		sb.append(",");
		sb.append(roomType.getId());
		sb.append(",");
		sb.append(numOfGuest);
		sb.append(",");
		sb.append(checkInDate);
		sb.append(",");
		sb.append(checkOutDate);
		sb.append(",");
		for (AService value : ad) {
			if (value == null) {sb.append(""); continue;}
		    sb.append(value.getId()).append(";");
		}
		sb.append(",");
		sb.append(reservationStatus);
		sb.append(",");
		sb.append(price);
		sb.append(",");
		sb.append(date);
		sb.append(",");
		for (RoomAdditionalService value : adRoom) {
			if ((value == null) || adRoom == null){
				sb.append("null");
				continue;
			}
			sb.append(value.getId()).append(";");
		}
		sb.append(",");
		if (checkOutDate2 == null) {sb.append("null");} else {sb.append(checkOutDate2);}
		sb.append(",");
		if (maid == null) {sb.append("null");} else {sb.append(maid.getId());}
		return sb.toString();
	}
}

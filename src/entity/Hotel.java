package entity;

import java.time.LocalTime;

public class Hotel {
	private String hotelName;
	private LocalTime startOfWorkingHours;
	private LocalTime endOfWorkingHours;
	
	
	public String getHotelName() {
		return hotelName;
	}
	public void setHotelName(String hotelName) {
		this.hotelName = hotelName;
	}
	public LocalTime getStartOfWorkingHours() {
		return startOfWorkingHours;
	}
	public void setStartOfWorkingHours(LocalTime startOfWorkingHours) {
		this.startOfWorkingHours = startOfWorkingHours;
	}
	public LocalTime getEndOfWorkingHours() {
		return endOfWorkingHours;
	}
	public void setEndOfWorkingHours(LocalTime endOfWorkingHours) {
		this.endOfWorkingHours = endOfWorkingHours;
	}
}

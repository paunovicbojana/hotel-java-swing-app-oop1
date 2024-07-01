package entity;


import java.time.LocalDate;
import java.util.ArrayList;

import enums.Gender;
import enums.LevelOfEducation;
import enums.Role;

public class Maid extends Staff {
	Role role;
	int count = 0;
	LocalDate date;
	private ArrayList<Room> roomListMaid;
	public Maid(int id, String firstName, String lastName, Gender gender, LocalDate dateOfBirth, String phone, String address, String username, String password, LevelOfEducation levelOfEducation, int yearsOfService, double salary, Role role, ArrayList<Room> roomListMaid ,boolean isDeleted, LocalDate date, int count) {
		super(id, firstName, lastName, gender, dateOfBirth, phone, address, username, password, levelOfEducation, yearsOfService, salary, role, false);
		this.role = Role.MAID;
		this.roomListMaid = roomListMaid;
		this.date = date;
		this.count = count;
	}
	
	public ArrayList<Room> getRoomListMaid() {
		return roomListMaid;
	}
	
	public void setRoomListMaid(ArrayList<Room> roomListMaid) {
		this.roomListMaid = roomListMaid;
	}
	
	public Role getRole() {
		return role;
	}
	
	public void setRole(Role role) {
		this.role = role;
	}
	
	public LocalDate getDate() {
		return date;
	}
	
	public void setDate(LocalDate date) {
		this.date = date;
	}
	
	public int getCount() {
		return count;
	}
	
	public void setCount(int count) {
		this.count = count;
	}
	
public String toFileString() {
		
		StringBuilder stringBuilder = new StringBuilder();
		stringBuilder.append(id);
		stringBuilder.append(",");
		stringBuilder.append(firstName);
		stringBuilder.append(",");
		stringBuilder.append(lastName);
		stringBuilder.append(",");
		stringBuilder.append(gender);
		stringBuilder.append(",");
		stringBuilder.append(dateOfBirth);
		stringBuilder.append(",");
		stringBuilder.append(phone);
		stringBuilder.append(",");
		stringBuilder.append(address);
		stringBuilder.append(",");
		stringBuilder.append(username);
		stringBuilder.append(",");
		stringBuilder.append(password);
		stringBuilder.append(",");
		stringBuilder.append(levelOfEducation);
		stringBuilder.append(",");
		stringBuilder.append(yearsOfService);
		stringBuilder.append(",");
		stringBuilder.append(salary);
		stringBuilder.append(",");
		stringBuilder.append(role);
		stringBuilder.append(",");
		for (Room room : roomListMaid) {
			stringBuilder.append(room.getRoomNumber());
			stringBuilder.append(";");
		}
		stringBuilder.append(",");
		stringBuilder.append(isDeleted);
		stringBuilder.append(",");
		stringBuilder.append(date);
		stringBuilder.append(",");
		stringBuilder.append(count);
		return stringBuilder.toString();
}
}

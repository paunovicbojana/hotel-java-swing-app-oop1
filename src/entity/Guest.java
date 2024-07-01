package entity;
import java.time.LocalDate;

import enums.Gender;
import enums.Role;

public class Guest extends User {
	protected Role role;
	protected Reservation reservation;
	protected double expenses;
	
	public Guest(int id, String firstName, String lastName, Gender gender, LocalDate dateOfBirth, String phone, String address, String username, String password, boolean isDeleted, double expenses) {
		super(id, firstName, lastName, gender, dateOfBirth, phone, address, username, password, isDeleted);
		this.role = Role.GUEST;
		this.expenses = expenses;
    }
	
	@Override
	public String toString() {
		return id + "";
	}
	
	public Reservation getReservation() {
		return reservation;
	}
	
	public void setReservation(Reservation reservation) {
		this.reservation = reservation;
	}
	
	public double getExpenses() {
		return expenses;
	}
	
	public void setExpenses(double expenses) {
		this.expenses = expenses;
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
		stringBuilder.append(isDeleted);
		stringBuilder.append(",");
		stringBuilder.append(expenses);
		return stringBuilder.toString();
	}
}

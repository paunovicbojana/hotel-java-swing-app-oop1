package entity;

import java.time.LocalDate;

import enums.*;

public abstract class User {
	
	protected int id;
	protected String firstName;
	protected String lastName;
    protected Gender gender;
    protected LocalDate dateOfBirth;
    protected String phone;
    protected String address;
    protected String username;
    protected String password;
    protected boolean isDeleted;
    
    //----------------------------
    
	protected User(int id, String firstName, String lastName, Gender gender, LocalDate dateOfBirth, String phone, String address, String username, String password, boolean isDeleted) {
		this.id = id;
		this.firstName = firstName;
		this.lastName = lastName;
		this.dateOfBirth = dateOfBirth;
		this.address = address;
		this.phone = phone;
		this.username = username;
		this.password = password;
		this.gender = gender;
		this.isDeleted = isDeleted;
	}
    
	public int getId() {
		return id;
	}
	
	public void setId(int id) {
		this.id = id;
	}

	public String getFirstName() {
        return firstName;
    }
	
	public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

	public String getLastName() {
		return lastName;
	}

	public void setLastName(String lastName) {
		this.lastName = lastName;
	}
	
	public Gender getGender() {
		return gender;
	}
	
	public void setGender(Gender gender) {
        this.gender = gender;
	}
	
	public LocalDate getDateOfBirth() {
		return dateOfBirth;
	}
	
	public void setDateOfBirth(LocalDate dateOfBirth) {
		this.dateOfBirth = dateOfBirth;
	}
	
	public String getPhone() {
		return phone;
	}
	
	public void setPhone(String phone) {
		this.phone = phone;
	}
	
	public String getAddress() {
		return address;
	}
	
	public void setAddress(String address) {
		this.address = address;
	}
	
	public String getUsername() {
		return username;
	}
	
	public void setUsername(String username) {
		this.username = username;
	}
	
	public String getPassword() {
		return password;
	}
	
	public void setPassword(String password) {
		this.password = password;
	}
	
	public boolean isDeleted() {
		return isDeleted;
	}
	
	public void setDeleted(boolean isDeleted) {
		this.isDeleted = isDeleted;
	}
	
	// ---------------------------
	
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
		return stringBuilder.toString();
	}

	public void logout() {
//		id = -1;
//		firstName = null;
//		lastName = null;
//		dateOfBirth = null;
//		address = null;
//		phone = null;
//		username = null;
//		password = null;
//		gender = null;
		
	}
}

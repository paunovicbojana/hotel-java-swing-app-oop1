package entity;

import java.time.LocalDate;

import enums.Gender;
import enums.LevelOfEducation;
import enums.Role;

public class Staff extends User {
	
	protected LevelOfEducation levelOfEducation;
    protected int yearsOfService;
    protected double salary;
    protected Role role;
	
	protected Staff(int id, String firstName, String lastName, Gender gender, LocalDate dateOfBirth, String phone, String address, String username, String password, LevelOfEducation levelOfEducation, int yearsOfService, double salary, Role role, boolean isDeleted) {
		super(id, firstName, lastName, gender, dateOfBirth, phone, address, username, password, isDeleted);
		this.levelOfEducation = levelOfEducation;
		this.yearsOfService = yearsOfService;
		this.salary = salary;
		this.role = role;
		
		
	}
    
	public LevelOfEducation getLevelOfEducation() {
		return levelOfEducation;
    }
	
	public void setLevelOfEducation(LevelOfEducation levelOfEducation) {
		this.levelOfEducation = levelOfEducation;
	}
	
	public int getYearsOfService() {
		return yearsOfService;
	}
	
	public void setYearsOfService(int yearsOfService) {
		this.yearsOfService = yearsOfService;
	}
	
	public double getSalary() {
		return salary;
	}
	
	public void setSalary(double salary) {
		this.salary = salary;
	}
	
	public Role getRole() {
		return role;
	}
	
	public void setRole(Role role) {
		this.role = role;
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
		stringBuilder.append(isDeleted);
	
		return stringBuilder.toString();
	
}
	
}

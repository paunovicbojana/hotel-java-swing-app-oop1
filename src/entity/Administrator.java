package entity;

import java.time.LocalDate;

import enums.*;

public class Administrator extends Staff{
	protected Role role;
	public Administrator(int id, String firstName, String lastName, Gender gender, LocalDate dateOfBirth, String phone, String address, String username, String password, LevelOfEducation levelOfEducation, int yearsOfService, double salary, Role role, boolean isDeleted) {
		super(id, firstName, lastName, gender, dateOfBirth, phone, address, username, password, levelOfEducation, yearsOfService, salary, role, false);
	    this.role = Role.ADMINISTRATOR;
	}
	
	@Override
    public String toString() {
        return "Administrator{" +
                "id=" + id +
                ", firstName='" + firstName + '\'' +
                ", lastName='" + lastName + '\'' +
                '}';
    }
}
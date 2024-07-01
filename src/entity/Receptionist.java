package entity;

import java.time.LocalDate;

import enums.Gender;
import enums.LevelOfEducation;
import enums.Role;

public class Receptionist extends Staff{
	Role role;
	public Receptionist(int id, String firstName, String lastName, Gender gender, LocalDate dateOfBirth, String phone, String address, String username, String password, LevelOfEducation levelOfEducation, int yearsOfService, double salary, Role role, boolean isDeleted) {
		super(id, firstName, lastName, gender, dateOfBirth, phone, address, username, password, levelOfEducation, yearsOfService, salary, role, isDeleted);
		this.role = Role.RECEPTIONIST;
	}
}

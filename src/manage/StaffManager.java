package manage;

import enums.LevelOfEducation;
import enums.Role;

public class StaffManager {
	private ReceptionistManager receptionistManager;
	private MaidManager maidManager;
	
	public StaffManager(ReceptionistManager receptionistManager, MaidManager maidManager) {
		this.receptionistManager = receptionistManager;
		this.maidManager = maidManager;
		this.receptionistManager.setStaffManager(this);
		this.maidManager.setStaffManager(this);
	}
	
	public ReceptionistManager getReceptionistManager() {
		return receptionistManager;
	}
	
	public MaidManager getMaidManager() {
		return maidManager;
	}
	
	public void setReceptionistManager(ReceptionistManager receptionistManager) {
		this.receptionistManager = receptionistManager;
	}
	
	public void setMaidManager(MaidManager maidManager) {
		this.maidManager = maidManager;
	}
	
	public void printStaff() {
		receptionistManager.printReceptionists();
		maidManager.printMaids();
	}
	
	public double getSalary(LevelOfEducation levelOfEducation, int yearsOfService, Role role) {
		double salary = (5*yearsOfService + 7*levelOfEducation.getOrdinal() + 5*role.getOrdinal())*1000;
		return salary;
	}
}

package manage;

import entity.Administrator;
import entity.Guest;
import entity.Maid;
import entity.Receptionist;
import entity.User;

public class LoginManager {
	AdministratorManager adminManager;
	ReceptionistManager receptionistManager;
	MaidManager maidManager;
	GuestManager guestManager;
	
	public LoginManager(AdministratorManager adminManager, ReceptionistManager receptionistManager, MaidManager maidManager, GuestManager guestManager) {
		this.adminManager = adminManager;
        this.receptionistManager = receptionistManager;
        this.maidManager = maidManager;
        this.guestManager = guestManager;
	}
	
	public User login(String username, String password) {
		for (Administrator admin : adminManager.getAdministratorList().values()) {
			if (admin.isDeleted())
				continue;
			if (admin.getUsername().equals(username) && admin.getPassword().equals(password)) {
				return admin;
			}
		}

		for (Receptionist receptionist : receptionistManager.getReceptionistList().values()) {
			if (receptionist.isDeleted())
				continue;
			if (receptionist.getUsername().equals(username) && receptionist.getPassword().equals(password)) {
				return receptionist;
			}
		}
		
		for (Maid maid : maidManager.getMaidList().values()) {
			if (maid.isDeleted())
				continue;
			if (maid.getUsername().equals(username) && maid.getPassword().equals(password)) {
				return maid;
			}
		}
		guestManager.loadData();
		for (Guest guest : guestManager.getGuestList().values()) {
			if (guest.isDeleted())
				continue;
			if (guest.getUsername().equals(username) && guest.getPassword().equals(password)) {
				return guest;
			}
		}
		
		return null;
	}

}

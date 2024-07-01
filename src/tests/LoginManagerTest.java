package tests;

import static org.junit.Assert.*;

import java.time.LocalDate;

import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import entity.Administrator;
import entity.Guest;
import entity.Maid;
import entity.Receptionist;
import enums.Gender;
import enums.LevelOfEducation;
import enums.Role;
import exceptions.MyException;
import manage.AdministratorManager;
import manage.GuestManager;
import manage.HotelManager;
import manage.LoginManager;
import manage.MaidManager;
import manage.ReceptionistManager;
import settings.Settings;

public class LoginManagerTest {
	private static LoginManager loginManager;
	private static AdministratorManager admin;
	private static ReceptionistManager receptionist;
	private static MaidManager maid;
	private static GuestManager guest;

	@BeforeClass
	public static void setUpBeforeClass() throws MyException {
		String separator = System.getProperty("file.separator");
		Settings settings = new Settings(
		    "." + separator + "src" + separator + "data" + separator + "administrators.csv",
		    "." + separator + "src" + separator + "data" + separator + "receptionists.csv",
		    "." + separator + "src" + separator + "data" + separator + "maids.csv",
		    "." + separator + "src" + separator + "data" + separator + "guests.csv",
		    "." + separator + "src" + separator + "data" + separator + "services.csv",
		    "." + separator + "src" + separator + "data" + separator + "pricelists.csv",
		    "." + separator + "src" + separator + "data" + separator + "rooms.csv",
		    "." + separator + "src" + separator + "data" + separator + "room_types.csv", 
		    "." + separator + "src" + separator + "data" + separator + "reservations.csv",
		    "." + separator + "src" + separator + "data" + separator + "room_ad_services.csv"
		);
		HotelManager hotel = new HotelManager(settings);
		loginManager = hotel.getLoginManager();
		admin = hotel.getAdminManager();
		receptionist = hotel.getReceptionistManager();
		maid = hotel.getMaidManager();
		guest = hotel.getGuestManager();
		
	}

	@Before
	public void setUp() throws MyException {
		admin.getAdministratorList().clear();
		receptionist.getReceptionistList().clear();
		maid.getMaidList().clear();
		guest.getGuestList().clear();
	}
	
	@Test
	public void testLoginAdmin() {
		assertEquals(null, loginManager.login("admin", "a"));
		assertEquals(0, admin.getAdministratorList().size());
		int a = admin.addAdministrator("Admin", "Adminić", Gender.MALE, LocalDate.parse("1999-12-13"), "181881818", "NS", "admin", "a", LevelOfEducation.DOKTORAT, 10, 200000.00, Role.ADMINISTRATOR, false);
		Administrator a1 = admin.getAdministratorByID(a);
		assertEquals(1, admin.getAdministratorList().size());
		assertEquals(a1, admin.getAdministratorList().get(0));
		assertEquals(a1, loginManager.login("admin", "a"));
	}
	
	@Test
	public void testLoginReceptionist() {
		assertEquals(0, receptionist.getReceptionistList().size());
		int r = receptionist.addReceptionist("Receptionist", "Receptionistić", Gender.MALE, LocalDate.parse("1999-12-13"), "181881818", "NS", "receptionist", "r",
				LevelOfEducation.DOKTORAT, 10, 200000.00, Role.RECEPTIONIST, false);
		Receptionist r1 = receptionist.getReceptionistByID(r);
		assertEquals(1, receptionist.getReceptionistList().size());
		assertEquals(r1, receptionist.getReceptionistList().get(0));
		assertEquals(r1, loginManager.login("receptionist", "r"));
	}
	
	@Test
	public void testLoginMaid() {
		assertEquals(0, maid.getMaidList().size());
		int m = maid.addMaid("Maid", "Maidić",Gender.MALE, LocalDate.parse("1999-12-13"), "181881818", "NS", "maid", "m", LevelOfEducation.DOKTORAT, 10, 200000.00, Role.MAID, false, null, 0);
		Maid m1 = maid.getMaidByID(m);
		assertEquals(1, maid.getMaidList().size());
		assertEquals(m1, maid.getMaidList().get(0));
		assertEquals(m1, loginManager.login("maid", "m"));
	}
	
	@Test
	public void testLoginWrong() {
		assertEquals(null, loginManager.login("admin", "b"));
	}
}

package tests;

import static org.junit.Assert.*;

import java.time.LocalDate;

import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import entity.Administrator;
import enums.Gender;
import enums.LevelOfEducation;
import enums.Role;
import exceptions.MyException;
import manage.AdministratorManager;
import manage.HotelManager;
import settings.Settings;

public class AdministratorManagerTest {
	private static AdministratorManager admin;

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
		admin = hotel.getAdminManager();
	}

	@Before
	public void setUp() throws MyException {
		admin.getAdministratorList().clear();
	}

	@Test
	public void testAddAdmin() {
		assertEquals(0, admin.getAdministratorList().size());
		int a = admin.addAdministrator("Admin", "Adminić", Gender.MALE, LocalDate.parse("1999-12-13"), "181881818", "NS", "admin", "a", LevelOfEducation.DOKTORAT, 10, 200000.00, Role.ADMINISTRATOR, false);
		Administrator a1 = admin.getAdministratorByID(a);
		assertEquals(1, admin.getAdministratorList().size());
		assertEquals(a1, admin.getAdministratorList().get(0));
	}
	
	@Test
	public void testDeleteAdmin() {
        assertEquals(0, admin.getAdministratorList().size());
        int a = admin.addAdministrator("Admin", "Adminić",Gender.MALE, LocalDate.parse("1999-12-13"), "181881818", "NS", "admin", "a", LevelOfEducation.DOKTORAT, 10, 200000.00, Role.ADMINISTRATOR, false);
        Administrator a1 = admin.getAdministratorByID(a);
        assertEquals(1, admin.getAdministratorList().size());
        admin.deleteAdministrator(a);
        assertEquals(1, admin.getAdministratorList().size());
        assertTrue(a1.isDeleted());
	}

}

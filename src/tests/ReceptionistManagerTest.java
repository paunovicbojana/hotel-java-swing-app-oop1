package tests;

import static org.junit.Assert.*;

import java.time.LocalDate;

import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import entity.Receptionist;
import enums.Gender;
import enums.LevelOfEducation;
import enums.Role;
import exceptions.MyException;
import manage.HotelManager;
import manage.ReceptionistManager;
import settings.Settings;

public class ReceptionistManagerTest {
	private static ReceptionistManager receptionist;

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
		receptionist = hotel.getReceptionistManager();
	}

	@Before
	public void setUp() throws MyException {
		receptionist.getReceptionistList().clear();
	}

	@Test
	public void testAddReceptionist() {
		assertEquals(0, receptionist.getReceptionistList().size());
		int r = receptionist.addReceptionist("Receptionist", "Receptionistić", Gender.MALE, LocalDate.parse("1999-12-13"), "181881818", "NS", "receptionist", "r",
				LevelOfEducation.DOKTORAT, 10, 200000.00, Role.RECEPTIONIST, false);
		Receptionist r1 = receptionist.getReceptionistByID(r);
		assertEquals(1, receptionist.getReceptionistList().size());
		assertEquals(r1, receptionist.getReceptionistList().get(0));
	}
	
	@Test
	public void testDeleteReceptionist() {
        assertEquals(0, receptionist.getReceptionistList().size());
        int r = receptionist.addReceptionist("Receptionist", "Receptionistić", Gender.MALE, LocalDate.parse("1999-12-13"), "181881818", "NS", "receptionist", "r",
				LevelOfEducation.DOKTORAT, 10, 200000.00, Role.RECEPTIONIST, false);
		Receptionist r1 = receptionist.getReceptionistByID(r);
		assertEquals(1, receptionist.getReceptionistList().size());
		receptionist.deleteReceptionist(r);
		assertEquals(1, receptionist.getReceptionistList().size());
		assertTrue(r1.isDeleted());
	}

}

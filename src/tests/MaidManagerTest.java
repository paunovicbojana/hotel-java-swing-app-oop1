package tests;

import static org.junit.Assert.*;

import java.time.LocalDate;

import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import entity.Maid;
import enums.Gender;
import enums.LevelOfEducation;
import enums.Role;
import exceptions.MyException;
import manage.HotelManager;
import manage.MaidManager;
import settings.Settings;

public class MaidManagerTest {
	private static MaidManager maid;

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
		maid = hotel.getMaidManager();
	}

	@Before
	public void setUp() throws MyException {
		maid.getMaidList().clear();
	}

	@Test
	public void testAddMaid() {
		assertEquals(0, maid.getMaidList().size());
		int m = maid.addMaid("Maid", "Maidić",Gender.MALE, LocalDate.parse("1999-12-13"), "181881818", "NS", "maid", "m", LevelOfEducation.DOKTORAT, 10, 200000.00, Role.MAID, false, null, 0);
		Maid m1 = maid.getMaidByID(m);
		assertEquals(1, maid.getMaidList().size());
		assertEquals(m1, maid.getMaidList().get(0));
	}
	
	@Test
	public void testDeleteMaid() {
        assertEquals(0, maid.getMaidList().size());
        int m = maid.addMaid("Maid", "Maidić",Gender.MALE, LocalDate.parse("1999-12-13"), "181881818", "NS", "maid", "m", LevelOfEducation.DOKTORAT, 10, 200000.00, Role.MAID, false, null, 0);
		Maid m1 = maid.getMaidByID(m);
		assertEquals(1, maid.getMaidList().size());
        maid.deleteMaid(m);
        assertTrue(m1.isDeleted());
	}

}

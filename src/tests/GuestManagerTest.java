package tests;

import static org.junit.Assert.*;

import java.time.LocalDate;

import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import entity.Guest;
import enums.Gender;
import exceptions.MyException;
import manage.GuestManager;
import manage.HotelManager;
import settings.Settings;

public class GuestManagerTest {
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
		guest = hotel.getGuestManager();
	}

	@Before
	public void setUp() throws MyException {
		guest.getGuestList().clear();
	}

	@Test
	public void testAddGuest() {
		assertEquals(0, guest.getGuestList().size());
		int a = guest.addGuest("Guest", "Guestić",Gender.FEMALE, LocalDate.parse("2000-12-30"), "guest", "g", "181818181", "NS", false, 0);
		Guest a1 = guest.getGuestByID(a);
		assertEquals(1, guest.getGuestList().size());
		assertEquals(a1, guest.getGuestList().get(0));
	}
	
	@Test
	public void testDeleteGuest() {
        assertEquals(0, guest.getGuestList().size());
        int a = guest.addGuest("Guest", "Guestić",Gender.FEMALE, LocalDate.parse("2000-12-30"), "guest", "g", "181818181", "NS", false, 0);
		Guest a1 = guest.getGuestByID(a);
		assertEquals(1, guest.getGuestList().size());
		guest.deleteGuest(a);
		assertEquals(1, guest.getGuestList().size());
		assertEquals(true, a1.isDeleted());
	}

}

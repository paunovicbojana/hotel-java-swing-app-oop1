package tests;

import static org.junit.Assert.*;

import java.time.LocalDate;

import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import entity.Guest;
import entity.Room;
import entity.RoomType;
import enums.Gender;
import enums.ReservationStatus;
import enums.RoomStatus;
import enums.ServiceType;
import exceptions.MyException;
import manage.AServiceManager;
import manage.GuestManager;
import manage.HotelManager;
import manage.MaidManager;
import manage.PricelistManager;
import manage.ReservationManager;
import manage.RoomAdditionalServiceManager;
import manage.RoomManager;
import manage.ServiceManager;
import settings.Settings;

public class ReservationManagerTest {
	private static ReservationManager reservationManager;
	private static GuestManager guestManager;
	private static RoomManager roomManager;
	private static AServiceManager serviceManager;
	private static PricelistManager priceListManager;
	private static RoomAdditionalServiceManager roomAdServiceManager;
	private static MaidManager maidManager;
	

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
		reservationManager = hotel.getReservationManager();
		guestManager = hotel.getGuestManager();
		roomManager = hotel.getRoomManager();
		serviceManager = hotel.getServiceManager();
		priceListManager = hotel.getPricelistManager();
		roomAdServiceManager = hotel.getRoomAdditionalServiceManager();
		maidManager = hotel.getMaidManager();
		
	}

	@Before
	public void setUp() throws MyException {
		reservationManager.getReservations().clear();
	}

	@Test
	public void testAddReservation() throws MyException {
		assertEquals(0, guestManager.getGuestList().size());
		int a = guestManager.addGuest("Guest", "Guestić",Gender.FEMALE, LocalDate.parse("2000-12-30"), "guest", "g", "181818181", "NS", false, 0);
		Guest a1 = guestManager.getGuestByID(a);
		assertEquals(1, guestManager.getGuestList().size());
		assertEquals(a1, guestManager.getGuestList().get(0));
		assertEquals(0, roomManager.getRoomTypeList().size());
		int n = roomManager.addRoomType("Single", 1, 1, 1.0, false, 1, ServiceType.ROOM_SERVICE);
		RoomType roomType = roomManager.getRoomTypeList().get(n);
		assertEquals(1, roomManager.getRoomTypeList().size());
		assertEquals("Single", roomManager.getRoomTypeList().get(0).getServiceName());
		assertEquals(0, roomManager.getRoomList().size());
		int b = roomManager.addRoom(1, roomType, RoomStatus.VACANT, null, false) - 1; 
		Room b1 = roomManager.getRoomList().get(b);
		assertEquals(1, roomManager.getRoomList().size());
		
		reservationManager.addReservation(a1, b1, roomType, 10, LocalDate.now(), LocalDate.now().plusDays(5), null, ReservationStatus.CONFIRMED, 100000.0, null, LocalDate.now(), null);
		assertEquals(1, reservationManager.getReservations().size());
		assertEquals(a1, reservationManager.getReservations().get(0).getGuest());
	}
	
}

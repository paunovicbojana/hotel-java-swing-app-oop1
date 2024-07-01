package tests;

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import exceptions.MyException;
import manage.HotelManager;
import manage.RoomAdditionalServiceManager;
import settings.Settings;

public class RoomAdditionalServiceManagerTest {
	private static RoomAdditionalServiceManager roomAdditionalServiceManager;

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
		roomAdditionalServiceManager = hotel.getRoomAdditionalServiceManager();
		
	}

	@Before
	public void setUp() throws MyException {
		roomAdditionalServiceManager.getRServiceList().clear();
	}

	@Test
	public void testGenerateServiceID() {
		assertEquals(0, roomAdditionalServiceManager.generateServiceID());
		roomAdditionalServiceManager.addServiceType("Service1", null, 1, false, 1);
		assertEquals(1, roomAdditionalServiceManager.generateServiceID());
	}
	
	@Test
	public void testAddServiceType() {
		assertEquals(0, roomAdditionalServiceManager.getRServiceList().size());
		roomAdditionalServiceManager.addServiceType("Service1", null, 1, false, 1);
		assertEquals(1, roomAdditionalServiceManager.getRServiceList().size());
		assertEquals("Service1", roomAdditionalServiceManager.getService(0).getServiceName());
	}
}

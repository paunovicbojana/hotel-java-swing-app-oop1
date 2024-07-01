package tests;

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import enums.ServiceType;
import exceptions.MyException;
import manage.AServiceManager;
import manage.HotelManager;
import manage.ServiceManager;
import settings.Settings;

public class AServiceManagerTest {
	private static AServiceManager serviceManager;

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
		serviceManager = hotel.getServiceManager();
	}

	@Before
	public void setUp() throws MyException {
		serviceManager.getServiceList().clear();
	}

	@Test
	public void testAddService() {
		assertEquals(0, serviceManager.getServiceList().size());
		serviceManager.addServiceType("Service1", ServiceType.ADDITIONAL_SERVICE, 100, false, 1);
		assertEquals(1, serviceManager.getServiceList().size());
		assertEquals("Service1", serviceManager.getServiceList().get(0).getServiceName());
		assertEquals(100, serviceManager.getServiceList().get(0).getPrice(), 0.01);
	}
	
	@Test
	public void testDeleteService() {
		assertEquals(0, serviceManager.getServiceList().size());
		serviceManager.addServiceType("Service1", ServiceType.ADDITIONAL_SERVICE, 100, false, 1);
		assertEquals(1, serviceManager.getServiceList().size());
		serviceManager.deleteService(0);
		assertTrue(serviceManager.getServiceList().get(0).isDeleted());
	}

}

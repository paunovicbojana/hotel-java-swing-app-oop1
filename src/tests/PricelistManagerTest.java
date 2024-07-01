package tests;

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import exceptions.MyException;
import manage.HotelManager;
import manage.PricelistManager;
import settings.Settings;

public class PricelistManagerTest {
	private static PricelistManager pricelistManager;

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
		pricelistManager = hotel.getPricelistManager();
		
	}

	@Before
	public void setUp() throws MyException {
		pricelistManager.getPricelistList().clear();
	}

	@Test
	public void testGeneratePricelistID() {
        assertEquals(0, pricelistManager.generatePricelistID());
        pricelistManager.generatePricelist("Pricelist1", null, null, 1);
        assertEquals(1, pricelistManager.generatePricelistID());
	}
	
	@Test
	public void testGeneratePricelist() {
        assertEquals(0, pricelistManager.getPricelistList().size());
        pricelistManager.generatePricelist("Pricelist1", null, null, 1);
        assertEquals(1, pricelistManager.getPricelistList().size());
        assertEquals("Pricelist1", pricelistManager.getPricelistList().get(0).getName());
	}
	
	@Test
	public void testAddToPricelist() {
		assertEquals(0, pricelistManager.getPricelistList().size());
		pricelistManager.generatePricelist("Pricelist1", null, null, 1);
		assertEquals(1, pricelistManager.getPricelistList().size());
		pricelistManager.addToPricelist(0, "Service1", null, 100);
		assertEquals(1, pricelistManager.getPricelistList().get(0).getServices().size());
		assertEquals("Service1", pricelistManager.getPricelistList().get(0).getServices().get(0).getServiceName());
	}
	
	@Test
	public void testRemoveFromPricelist() {
        assertEquals(0, pricelistManager.getPricelistList().size());
		pricelistManager.generatePricelist("Pricelist1", null, null, 1);
		assertEquals(1, pricelistManager.getPricelistList().size());
		pricelistManager.addToPricelist(0, "Service1", null, 100);
		assertEquals(1, pricelistManager.getPricelistList().get(0).getServices().size());
		assertEquals("Service1", pricelistManager.getPricelistList().get(0).getServices().get(0).getServiceName());
		pricelistManager.removeFromPricelist(0, 0);
		assertEquals(0, pricelistManager.getPricelistList().get(0).getServices().size());
	}
}

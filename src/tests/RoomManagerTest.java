package tests;

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import entity.RoomType;
import enums.RoomStatus;
import enums.ServiceType;
import exceptions.MyException;
import manage.HotelManager;
import manage.RoomManager;
import settings.Settings;

public class RoomManagerTest {
	private static RoomManager room;

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
		room = hotel.getRoomManager();
	}

	@Before
	public void setUp() throws MyException {
		room.getRoomTypeList().clear();
		room.getRoomList().clear();
		room.getRoomTypeCount().clear();
	}

	@Test
	public void testAddRoomType() throws MyException {
		assertEquals(0, room.getRoomTypeList().size());
		int a = room.addRoomType("Single", 1, 1, 1.0, false, 1, ServiceType.ROOM_SERVICE);
		assertEquals(1, room.getRoomTypeList().size());
		assertEquals("Single", room.getRoomTypeList().get(0).getServiceName());
	}
	
	@Test
	public void testDeleteRoomType() throws MyException {
		assertEquals(0, room.getRoomTypeList().size());
		room.addRoomType("Single", 1, 1, 1.0, false, 1, ServiceType.ROOM_SERVICE);
		assertEquals(1, room.getRoomTypeList().size());
		room.deleteRoomType(0);
		assertTrue(room.getRoomTypeList().get(0).isDeleted());
	}
	
	@Test
	public void testAddRoom() throws MyException {
		assertEquals(0, room.getRoomTypeList().size());
		int a = room.addRoomType("Single", 1, 1, 1.0, false, 1, ServiceType.ROOM_SERVICE);
		RoomType roomType = room.getRoomTypeList().get(a);
		assertEquals(1, room.getRoomTypeList().size());
		assertEquals("Single", room.getRoomTypeList().get(0).getServiceName());
		assertEquals(0, room.getRoomList().size());
		room.addRoom(1, roomType, RoomStatus.VACANT, null, false);
		assertEquals(1, room.getRoomList().size());
	}
	
	@Test
	public void testSetRoomStatus() throws MyException {
		assertEquals(0, room.getRoomTypeList().size());
		int a = room.addRoomType("Single", 1, 1, 1.0, false, 1, ServiceType.ROOM_SERVICE);
		RoomType roomType = room.getRoomTypeList().get(a);
		assertEquals(1, room.getRoomTypeList().size());
		assertEquals("Single", room.getRoomTypeList().get(0).getServiceName());
		assertEquals(0, room.getRoomList().size());
		int b = room.addRoom(1, roomType, RoomStatus.VACANT, null, false);
		assertEquals(1, room.getRoomList().size());
		room.setRoomStatus(b, RoomStatus.OCCUPIED);
		assertEquals(RoomStatus.OCCUPIED, room.getRoomStatus(b));
	}
	
	
}

package main;
import view.*;
import manage.HotelManager;
import settings.Settings;
public class HotelApp {
	public HotelApp() {
		// TODO Auto-generated constructor stub
	}

	public static void main(String[] args) {
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
		hotel.loadData();
		hotel.getReservationManager().checkExpired();
		hotel.loadIncome();
	    hotel.loadExpenses();
		new LoginFrame(hotel);
	}
}

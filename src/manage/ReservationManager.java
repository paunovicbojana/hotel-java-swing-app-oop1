package manage;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import entity.Guest;
import entity.Maid;
import entity.Reservation;
import entity.Room;
import entity.RoomAdditionalService;
import entity.RoomType;
import entity.AService;
import enums.ReservationStatus;

public class ReservationManager {
	
	String pathReservationFile;
	HashMap<Integer, Reservation> reservationsList;
	
	Map<LocalDate, ArrayList<Integer>> availableRoomTypes;
	
	
	public ReservationManager(String pathReservationFile) {
		this.pathReservationFile = pathReservationFile;
		reservationsList = new HashMap<Integer, Reservation>();	}

	public String getPathReservationFile() {
		return pathReservationFile;
	}

	public void setPathReservationFile(String pathReservationFile) {
		this.pathReservationFile = pathReservationFile;
	}

	public HashMap<Integer, Reservation> getReservations() {
		return reservationsList;
	}

	public void setReservations(HashMap<Integer, Reservation> reservationsList) {
		this.reservationsList = reservationsList;
	}
	
	public int generateReservationID() {
		int ID = reservationsList.size();
		if (ID == 0) {
			return ID;
		}
		return ID;
	}
	
	
	public int addReservation(Guest guest, Room room, RoomType rt, int numOfGuests, LocalDate checkInDate, LocalDate checkOutDate, ArrayList<AService> additionalData, ReservationStatus reservationStatus, double price, ArrayList<RoomAdditionalService> adRoom, LocalDate checkOutDate2, Maid maid) {
	    Reservation reservation = new Reservation(generateReservationID(), guest, room, rt, numOfGuests, checkInDate, checkOutDate, additionalData, reservationStatus, price, LocalDate.now(), adRoom, checkOutDate2, maid);
	    reservationsList.put(reservation.getId(), reservation);
	    return reservation.getId();
	}
	
	public int addReservation(Reservation reservation) {
	    reservationsList.put(reservation.getId(), reservation);
	    return reservation.getId();
	}

	
	public boolean saveData() {
	    if (this.pathReservationFile == null) {
	        System.err.println("File path is null.");
	        return false;
	    }

	    try (PrintWriter pw = new PrintWriter(new FileWriter(this.pathReservationFile, false))) {
	        for (Reservation reservation : reservationsList.values()) {
	            pw.println(reservation.toFileString());
	        }
	        return true;
	    } catch (IOException e) {
	        System.err.println("Failed to save data: " + e.getMessage());
	        return false;
	    }
	}
	
	public Guest getGuestById(int id, GuestManager gm) {
		return gm.getGuestByID(id);
	}
	
	public Room getRoomById(int id, RoomManager rm) {
		return rm.getRoom(id);
	}
	
	public RoomType getRoomTypeById(int id, RoomManager rm) {
		return rm.getRoomType(id);
	}
	
	public AService getServiceById(int id, AServiceManager sm) {
		return sm.getService(id);
	}
	
	

	public boolean loadData(GuestManager gm, RoomManager rm, AServiceManager sm, RoomAdditionalServiceManager ram, MaidManager mm) {
	    try {
	        BufferedReader br = new BufferedReader(new FileReader(this.pathReservationFile));
	        String linija = null;
	        while ((linija = br.readLine()) != null) {
	            String[] tokens = linija.split(",");

	            int reservationId = Integer.parseInt(tokens[0]);
	            int guestId = Integer.parseInt(tokens[1]);
	            int roomId = Integer.parseInt(tokens[2]);
	            int roomTypeId = Integer.parseInt(tokens[3]);
	            int numOfGuests = Integer.parseInt(tokens[4]);
	            LocalDate checkInDate = LocalDate.parse(tokens[5]);
	            LocalDate checkOutDate = LocalDate.parse(tokens[6]);
	            double price = Double.parseDouble(tokens[9]);
	            
	            Guest guest = getGuestById(guestId, gm);
	            Room room = getRoomById(roomId, rm);
	            RoomType roomType = getRoomTypeById(roomTypeId, rm);

	            ArrayList<AService> services = new ArrayList<>();
	            String[] serviceStrings = tokens[7].split(";");
	            for (String serviceString : serviceStrings) {
	                if (!serviceString.equals("")) {
	                    services.add(getServiceById(Integer.parseInt(serviceString), sm));
	                } else {
	                    services.add(null);
	                }
	            }

	            ReservationStatus reservationStatus = ReservationStatus.valueOf(tokens[8]);
	            LocalDate date = LocalDate.parse(tokens[10]);
	            String[] roomAdditionalServices = tokens[11].split(";");
	            
	            ArrayList<RoomAdditionalService> additionalData = new ArrayList<>();
				for (String serviceString : roomAdditionalServices) {
					if (!serviceString.equals("") && !serviceString.equals("null")) {
						additionalData.add(ram.getService(Integer.parseInt(serviceString)));
					}
					else {
						additionalData.add(null);
					}
				}
				LocalDate checkOutDate2 = null;
				if (tokens[12].equals("null")) {
					checkOutDate2 = null;
				}
				else {
					checkOutDate2 = LocalDate.parse(tokens[12]);
				}
				Maid maid = null;
				if (tokens[13].equals("null")) {
					maid = null;
				}
				else{int maidId = Integer.parseInt(tokens[13]);
				maid = mm.getMaidList().get(maidId);}
	            

	            Reservation reservation = new Reservation(
	                reservationId,
	                guest,
	                room,
	                roomType,
	                numOfGuests,
	                checkInDate,
	                checkOutDate,
	                services,
	                reservationStatus,
	                price,
	                date, 
	                additionalData,
	                checkOutDate2,
	                maid
	            );
				
	            this.reservationsList.put(reservationId, reservation);
	            
	        }
	        br.close();
	    } catch (IOException e) {
	        System.err.println("Failed to load data: " + e.getMessage());
	        return false;
	    }
	    
	    return true;
	}
	
	public void checkExpired() {
		for (Reservation r : reservationsList.values()) {
			if (r.getCheckInDate().isBefore(LocalDate.now()) && r.getReservationStatus() == ReservationStatus.ONHOLD) {
				r.setReservationStatus(ReservationStatus.REJECTED);
				double a = r.getPrice();
				r.getGuest().setExpenses(r.getGuest().getExpenses() - a);
				r.setPrice(0);
				
			}
		}
		
	}
}

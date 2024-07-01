package manage;

import entity.Guest;
import entity.Reservation;
import entity.Staff;
import enums.ReservationStatus;
import settings.Settings;

public class HotelManager {
    private AdministratorManager adminManager;
    private StaffManager staffManager;
    private RoomManager roomManager;
    private AServiceManager aServiceManager;
    private ReceptionistManager receptionistManager;
    private MaidManager maidManager;
    private PricelistManager pricelistManager;
    private LoginManager loginManager;
    private GuestManager guestManager;
    private ReservationManager reservationManager;
    private ServiceManager serviceManager;
    private RoomAdditionalServiceManager roomAdditionalServiceManager;
    private double income = 0;
    private double expenses = 0;
    
    public HotelManager(Settings settings) {
        this.adminManager = new AdministratorManager(settings.getPathAdminFile());
        this.receptionistManager = new ReceptionistManager(settings.getPathReceptionistFile(), settings.getPathGuestFile());
        this.maidManager = new MaidManager(settings.getPathMaidFile());
        this.guestManager = new GuestManager(settings.getPathGuestFile());
        this.staffManager = new StaffManager(receptionistManager, maidManager);
        this.serviceManager = new ServiceManager();
        this.roomAdditionalServiceManager = new RoomAdditionalServiceManager(settings.getPathRoomAdditionalServiceFile(), serviceManager.getServiceList());
        this.roomManager = new RoomManager(settings.getPathRoomFile(), settings.getPathRoomTypeFile(), serviceManager.getServiceList(), roomAdditionalServiceManager);
        this.aServiceManager = new AServiceManager(settings.getPathServiceFile(), serviceManager.getServiceList());
        this.pricelistManager = new PricelistManager(settings.getPathPricelistFile(), serviceManager);
        this.loginManager = new LoginManager(adminManager, receptionistManager, maidManager, guestManager);
        this.reservationManager = new ReservationManager(settings.getPathReservationFile());
       
    }

    public AdministratorManager getAdminManager() {
        return this.adminManager;
    }
    
	public ReceptionistManager getReceptionistManager() {
		return this.receptionistManager;
	}
	
	public MaidManager getMaidManager() {
		return this.maidManager;
	}
	
	public StaffManager getStaffManager() {
		return this.staffManager;
	}
	
	public void setStaffManager(StaffManager staffManager) {
		this.staffManager = staffManager;
	}
	
	public RoomManager getRoomManager() {
		return this.roomManager;
	}
    
	public AServiceManager getServiceManager() {
		return this.aServiceManager;
	}
	
	public PricelistManager getPricelistManager() {
		return this.pricelistManager;
	}
	
	public void setPricelistManager(PricelistManager pricelistManager) {
		this.pricelistManager = pricelistManager;
	}
	
	public LoginManager getLoginManager() {
		return this.loginManager;
	}
	
	public void setLoginManager(LoginManager loginManager) {
		this.loginManager = loginManager;
	}
	
	public ReservationManager getReservationManager() {
		return this.reservationManager;
	}
	
	public void setReservationManager(ReservationManager reservationManager) {
		this.reservationManager = reservationManager;
	}
	
	public GuestManager getGuestManager() {
		return this.guestManager;
	}
	
	public void setGuestManager(GuestManager guestManager) {
		this.guestManager = guestManager;
	}
	
	public RoomAdditionalServiceManager getRoomAdditionalServiceManager() {
		return this.roomAdditionalServiceManager;
	}
	
	public void setRoomAdditionalServiceManager(RoomAdditionalServiceManager roomAdditionalServiceManager) {
		this.roomAdditionalServiceManager = roomAdditionalServiceManager;
	}
	
	public ServiceManager getAllServiceManager() {
		return this.serviceManager;
	}
	
	public void setAllServiceManager(ServiceManager serviceManager) {
		this.serviceManager = serviceManager;
	}
	
	public double getIncome() {
		return this.income;
	}
	
	public void setIncome(double income) {
		this.income = income;
	}
	
	public double getExpenses() {
		return this.expenses;
	}
	
	public void setExpenses(double expenses) {
		this.expenses = expenses;
	}
	
	public void addIncome(int income) {
		this.income += income;
	}
	
	public void addExpenses(int expenses) {
		this.expenses += expenses;
	}
	
	public void loadIncome() {
		for (Reservation reservation : reservationManager.getReservations().values()) {
			if (reservation.getReservationStatus() != ReservationStatus.REJECTED) {
				income += reservation.getPrice();
			}
		}
	}
	
	public void loadExpenses() {
		for (Staff staff : staffManager.getMaidManager().getMaidList().values()) {
			if (!staff.isDeleted())
			expenses += staff.getSalary();
		}
		for (Staff staff : staffManager.getReceptionistManager().getReceptionistList().values()) {
			if (!staff.isDeleted())
			expenses += staff.getSalary();
		}
		for (Staff staff : adminManager.getAdministratorList().values()) {
			if (!staff.isDeleted())
			expenses += staff.getSalary();
		}
		
	}
    
	
	
	public void loadData() {
		adminManager.loadData();
		receptionistManager.loadData();
		roomAdditionalServiceManager.loadData();
		roomManager.loadRTData();
		roomManager.loadData();
		aServiceManager.loadData();
		pricelistManager.loadData();
		guestManager.loadData();
		maidManager.loadData(roomManager);
		reservationManager.loadData(guestManager , roomManager, aServiceManager, roomAdditionalServiceManager, maidManager);
		
	}
	
	public void saveData() {
		adminManager.saveData();
		receptionistManager.saveData();
		maidManager.saveData();
		roomAdditionalServiceManager.saveData();
		guestManager.saveData();
		aServiceManager.saveData();
		pricelistManager.saveData();
		roomManager.saveData();
		roomManager.saveRTData();
		reservationManager.saveData();
	}

}

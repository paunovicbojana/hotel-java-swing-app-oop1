package settings;

public class Settings {
	private String pathAdminFile;
	private String pathReceptionistFile;
	private String pathMaidFile;
	private String pathGuestFile;
	private String pathServiceFile;
	private String pathPricelistFile;
	private String pathRoomFile;
	private String pathRoomTypeFile;
	private String pathReservationFile;
	private String pathRoomAdditionalServiceFile;
	
	public Settings(String pathAdminFile, String pathReceptionistFile, String pathMaidFile, String pathGuestFile, String pathServiceFile, String pathPricelistFile, String pathRoomFile, String pathRoomTypeFile, String pathReservationFile, String pathRoomAdditionalServiceFile) {
		this.pathAdminFile = pathAdminFile;
		this.pathReceptionistFile = pathReceptionistFile;
		this.pathMaidFile = pathMaidFile;
		this.pathGuestFile = pathGuestFile;
		this.pathServiceFile = pathServiceFile;
		this.pathPricelistFile = pathPricelistFile;
		this.pathRoomFile = pathRoomFile;
		this.pathRoomTypeFile = pathRoomTypeFile;
		this.pathReservationFile = pathReservationFile;
		this.pathRoomAdditionalServiceFile = pathRoomAdditionalServiceFile;
    }
	
	public String getPathReceptionistFile() {
		return pathReceptionistFile;
	}

	public void setPathReceptionistFile(String pathReceptionistFile) {
		this.pathReceptionistFile = pathReceptionistFile;
	}

	public String getPathMaidFile() {
		return pathMaidFile;
	}

	public void setPathMaidFile(String pathMaidFile) {
		this.pathMaidFile = pathMaidFile;
	}
	
	public String getPathAdminFile() {
		return pathAdminFile;
	}
	
	public void setPathAdminFile(String pathAdminFile) {
		this.pathAdminFile = pathAdminFile;
	}

	public String getPathGuestFile() {
		return pathGuestFile;
	}

	public void setPathGuestFile(String pathGuestFile) {
		this.pathGuestFile = pathGuestFile;
	}
	
	public String getPathServiceFile() {
		return pathServiceFile;
	}
	
	public void setPathServiceFile(String pathServiceFile) {
		this.pathServiceFile = pathServiceFile;
	}
	
	public String getPathPricelistFile() {
		return pathPricelistFile;
	}
	
	public void setPathPricelistFile(String pathPricelistFile) {
		this.pathPricelistFile = pathPricelistFile;
	}
	
	public String getPathRoomFile() {
		return pathRoomFile;
	}
	
	public void setPathRoomFile(String pathRoomFile) {
		this.pathRoomFile = pathRoomFile;
	}
	
	public String getPathRoomTypeFile() {
		return pathRoomTypeFile;
	}
	
	public void setPathRoomFileType(String pathRoomTypeFile) {
		this.pathRoomTypeFile = pathRoomTypeFile;
	}

	public String getPathReservationFile() {
		return pathReservationFile;
	}
	
	public void setPathReservationFile(String pathReservationFile) {
		this.pathReservationFile = pathReservationFile;
	}
	
	public String getPathRoomAdditionalServiceFile() {
		return pathRoomAdditionalServiceFile;
	}
	
	public void setPathRoomAdditionalServiceFile(String pathRoomAdditionalServiceFile) {
		this.pathRoomAdditionalServiceFile = pathRoomAdditionalServiceFile;
	}
}

package enums;

public enum Role {
	ADMINISTRATOR(5), 
	RECEPTIONIST(4), 
	MAID(3), 
	GUEST(0);
	
	private final int ordinal;

	Role(int ordinal) {
	    this.ordinal = ordinal;
	}

	public int getOrdinal() {
	    return ordinal;
	}
}



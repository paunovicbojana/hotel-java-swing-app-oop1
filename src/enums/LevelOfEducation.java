package enums;

public enum LevelOfEducation {
	OSNOVNA_SKOLA(1),
    TROGODISNJA_SREDNJA_SKOLA(2),
    CETVOROGODISNJA_SREDNJA_SKOLA(3),
    OSNOVNE_STRUKOVNE_STUDIJE(4),
    MASTER_STRUKOVNE_STUDIJE(5),
    OSNOVNE_AKADEMSKE_STUDIJE(6),
    MASTER_AKADEMSKE_STUDIJE(7),
    DOKTORAT(8);
	
	private final int ordinal;

	LevelOfEducation(int ordinal) {
	    this.ordinal = ordinal;
	}

	public int getOrdinal() {
	    return ordinal;
	}
}

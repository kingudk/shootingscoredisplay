package dk.kingu.shootingscoredisplay.datastore.DIF;


/**
 * Enum class to denote a shots type 
 */
public enum ShotType {
	UNKNOWN(0), SIGHTERS(1), COMPETITION(2);
	
	private int val;
	
	ShotType(int val) {
		this.val = val;
	}
	
	public int value() {
		return val;
	}
	
}

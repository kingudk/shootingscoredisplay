package dk.kingu.shootingscoredisplay.datastore.DIF;

/**
 * Enum class to denote the state that a given match is in 
 */
public enum MatchState {
	UNKNOWN(-1),
	CREATED(0), 
	RUNNING(1), 
	COMPLETED(2);
	
	private int val;
	
	MatchState(int val) {
		this.val = val;
	}
	
	public int value() {
		return val;
	}
	
	public static MatchState fromValue(int value) {
		for(MatchState state : MatchState.values()) {
			if(state.value() == value) {
				return state;
			}
		}
		return MatchState.UNKNOWN;
	}
	
}

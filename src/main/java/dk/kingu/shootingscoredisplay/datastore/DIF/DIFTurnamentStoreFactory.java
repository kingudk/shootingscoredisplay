package dk.kingu.shootingscoredisplay.datastore.DIF;

public class DIFTurnamentStoreFactory {

	private static DIFTurnamentStore store;
	
	public synchronized static DIFTurnamentStore getDIFTurnamentStore() {
		if(store == null) {
			store = new DIFTurnamentStore();
		}
		return store;
	}
}

package dk.kingu.shootingscoredisplay.datastore;

public class MemoryBasedDataStoreFactory {

	private static MemoryBasedDataStore store;
	
	public synchronized static DataStore getDataStore() {
		if(store == null) {
			store = new MemoryBasedDataStore();
		}
		return store;
	}
}

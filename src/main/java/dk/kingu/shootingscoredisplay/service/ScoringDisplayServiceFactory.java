package dk.kingu.shootingscoredisplay.service;

public class ScoringDisplayServiceFactory {

	private static ScoringDisplayService service;
	
	public static synchronized ScoringDisplayService getScoringDisplayService() {
		if(service == null) {
			service = new ScoringDisplayService();
		}
		return service;
	}
}

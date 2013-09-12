package dk.kingu.shootingscoredisplay;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

import dk.kingu.shootingscoredisplay.service.ScoringDisplayServiceFactory;

/**
 * Class handling startup of ShootingScoreDisplay webapplication in a Tomcat servlet server. 
 */
public class ShootingScoreDisplayContextListener implements ServletContextListener {

	public void contextDestroyed(ServletContextEvent arg0) {
		System.out.println("ShootingScoreDisplay shutting down...");
	}

	public void contextInitialized(ServletContextEvent arg0) {
		System.out.println("ShootingScoreDisplay starting up...");
		ScoringDisplayServiceFactory.getScoringDisplayService();
		System.out.println("started :)");
	}
	

}

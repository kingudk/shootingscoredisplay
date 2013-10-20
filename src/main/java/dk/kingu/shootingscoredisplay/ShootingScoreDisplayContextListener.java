package dk.kingu.shootingscoredisplay;

import java.io.IOException;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

import dk.kingu.shootingscoredisplay.service.ScoringDisplayServiceFactory;

/**
 * Class handling startup of ShootingScoreDisplay webapplication in a Tomcat servlet server. 
 */
public class ShootingScoreDisplayContextListener implements ServletContextListener {

	public void contextDestroyed(ServletContextEvent sce) {
		System.out.println("ShootingScoreDisplay shutting down...");
	}

	public void contextInitialized(ServletContextEvent sce) {
        String configFile = sce.getServletContext().getInitParameter("config");
		System.out.println("ShootingScoreDisplay starting up...");
		try {
			ScoringDisplayServiceFactory.init(configFile);
		} catch (IOException e) {
			throw new RuntimeException("Failed to initialize service.", e);
		}
		ScoringDisplayServiceFactory.getScoringDisplayService();
		
		System.out.println("started :)");
	}
	

}

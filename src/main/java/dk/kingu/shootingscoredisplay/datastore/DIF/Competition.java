package dk.kingu.shootingscoredisplay.datastore.DIF;

import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
public class Competition {

	private int competitionID;
	private Shooter shooter;
	
	public int getCompetitionID() {
		return competitionID;
	}
	public void setCompetitionID(int competitionID) {
		this.competitionID = competitionID;
	}
	public Shooter getShooter() {
		return shooter;
	}
	public void setShooter(Shooter shooter) {
		this.shooter = shooter;
	}
	
	
}

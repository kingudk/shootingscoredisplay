package dk.kingu.shootingscoredisplay.datastore.DIF;

import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
public class Shooter {

	private int id;
	private String shooterName;
	private String clubName;
	
	public String getShooterName() {
		return shooterName;
	}
	public void setShooterName(String name) {
		this.shooterName = name;
	}
	public String getClubName() {
		return clubName;
	}
	public void setClubName(String club) {
		this.clubName = club;
	}
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	
	
}

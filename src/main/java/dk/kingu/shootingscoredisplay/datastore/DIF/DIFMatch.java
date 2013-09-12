package dk.kingu.shootingscoredisplay.datastore.DIF;

import java.util.List;

import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
public class DIFMatch {

	private String place;
	private String date;
	private String team1, team2;
	private List<String> team1lanes, team2lanes;
	private String matchName;
	
	DIFMatch(String team1, String team2, List<String> team1lanes, List<String> team2lanes, String date) {
		this.matchName = team1 + "-" + team2;
		this.team1 = team1;
		this.team2 = team2;
		this.team1lanes = team1lanes;
		this.team2lanes = team2lanes;
		place = "Skytterneshus"; //FIXME Make this a parameter
		this.date = date;
	}
	
	public DIFMatch() {
		place = "Skytterneshus";
	}
	
	public String getMatchName() {
		return matchName;
	}
	
	public String getTeam1() {
		return team1;
	}
	
	public String getTeam2() {
		return team2;
	}
	
	public List<String> getTeam1Lanes() {
		return team1lanes;
	}
	
	public List<String> getTeam2Lanes() {
		return team2lanes;
	}
	
	public String getDate() {
		return date;
	}
	
	public String getPlace() {
		return place;
	}

	public void setDate(String date) {
		this.date = date;
	}

	public void setTeam1(String team1) {
		this.team1 = team1;
	}

	public void setTeam2(String team2) {
		this.team2 = team2;
	}

	public void setTeam1lanes(List<String> team1lanes) {
		this.team1lanes = team1lanes;
	}

	public void setTeam2lanes(List<String> team2lanes) {
		this.team2lanes = team2lanes;
	}
	
	public void setPlace(String place) {
		this.place = place;
	}
}

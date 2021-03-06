package dk.kingu.shootingscoredisplay.datastore.DIF;

import java.util.List;

import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
public class Match {
	
    private int matchID;
    private String name;
	private List<Integer> teamA;
	private List<Integer> teamB;
	private MatchState state;
	
   public int getMatchID() {
        return matchID;
    }
    public void setMatchID(int matchID) {
        this.matchID = matchID;
    }
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public List<Integer> getTeamA() {
		return teamA;
	}
	public void setTeamA(List<Integer> teamA) {
		this.teamA = teamA;
	}
	public List<Integer> getTeamB() {
		return teamB;
	}
	public void setTeamB(List<Integer> teamB) {
		this.teamB = teamB;
	}
	public MatchState getState() {
		return state;
	}
	public void setState(MatchState state) {
		this.state = state;
	}
	

}

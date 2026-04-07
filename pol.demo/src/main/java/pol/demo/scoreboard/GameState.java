package pol.demo.scoreboard;

import java.util.ArrayList;
import java.util.List;

public class GameState {
    private String homeTeam = "HOME";
    private String awayTeam = "AWAY";
    private int homeScore = 0;
    private int awayScore = 0;
    private int period = 1;
    private int clockSeconds = 20 * 60;
    private boolean clockRunning = false;
    private List<Penalty> homePenalties = new ArrayList<>();
    private List<Penalty> awayPenalties = new ArrayList<>();
    private String tournamentName = "HOCKEY";

    // Getters and setters
    public String getHomeTeam() { return homeTeam; }
    public void setHomeTeam(String homeTeam) { this.homeTeam = homeTeam; }

    public String getAwayTeam() { return awayTeam; }
    public void setAwayTeam(String awayTeam) { this.awayTeam = awayTeam; }

    public int getHomeScore() { return homeScore; }
    public void setHomeScore(int homeScore) { this.homeScore = homeScore; }

    public int getAwayScore() { return awayScore; }
    public void setAwayScore(int awayScore) { this.awayScore = awayScore; }

    public int getPeriod() { return period; }
    public void setPeriod(int period) { this.period = period; }

    public int getClockSeconds() { return clockSeconds; }
    public void setClockSeconds(int clockSeconds) { this.clockSeconds = clockSeconds; }

    public boolean isClockRunning() { return clockRunning; }
    public void setClockRunning(boolean clockRunning) { this.clockRunning = clockRunning; }

    public List<Penalty> getHomePenalties() { return homePenalties; }
    public void setHomePenalties(List<Penalty> homePenalties) { this.homePenalties = homePenalties; }

    public List<Penalty> getAwayPenalties() { return awayPenalties; }
    public void setAwayPenalties(List<Penalty> awayPenalties) { this.awayPenalties = awayPenalties; }

    public String getTournamentName() { return tournamentName; }
    public void setTournamentName(String tournamentName) { this.tournamentName = tournamentName; }
}

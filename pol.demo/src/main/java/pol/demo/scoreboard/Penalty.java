package pol.demo.scoreboard;

public class Penalty {
    private String playerNumber;
    private int remainingSeconds;

    public Penalty() {}

    public Penalty(String playerNumber, int remainingSeconds) {
        this.playerNumber = playerNumber;
        this.remainingSeconds = remainingSeconds;
    }

    public String getPlayerNumber() { return playerNumber; }
    public void setPlayerNumber(String playerNumber) { this.playerNumber = playerNumber; }

    public int getRemainingSeconds() { return remainingSeconds; }
    public void setRemainingSeconds(int remainingSeconds) { this.remainingSeconds = remainingSeconds; }
}

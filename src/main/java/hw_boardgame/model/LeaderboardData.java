package hw_boardgame.model;

/**
 * This class was made for the LeaderboardController for reading JSON.
 */
public class LeaderboardData {
    private String playerName;
    private int highScore;
    private int playerRank;

    /**
     * This method returns the player name from the target JSON.
     * @return The player name from the target JSON.
     */
    public String getPlayerName() {
        return playerName;
    }

    /**
     * This method returns the player score from the target JSON.
     * @return The player score from the target JSON.
     */
    public int getHighScore() {
        return highScore;
    }

    /**
     * This method returns the player rank.
     * @return The player rank.
     */
    public int getPlayerRank() {
        return playerRank;
    }

    /**
     * This method sets the player rank.
     * @param rank Is the rank of the player.
     */
    public void setPlayerRank(int rank) {
        playerRank = rank;
    }
}

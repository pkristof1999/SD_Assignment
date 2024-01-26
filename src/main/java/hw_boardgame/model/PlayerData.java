package hw_boardgame.model;

/**
 * <p>This class is made to collect and modify player data and score.</p>
 */
public class PlayerData {
    static private String playerOneName;
    static private String playerTwoName;
    static private KingColor playerOneColor;
    static private KingColor playerTwoColor;
    static private int playerOneScore = 2500;
    static private int playerTwoScore = 2500;

    /**
     * This method sets the name of player one.
     * @param name Is the new name assigned to player one.
     */
    static public void setPlayerOneName(String name) {
        playerOneName = name;
    }

    /**
     * This method sets the name of player two.
     * @param name Is the new name assigned to player two.
     */
    static public void setPlayerTwoName(String name) {
        playerTwoName = name;
    }

    /**
     * This method sets the color of player one's king, based on user input.
     * @param color Is the new color assigned to player one.
     */
    static public void setPlayerOneColor(KingColor color) {
        playerOneColor = color;
    }

    /**
     * This method sets the color of player two's king, based on user input.
     * @param color Is the new color assigned to player two.
     */
    static public void setPlayerTwoColor(KingColor color) {
        playerTwoColor = color;
    }

    /**
     * This method resets the player score to default value.
     */
    static public void resetPlayerScore() {
        playerOneScore = 2500;
        playerTwoScore = 2500;
    }

    /**
     * This method adds 100 points to player one's score.
     */
    static public void addPlayerOneScore() {
        playerOneScore += 100;
    }

    /**
     * In case of winning, this method adds 2000 points to player one's score.
     */
    static public void addWinningScoreToPlayerOne() {
        playerOneScore += 2000;
    }

    /**
     * In case of winning, this method adds 2000 points to player two's score.
     */
    static public void addWinningScoreToPlayerTwo() {
        playerTwoScore += 2000;
    }

    /**
     * This method adds 100 points to player two's score.
     */
    static public void addPlayerTwoScore() {
        playerTwoScore += 100;
    }

    /**
     * This method subtracts 100 points from player one's score.
     */
    static public void subsPlayerOneScore() {
        if (playerOneScore > 0) {
            playerOneScore -= 100;
        }
    }

    /**
     * This method subtracts 100 points from player two's score.
     */
    static public void subsPlayerTwoScore() {
        if (playerTwoScore > 0) {
            playerTwoScore -= 100;
        }
    }

    /**
     * This method returns the name of player one.
     * @return The name of player one.
     */
    static public String getPlayerOneName() {
        return playerOneName;
    }

    /**
     * This method returns the name of player two.
     * @return The name of player two.
     */
    static public String getPlayerTwoName() {
        return playerTwoName;
    }

    /**
     * This method returns the king color for player one.
     * @return The color of player one's king.
     */
    static public KingColor getPlayerOneColor() {
        return playerOneColor;
    }

    /**
     * This method returns the king color for player two.
     * @return The color of player two's king.
     */
    static public KingColor getPlayerTwoColor() {
        return playerTwoColor;
    }

    /**
     * This method returns the score of player one.
     * @return The score of player one.
     */
    static public int getPlayerOneScore() {
        return playerOneScore;
    }

    /**
     * This method returns the score of player one.
     * @return The score of player one.
     */
    static public int getPlayerTwoScore() {
        return playerTwoScore;
    }
}

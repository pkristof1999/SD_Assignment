package hw_boardgame.model;

/**
 * This record is made for observing and changing to the selectable positions.
 * @param row is the current row value of the position.
 * @param col is the current column value of the position.
 */
public record KingPosition(int row, int col) {

    /**
     * This method returns with a new position after a step.
     * @param direction is the direction of the king's next step.
     * @return with the new position of the king.
     */
    public KingPosition moveTo(Direction direction) {
        return new KingPosition(row + direction.getRowChange(), col + direction.getColChange());
    }
}

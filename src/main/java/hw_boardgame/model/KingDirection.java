package hw_boardgame.model;

/**
 * This enum represents the directions of the possible steps on the board.
 */
public enum KingDirection implements Direction {
    UP_LEFT(-1, -1),
    UP(-1, 0),
    UP_RIGHT(-1, 1),
    RIGHT(0, 1),
    DOWN_RIGHT(1, 1),
    DOWN(1, 0),
    DOWN_LEFT(1, -1),
    LEFT(0, -1);

    private final int rowChange;
    private final int colChange;

    /**
     * This constructor constructs a new KingDirection with the given row and column.
     * @param rowChange is the value of the row change.
     * @param colChange is the value of the column change.
     */
    KingDirection(int rowChange, int colChange) {
        this.rowChange = rowChange;
        this.colChange = colChange;
    }

    /**
     * This method returns the value of the row changing.
     * @return The value of the row changing.
     */
    public int getRowChange() {
        return rowChange;
    }

    /**
     * This method returns the value of the column changing.
     * @return The value of the column changing.
     */
    public int getColChange() {
        return colChange;
    }

    /**
     * This method returns the direction, if it is valid.
     * @param rowChange Is the row change value.
     * @param colChange Is the column change value.
     * @return The direction if available.
     */
    public static KingDirection of(int rowChange, int colChange) {
        for (var direction : values()) {
            if (direction.rowChange == rowChange && direction.colChange == colChange) {
                return direction;
            }
        }
        throw new IllegalArgumentException();
    }
}

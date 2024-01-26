package hw_boardgame.model;

import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;

/**
 * This class is made to declare the KingPiece objects.
 */
public class KingPiece {
    private final KingColor type;
    private final ObjectProperty<KingPosition> position = new SimpleObjectProperty<>();

    /**
     * This is the constructor of the KingPiece object.
     * @param type sets the color of the king.
     * @param position sets the position of the king.
     */
    public KingPiece(KingColor type, KingPosition position) {
        this.type = type;
        this.position.set(position);
    }

    /**
     * This method returns the color of the king piece.
     * @return The color of the king piece.
     */
    public KingColor getType() {
        return type;
    }

    /**
     * This method returns the position of the king piece.
     * @return The position of the king piece.
     */
    public KingPosition getPosition() {
        return position.get();
    }

    /**
     * This method moves the king to the next position.
     * @param direction Is the direction where the king should move.
     */
    public void moveTo(Direction direction) {
        KingPosition newPosition = position.get().moveTo(direction);
        position.set(newPosition);
    }

    /**
     * This method returns the position property of the KingPosition object.
     * @return The position property of the KingPosition object.
     */
    public ObjectProperty<KingPosition> positionProperty() {
        return position;
    }
}

package hw_boardgame.model;

import javafx.beans.property.ReadOnlyObjectProperty;
import javafx.beans.property.ReadOnlyObjectWrapper;
import javafx.beans.property.ObjectProperty;
import java.util.*;

/**
 * This class is the main model for the board game.
 */
@SuppressWarnings("unchecked")
public class GameModel {
    private final KingPiece[] pieces;
    private final ReadOnlyObjectWrapper<TileDelete>[][] board = new ReadOnlyObjectWrapper[6][8];

    /**
     * This method puts the pieces on the board, also sets up a grid for the deletion phase.
     */
    public GameModel() {
        this(new KingPiece(KingColor.WHITE, new KingPosition(2, 0)),
                new KingPiece(KingColor.BLACK, new KingPosition(3, 7)));

        for (var i = 0; i < 6; i++) {
            for (var j = 0; j < 8; j++) {
                board[i][j] = new ReadOnlyObjectWrapper<>(TileDelete.AVAILABLE);
            }
        }
    }

    /**
     * This method returns the property of a tile on the board.
     * @param i Is the row number.
     * @param j Is the column number.
     * @return The property of a tile based on position.
     */
    public ReadOnlyObjectProperty<TileDelete> TileProperty(int i, int j) {
        return board[i][j].getReadOnlyProperty();
    }

    /**
     * This method sets the property of a tile from available to deleted.
     * @param i Is the row number.
     * @param j Is the column number.
     */
    public void Delete(int i, int j) {
        if (board[i][j].get() == TileDelete.AVAILABLE) {
            board[i][j].set(TileDelete.DELETED);
        }
    }

    /**
     * This is the constructor for the model. It also checks if the pieces are valid.
     * @param pieces Is the king pieces.
     */
    public GameModel(KingPiece... pieces) {
        checkPieces(pieces);
        this.pieces = pieces.clone();
    }

    private void checkPieces(KingPiece[] pieces) {
        var seen = new HashSet<KingPosition>();
        for (var piece : pieces) {
            if (isOnBoard(piece.getPosition()) || seen.contains(piece.getPosition())) {
                throw new IllegalArgumentException();
            }
            seen.add(piece.getPosition());
        }
    }

    /**
     * This method returns the number of pieces on the board.
     * @return The number of pieces on the board.
     */
    public int getPieceCount() {
        return pieces.length;
    }

    /**
     * This method returns the color of the king piece by its identifier.
     * @param pieceNumber Is the identifier for the king piece.
     * @return The color of the king piece by its identifier.
     */
    public KingColor getPieceType(int pieceNumber) {
        return pieces[pieceNumber].getType();
    }

    /**
     * This method returns the position of the king piece by its identifier
     * @param pieceNumber Is the identifier for the king piece.
     * @return The position of the king piece by its identifier
     */
    public KingPosition getPiecePosition(int pieceNumber) {
        return pieces[pieceNumber].getPosition();
    }

    /**
     * This method returns the position property of the king piece by its identifier
     * @param pieceNumber Is the identifier for the king piece.
     * @return The position property of the king piece by its identifier
     */
    public ObjectProperty<KingPosition> positionProperty(int pieceNumber) {
        return pieces[pieceNumber].positionProperty();
    }

    /**
     * This method checks, if the direction the king to be move is valid.
     * @param pieceNumber Is the identifier for the king piece.
     * @param direction Is the direction where the king will move.
     * @return A boolean if the move is valid or not.
     */
    public boolean isValidMove(int pieceNumber, KingDirection direction) {
        if (pieceNumber < 0 || pieceNumber >= pieces.length) {
            throw new IllegalArgumentException();
        }
        KingPosition newPosition = pieces[pieceNumber].getPosition().moveTo(direction);
        if (isOnBoard(newPosition)) {
            return false;
        }
        for (var piece : pieces) {
            if (piece.getPosition().equals(newPosition)) {
                return false;
            }
        }
        return true;
    }

    /**
     * This method returns all the available directions a king can make.
     * @param pieceNumber Is the identifier for the king piece.
     * @return The set of valid moves.
     */
    public Set<KingDirection> getValidMoves(int pieceNumber) {
        EnumSet<KingDirection> validMoves = EnumSet.noneOf(KingDirection.class);
        for (var direction : KingDirection.values()) {
            if (isValidMove(pieceNumber, direction)) {
                validMoves.add(direction);
            }
        }
        return validMoves;
    }

    /**
     * This method moves the king to the next position.
     * @param pieceNumber Is the identifier for the king piece.
     * @param direction Is the direction where the king should move.
     */
    public void move(int pieceNumber, KingDirection direction) {
        pieces[pieceNumber].moveTo(direction);
    }

    /**
     * This method checks the initially available positions on the board.
     * @param position Is the position of a tile.
     * @return The initially available positions.
     */
    public static boolean isOnBoard(KingPosition position) {
        return 0 > position.row() || position.row() >= 6
                || 0 > position.col() || position.col() >= 8;
    }

    /**
     * This method stores the current piece positions.
     * @return The current piece positions.
     */
    public List<KingPosition> getPiecePositions() {
        List<KingPosition> positions = new ArrayList<>(pieces.length);
        for (var piece : pieces) {
            positions.add(piece.getPosition());
        }
        return positions;
    }

    /**
     * This method returns the piece number - if available - of a king piece by its position.
     * @param position Is the position of a king piece.
     * @return The position of a king piece if available.
     */
    public OptionalInt getPieceNumber(KingPosition position) {
        for (int i = 0; i < pieces.length; i++) {
            if (pieces[i].getPosition().equals(position)) {
                return OptionalInt.of(i);
            }
        }
        return OptionalInt.empty();
    }
}

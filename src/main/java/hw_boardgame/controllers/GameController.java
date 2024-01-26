package hw_boardgame.controllers;

import hw_boardgame.model.*;
import com.google.gson.*;
import javafx.scene.image.Image;
import org.tinylog.*;

import javafx.beans.value.ObservableValue;
import javafx.beans.binding.ObjectBinding;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.GridPane;
import javafx.scene.control.Label;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.io.FileReader;
import java.io.IOException;
import java.io.FileWriter;
import java.util.Objects;
import java.util.ArrayList;
import java.util.List;

/**
 * This is the controller for the main stage of the game.
 * The controller contains the necessary logic for the steps and deletions on the board.
 * It also fills up the leaderboard.json, if a player wins.
 */
public class GameController {
    private SelectionPhase selectionPhase = SelectionPhase.SELECT_FROM;
    private final List<KingPosition> selectablePositions = new ArrayList<>();
    private final List<KingPosition> flaggedPositions = new ArrayList<>();
    private final Gson gson = new GsonBuilder().setPrettyPrinting().create();
    private final GameModel model = new GameModel();
    private KingPosition selected;
    private KingColor playerColor;
    private boolean isClicked = false;
    private boolean writePlayerOneScore = false;
    private boolean writePlayerTwoScore = false;
    private boolean playerOneSteps = false;
    private boolean playerTwoSteps = false;
    private boolean canNextStep = true;
    @FXML
    private GridPane playArea;
    @FXML
    private Label playerOneNameField;
    @FXML
    private Label playerTwoNameField;
    @FXML
    private TextField playerOneScoreField;
    @FXML
    private TextField playerTwoScoreField;
    @FXML
    private TextArea messagesArea;
    @FXML
    private void initialize() {
        createBoard();
        createPieces();
        playerStart();
        setSelectablePositions();
        showSelectablePositions();
        playerSteps();
        playerOneNameField.setText("The score of " + PlayerData.getPlayerOneName() + ":");
        playerTwoNameField.setText("The score of " + PlayerData.getPlayerTwoName() + ":");
        playerOneScoreField.setText(Integer.toString(2500));
        playerTwoScoreField.setText(Integer.toString(2500));
        playerOneScoreField.setEditable(false);
        playerTwoScoreField.setEditable(false);
        messagesArea.setEditable(false);
    }

    /**
     * <p>This enum is for altering the selectable positions.</p>
     */
    private enum SelectionPhase {
        SELECT_FROM,
        SELECT_TO;

        public SelectionPhase alter() {
            return switch (this) {
                case SELECT_FROM -> SELECT_TO;
                case SELECT_TO -> SELECT_FROM;
            };
        }
    }

    /**
     * <p>This method creates the board for the king steps, and for the tile deletion.</p>
     */
    private void createBoard() {
        for (int i = 0; i < playArea.getRowCount(); i++) {
            for (int j = 0; j < playArea.getColumnCount(); j++) {
                var square = createSquare();
                var deleted = deleteTile(i, j);
                playArea.add(square, j, i);
                playArea.add(deleted, j, i);
            }
        }
    }

    /**
     * <p>This method creates the tiles for the king steps, and for the tile deletion.</p>
     */
    private VBox createSquare() {
        var square = new VBox();
        square.getStyleClass().add("square");
        square.setOnMouseClicked(this::handleMouseClick);
        return square;
    }

    /**
     * This method returns with the square, if the king is on it.
     * @param position Is the position of the king.
     * @return The square, where the king is on.
     */
    private VBox getSquare(KingPosition position) {
        for (var child : playArea.getChildren()) {
            if (GridPane.getRowIndex(child) != null &&
                    GridPane.getRowIndex(child) == position.row() &&
                    GridPane.getColumnIndex(child) != null &&
                    GridPane.getColumnIndex(child) == position.col()) {
                return (VBox) child;
            }
        }
        throw new AssertionError();
    }

    /**
     * <p>This is the method for creating new pieces upon position changing.</p>
     */
    private void createPieces() {
        for (int i = 0; i < model.getPieceCount(); i++) {
            model.positionProperty(i).addListener(this::piecePositionChange);
            var piece = createPiece(model.getPieceType(i));
            getSquare(model.getPiecePosition(i)).getChildren().add(piece);
        }
    }

    /**
     * <p>This method assigns the white or black king symbol based on color.</p>
     * @param color Is the color of the king.
     * @return The king symbol based on color.
     */
    private Node createPiece(KingColor color) {
        @SuppressWarnings("unused")
        var piece = new Label(color == KingColor.WHITE ? "♔" : "♚");
        return piece;
    }

    /**
     * <p>This is the method for initializing the steps based on player choice.</p>
     */
    private void playerStart() {
        if (PlayerData.getPlayerOneColor() == KingColor.WHITE) {
            playerOneSteps = true;
            playerColor = PlayerData.getPlayerOneColor();
            PlayerData.addPlayerTwoScore();
        } else {
            playerTwoSteps = true;
            playerColor = PlayerData.getPlayerTwoColor();
            PlayerData.addPlayerOneScore();
        }
    }

    /**
     * <p>This method writes down which player should come next in turns.</p>
     */
    @FXML
    private void playerSteps() {
        if (canNextStep) {
            if (playerOneSteps) {
                playerTwoSteps = true;
                playerOneSteps = false;
            } else if (playerTwoSteps) {
                playerOneSteps = true;
                playerTwoSteps = false;
            }
            if (playerOneSteps) {
                playerColor = PlayerData.getPlayerOneColor();
                messagesArea.setText("Player " + PlayerData.getPlayerTwoName() + " is next!");
                Logger.info("Player {} is next!", PlayerData.getPlayerTwoName());
                PlayerData.subsPlayerOneScore();
                playerOneScoreField.setText(Integer.toString(PlayerData.getPlayerOneScore()));
            } else if (playerTwoSteps) {
                playerColor = PlayerData.getPlayerTwoColor();
                messagesArea.setText("Player " + PlayerData.getPlayerOneName() + " is next!");
                Logger.info("Player {} is next!", PlayerData.getPlayerOneName());
                PlayerData.subsPlayerTwoScore();
                playerTwoScoreField.setText(Integer.toString(PlayerData.getPlayerTwoScore()));
            }
        }
    }

    /**
     * <p>This method handles the mouse clicks on the tiles.</p>
     * <p>Left mouse click moves the king in the possible direction.</p>
     * <p>Right mouse click deletes a tile when possible.</p>
     * @param event Is the mouse click event.
     */
    @FXML
    private void handleMouseClick(MouseEvent event) {
        if (event.getButton() == MouseButton.PRIMARY && !isClicked) {
            var square = (VBox) event.getSource();
            var row = GridPane.getRowIndex(square);
            var col = GridPane.getColumnIndex(square);
            var position = new KingPosition(row, col);
            Logger.info("Left click on square {}", position);
            if (selectablePositions.contains(position) && !flaggedPositions.contains(position)) {
                handleClickOnSquare(position);
            }
        } else if (event.getButton() == MouseButton.SECONDARY && isClicked) {
            var square = (VBox) event.getSource();
            var row = GridPane.getRowIndex(square);
            var col = GridPane.getColumnIndex(square);
            var position = new KingPosition(row, col);
            Logger.info("Right click on square {}", position);
            flaggedPositions.addAll(model.getPiecePositions());
            if (!flaggedPositions.contains(position)) {
                model.Delete(row, col);
                isClicked = false;
                flaggedPositions.add(position);
                canNextStep = true;
                Logger.info("Deleted row: {}, col: {} from the board", row, col);
                playerSteps();
            }
            flaggedPositions.removeAll(model.getPiecePositions());
            if (!isClicked) {
                showSelectablePositions();
            }
        } else {
            event.consume();
        }
    }

    /**
     * <p>This method writes down the possible steps, when {@code handleMouseClick} calls it.</p>
     * @param position Is the position of the king.
     */
    @SuppressWarnings("OptionalGetWithoutIsPresent")
    private void handleClickOnSquare(KingPosition position) {
        switch (selectionPhase) {
            case SELECT_FROM -> {
                if (selectablePositions.contains(position)) {
                    selectPosition(position);
                    alterSelectionPhase();
                }
                isClicked = false;
            }
            case SELECT_TO -> {
                if (selectablePositions.contains(position) && !flaggedPositions.contains(position)) {
                    var pieceNumber = model.getPieceNumber(selected).getAsInt();
                    var direction = KingDirection.of(position.row() - selected.row(), position.col() - selected.col());
                    Logger.info("Moving piece {} {}", pieceNumber, direction);
                    model.move(pieceNumber, direction);
                    deselectSelectedPosition();
                    alterSelectionPhase();
                }
                canNextStep = false;
                isClicked = true;
                hideSelectablePositions();
            }
        }
    }

    /**
     * <p>This method writes down the possible deletions, when {@code handleMouseClick} calls it.</p>
     * @return From available to deleted.
     */
    private VBox deleteTile(int i, int j) {
        var square = new VBox();
        square.getStyleClass().add("deletedSquare");
        var piece = new Label("×");
        piece.visibleProperty().bind(
                new ObjectBinding<>() {
                    {
                        super.bind(model.TileProperty(i, j));
                    }

                    @Override
                    protected Boolean computeValue() {
                        return switch (model.TileProperty(i, j).get()) {
                            case AVAILABLE -> false;
                            case DELETED -> true;
                        };
                    }
                }
        );
        if (model.TileProperty(i, j).get() == TileDelete.AVAILABLE)
            square.getChildren().add(piece);
        square.setOnMouseClicked(this::handleMouseClick);
        return square;
    }

    /**
     * <p>This method if for showing selectable positions for the king.</p>
     * <p>It also have a counter, if there are no more selectable positions, the method calls for the
     * {@code endOfRound} method.</p>
     */
    private void showSelectablePositions() {
        int selectableCounter = 0;
        for (var selectablePosition : selectablePositions) {
            var square = getSquare(selectablePosition);
            if (!flaggedPositions.contains(selectablePosition)) {
                square.getStyleClass().add("selectable");
                selectableCounter++;
            }
            square.getStyleClass().remove("otherPiece");
        }
        if (selectableCounter == 0) {
            endOfRound();
        }
    }

    /**
     * <p>This method hides the selectable positions.</p>
     */
    private void hideSelectablePositions() {
        for (var selectablePosition : selectablePositions) {
            var square = getSquare(selectablePosition);
            square.getStyleClass().remove("selectable");
            square.getStyleClass().add("otherPiece");
        }
    }

    /**
     * <p>This method alters the selectable positions, using {@code SelectionPhase}.</p>
     */
    private void alterSelectionPhase() {
        selectionPhase = selectionPhase.alter();
        hideSelectablePositions();
        setSelectablePositions();
        showSelectablePositions();
    }

    /**
     * <p>This method shows the possible steps, after clicking on the king.</p>
     * @param position Is the position of the king.
     */
    private void selectPosition(KingPosition position) {
        selected = position;
        showSelectedPosition();
    }

    /**
     * <p>This method selects the available positions.</p>
     */
    @SuppressWarnings({"OptionalGetWithoutIsPresent", "SuspiciousMethodCalls"})
    private void setSelectablePositions() {
        selectablePositions.clear();
        switch (selectionPhase) {
            case SELECT_FROM -> {
                for (var position : model.getPiecePositions()) {
                    var pieceNumber = model.getPieceNumber(position).getAsInt();
                    var pieceColor = model.getPieceType(pieceNumber);
                    var square = getSquare(position);
                    if (pieceColor == playerColor) {
                        selectablePositions.add(position);
                    } else {
                        square.getStyleClass().add("otherPiece");
                    }
                }
            }
            case SELECT_TO -> {
                var pieceNumber = model.getPieceNumber(selected).getAsInt();
                for (var direction : model.getValidMoves(pieceNumber)) {
                    if (!flaggedPositions.contains(direction)) {
                        selectablePositions.add(selected.moveTo(direction));
                    }
                }
            }
        }
    }

    /**
     * <p>This method is to show the available tiles on the board.</p>
     */
    private void showSelectedPosition() {
        var square = getSquare(selected);
        square.getStyleClass().add("selected");
    }

    /**
     * <p>This method is to deselect the available tiles on the board.</p>
     */
    private void deselectSelectedPosition() {
        hideSelectedPosition();
        selected = null;
    }

    /**
     * <p>This method is to hide the available tiles on the board.</p>
     */
    private void hideSelectedPosition() {
        var square = getSquare(selected);
        square.getStyleClass().remove("selected");
    }

    /**
     * <p>This method is called when changing the position of the king on the board.</p>
     * @param observable Is represents the position property of a king piece that is being observed for changes.
     * @param oldPosition Is the position a king piece moved from.
     * @param newPosition Is the position a king piece moved to.
     */
    private void piecePositionChange(ObservableValue<? extends KingPosition> observable, KingPosition oldPosition, KingPosition newPosition) {
        Logger.info("Move: {} -> {}", oldPosition, newPosition);
        VBox oldSquare = getSquare(oldPosition);
        VBox newSquare = getSquare(newPosition);
        newSquare.getChildren().addAll(oldSquare.getChildren());
        oldSquare.getChildren().clear();
    }

    /**
     * <p>This method is called, when a player wins.</p>
     */
    @FXML
    private void endOfRound() {
        var square = getSquare(selected);
        square.getStyleClass().add("losingPiece");
        if (playerOneSteps) {
            PlayerData.addWinningScoreToPlayerOne();
            Logger.info("There are no more possible moves, {} wins!", PlayerData.getPlayerOneName());
            playerOneScoreField.setText(Integer.toString(PlayerData.getPlayerOneScore()));
            messagesArea.setText("There are no more possible moves, " + PlayerData.getPlayerOneName() + " wins!");
            writePlayerOneScore = true;
            writeScoreToJson();
        } else {
            PlayerData.addWinningScoreToPlayerTwo();
            Logger.info("There are no more possible moves, {} wins!", PlayerData.getPlayerTwoName());
            playerTwoScoreField.setText(Integer.toString(PlayerData.getPlayerTwoScore()));
            messagesArea.setText("There are no more possible moves, " + PlayerData.getPlayerTwoName() + " wins!");
            writePlayerTwoScore = true;
            writeScoreToJson();
        }
    }

    /**
     * <p>This method is called by {@code endOfRound}, it writes the winner's data in leaderboard.json file.</p>
     */
    private void writeScoreToJson() {
        try (FileReader json = new FileReader("src/main/resources/leaderboard.json")) {
            JsonElement jsonElement = JsonParser.parseReader(json);
            JsonArray jsonArray = jsonElement.getAsJsonArray();

            if (writePlayerOneScore) {
                JsonObject newScore = new JsonObject();
                newScore.addProperty("playerName", PlayerData.getPlayerOneName());
                newScore.addProperty("highScore", PlayerData.getPlayerOneScore());
                jsonArray.add(newScore);
                Logger.info("The score of the winner ({}) is now stored in the leaderboard!",
                            PlayerData.getPlayerOneName());
            } else if (writePlayerTwoScore) {
                JsonObject newScore = new JsonObject();
                newScore.addProperty("playerName", PlayerData.getPlayerTwoName());
                newScore.addProperty("highScore", PlayerData.getPlayerTwoScore());
                jsonArray.add(newScore);
                Logger.info("The score of the winner ({}) is now stored in the leaderboard!",
                        PlayerData.getPlayerTwoName());
            }
            try (FileWriter writer = new FileWriter("src/main/resources/leaderboard.json")) {
                gson.toJson(jsonArray, writer);
                writer.flush();
            } catch (IOException e) {
                Logger.error(new RuntimeException(e));
            }
        } catch (IOException e) {
            Logger.error(new RuntimeException(e));
        }
    }

    @FXML
    private void openStartScene(ActionEvent event) throws IOException {
        PlayerData.resetPlayerScore();
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        Parent root = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("/startui.fxml")));
        stage.setScene(new Scene(root));
        stage.show();
    }

    @FXML
    private void openLeaderboardScene() throws IOException {
        Stage stage = new Stage();
        stage.initModality(Modality.WINDOW_MODAL);
        Parent root = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("/leaderboardui.fxml")));
        Image icon = new Image(Objects.requireNonNull(getClass().getResourceAsStream("/img/King.png")));
        stage.setScene(new Scene(root));
        stage.setTitle("Leaderboard");
        stage.getIcons().add(icon);
        stage.show();
    }

    @FXML
    private void exitButtonAction(ActionEvent event) {
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        stage.close();
    }
}

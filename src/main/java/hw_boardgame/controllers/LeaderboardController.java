package hw_boardgame.controllers;

import com.google.gson.reflect.TypeToken;
import hw_boardgame.model.*;

import javafx.collections.ObservableList;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.stage.Stage;

import java.io.*;
import java.util.*;
import java.util.stream.IntStream;

import com.google.gson.Gson;
import javafx.collections.FXCollections;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;

/**
 * This controller controls the leaderboard stage for the board game.
 * After loading the stage, the {@code initialize} method will read the leaderboard.json
 * and sort the players by high score. If there is a tie, then it will be sorted by name.
 * After sorting, it will assign a rank to the player.
 */
public class LeaderboardController {
    Gson gson = new Gson();
    @FXML
    private TableView<LeaderboardData> LeaderboardTable;
    @FXML
    private TableColumn<LeaderboardData, Integer> playerRank;
    @FXML
    private TableColumn<LeaderboardData, String> playerName;
    @FXML
    private TableColumn<LeaderboardData, Integer> highScore;
    @FXML
    private void initialize() throws IOException {
        playerRank.setCellValueFactory(new PropertyValueFactory<>("playerRank"));
        playerName.setCellValueFactory(new PropertyValueFactory<>("playerName"));
        highScore.setCellValueFactory(new PropertyValueFactory<>("highScore"));

        List<LeaderboardData> leaderboardData = gson.fromJson(
                new FileReader("src/main/resources/leaderboard.json"),
                new TypeToken<List<LeaderboardData>>() {}.getType());

        Comparator<LeaderboardData> byHighScore = Comparator.comparingInt(LeaderboardData::getHighScore).reversed();
        Comparator<LeaderboardData> byName = Comparator.comparing(LeaderboardData::getPlayerName);
        leaderboardData.sort(byHighScore.thenComparing(byName));

        ObservableList<LeaderboardData> observableList = FXCollections.observableArrayList();
        observableList.addAll(leaderboardData);
        LeaderboardTable.setItems(observableList);

        IntStream.range(0, leaderboardData.size())
                .forEach(i -> leaderboardData.get(i).setPlayerRank(i + 1));
    }

    @FXML
    private void refreshButtonAction() throws IOException {
        initialize();
    }
    @FXML
    private void exitButtonAction(ActionEvent event) {
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        stage.close();
    }
}

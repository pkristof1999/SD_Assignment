package hw_boardgame.controllers;

import hw_boardgame.model.KingColor;
import hw_boardgame.model.PlayerData;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.BooleanProperty;
import javafx.beans.binding.Bindings;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.TextField;
import javafx.scene.control.Button;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.Scanner;

/**
 * This class contains the controller for the game's start stage, where the players can give their data.
 */
@SuppressWarnings("rawtypes")
public class StartController {
    @FXML
    private TextField nameField1;
    @FXML
    private TextField nameField2;
    @FXML
    private ChoiceBox colorBox1;
    @FXML
    private ChoiceBox colorBox2;
    @FXML
    private Button playButton;

    @FXML
    private void openGameScene(ActionEvent event) throws IOException {
        BooleanProperty isDisabled = new SimpleBooleanProperty();
        isDisabled.bind(Bindings.createBooleanBinding(() ->
                nameField1.getText().isEmpty() || nameField2.getText().isEmpty() ||
                colorBox1.getValue() == null || colorBox2.getValue() == null,
                nameField1.textProperty(), nameField2.textProperty()));
        playButton.disableProperty().bind(isDisabled);
        if (!playButton.isDisabled() &&
                !nameField1.getText().equals(nameField2.getText()) &&
                !colorBox1.getValue().equals(colorBox2.getValue())) {
            PlayerData.setPlayerOneName(nameField1.getText());
            PlayerData.setPlayerTwoName(nameField2.getText());
            if (colorBox1.getValue().toString().equals("BLACK")) {
                PlayerData.setPlayerOneColor(KingColor.BLACK);
                PlayerData.setPlayerTwoColor(KingColor.WHITE);
            } else if (colorBox1.getValue().toString().equals("WHITE")) {
                PlayerData.setPlayerOneColor(KingColor.WHITE);
                PlayerData.setPlayerTwoColor(KingColor.BLACK);
            }
            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            FXMLLoader startLoader = new FXMLLoader(getClass().getResource("/gameui.fxml"));
            Parent root = startLoader.load();
            stage.setScene(new Scene(root));
            stage.show();
        } else {
            Alert alert = new Alert(AlertType.WARNING);
            Stage stage = (Stage) alert.getDialogPane().getScene().getWindow();
            alert.setTitle("Warning");
            stage.getIcons().add(new Image("/img/King.png"));
            alert.setHeaderText("Please check the given player info!");
            alert.showAndWait();
        }
    }

    @FXML
    private void rulesButtonAction() throws FileNotFoundException {
        FileReader reader = new FileReader("src/main/resources/rules.txt");
        Scanner sc = new Scanner(reader);
        StringBuilder rules = new StringBuilder();
        while (sc.hasNextLine()) {
            rules.append(sc.nextLine()).append("\n");
        }
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        Stage stage = (Stage) alert.getDialogPane().getScene().getWindow();
        alert.setTitle("Rules");
        stage.getIcons().add(new Image("/img/King.png"));
        alert.setHeaderText(rules.toString());
        alert.showAndWait();
    }

    @FXML
    private void exitButtonAction(ActionEvent event) {
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        stage.close();
    }
}
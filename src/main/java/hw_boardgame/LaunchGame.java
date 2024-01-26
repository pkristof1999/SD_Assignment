package hw_boardgame;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.*;
import javafx.stage.Stage;
import java.io.IOException;
import java.util.Objects;

/**
 * This class opens the start stage for the game.
 */
public class LaunchGame extends Application {

    @Override
    public void start(Stage stage) throws IOException {
        Parent root = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("/startui.fxml")));
        Image icon = new Image(Objects.requireNonNull(getClass().getResourceAsStream("/img/King.png")));
        stage.setTitle("Board Game");
        Scene startScene = new Scene(root);
        stage.getIcons().add(icon);
        stage.setScene(startScene);
        stage.setResizable(false);
        stage.show();
    }
}
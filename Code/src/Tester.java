/*
FXML Tester for Game

Darragh O'Halloran
 */

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;

import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.io.IOException;

public class Tester extends Application {

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) throws IOException {

        //Load resources from our XML file
        FXMLLoader loader = new FXMLLoader(getClass().getResource("GameProjectFXML.fxml"));

        //Load them into a scene we cast to VBox because that's the rootpane but I'm sure there's a non hard-coded way to do it ;(
        Scene newScene = new Scene((VBox)loader.load());
        MainGameController gameController = (MainGameController)loader.getController();

        //Css was too ambitious - leaving it in only because I may continue this on my own - Darragh O
        //newScene.getStylesheets().add(Tester.class.getResource("maybe.css").toExternalForm());
        primaryStage.setScene(newScene);
        primaryStage.setTitle("CastleDelver");
        primaryStage.setHeight(900);
        primaryStage.setWidth(1600);
        primaryStage.show();
        //Display game
        gameController.startUp();
        Alert alert = new Alert(Alert.AlertType.INFORMATION, "Press File -> Control Player One to play alongside! Controls are disabled until you check that field. \nYou can also use the arrow keys to move!", ButtonType.OK);
        alert.showAndWait();



    }
}
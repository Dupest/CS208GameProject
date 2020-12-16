

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;

import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.io.IOException;

public class Tester extends Application {

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("GameProjectFXML.fxml"));
        //Now we have access to getController() through the instance... don't forget the type cast
        Scene newScene = new Scene((VBox)loader.load());
        MainGameController gameController = (MainGameController)loader.getController();
        //newScene.getStylesheets().add(Tester.class.getResource("maybe.css").toExternalForm());
        primaryStage.setScene(newScene);
        primaryStage.setTitle("CastleDelver");
        primaryStage.setHeight(900);
        primaryStage.setWidth(1600);
        primaryStage.show();
        gameController.startUp();


    }
}
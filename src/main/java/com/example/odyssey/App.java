package com.example.odyssey;

import java.io.IOException;

import com.example.odyssey.controller.AppController;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;



public class App extends Application {

    @Override
    public void start(Stage stage) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/com/example/odyssey/hello-view.fxml"));
        Image image = new Image(getClass().getResource("/static/airplane.png").toExternalForm());
        Scene scene = new Scene(fxmlLoader.load());
        AppController controller = fxmlLoader.getController();
        stage.setTitle("Trip UI");
        stage.getIcons().add(image);
        stage.setScene(scene);
        stage.show();
    }

    public static void main(String[] args) {
        System.setProperty("prism.lcdtext", "false");
        launch();
    }
}
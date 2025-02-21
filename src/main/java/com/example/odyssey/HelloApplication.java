package com.example.odyssey;

import java.io.IOException;

import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;



public class HelloApplication extends Application {

    @Override
    public void start(Stage stage) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/com/example/odyssey/hello-view.fxml"));
        Image image = new Image(getClass().getResource("/airplane.png").toExternalForm());
        Scene scene = new Scene(fxmlLoader.load());
        HelloController controller = fxmlLoader.getController();
        stage.setTitle("Trip UI");
        stage.getIcons().add(image);
        stage.setScene(scene);
        stage.show();
    }

    public static void main(String[] args) {
        launch();
    }
}
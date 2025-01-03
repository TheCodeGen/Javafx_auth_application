package com.example.odyssey;

import javafx.event.Event;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Cursor;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.RadioButton;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.media.MediaView;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.scene.shape.Rectangle;

import java.net.URL;
import java.util.ResourceBundle;

public class HelloController implements Initializable {

    @FXML
    private Pane mediaPane;

    @FXML
    private HBox socialButtonsBox;

    @FXML
    private RadioButton darkModeToggle;

    @FXML
    private GridPane rootPane;

    private Scene currentScene;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        addSocialButtons();

        // Video setup (unchanged)
        String videoPath = getClass().getResource("/video6.mp4").toExternalForm();
        Media media = new Media(videoPath);
        MediaPlayer mediaPlayer = new MediaPlayer(media);
        mediaPlayer.setAutoPlay(true);
        mediaPlayer.setCycleCount(MediaPlayer.INDEFINITE);

        MediaView mediaView = new MediaView(mediaPlayer);
        mediaView.setPreserveRatio(true);

        Rectangle clip = new Rectangle();
        clip.setArcWidth(60);
        clip.setArcHeight(60);
        clip.widthProperty().bind(mediaPane.widthProperty());
        clip.heightProperty().bind(mediaPane.heightProperty());
        mediaPane.setClip(clip);

        mediaPane.widthProperty().addListener((obs, oldWidth, newWidth) -> adjustMediaView(mediaView, mediaPane));
        mediaPane.heightProperty().addListener((obs, oldHeight, newHeight) -> adjustMediaView(mediaView, mediaPane));

        mediaPane.getChildren().add(mediaView);
        mediaPlayer.play();
    }

    private void addSocialButtons() {
        String[] icons = {"/apple-logo.png",
                "/google.png",
                "/facebook.png"};

        for (String iconPath : icons) {
            ImageView icon = new ImageView(new Image(getClass().getResourceAsStream(iconPath)));
            icon.setFitWidth(20);
            icon.setFitHeight(20);
            Button button = new Button();
            button.setCursor(Cursor.HAND);
            button.setGraphic(icon);
            button.getStyleClass().add("social-button");
            socialButtonsBox.getChildren().add(button);
        }
    }

    public void setScene(Scene scene){
        this.currentScene = scene;
    }



    private void adjustMediaView(MediaView mediaView, Pane mediaPane) {
        double paneWidth = mediaPane.getWidth();
        double paneHeight = mediaPane.getHeight();
        double videoWidth = mediaView.getMediaPlayer().getMedia().getWidth();
        double videoHeight = mediaView.getMediaPlayer().getMedia().getHeight();

        if (videoWidth == 0 || videoHeight == 0) return;

        double scaleWidth = paneWidth / videoWidth;
        double scaleHeight = paneHeight / videoHeight;
        double scale = Math.max(scaleWidth, scaleHeight);

        mediaView.setFitWidth(videoWidth * scale);
        mediaView.setFitHeight(videoHeight * scale);

        mediaView.setTranslateX((paneWidth - mediaView.getFitWidth()) / 2);
        mediaView.setTranslateY((paneHeight - mediaView.getFitHeight()) / 2);
    }

    @FXML
    public void setDarkMode(Event ActionEvent){
        currentScene.getStylesheets().clear();

        if (darkModeToggle.isSelected()){
            darkModeToggle.setText("LightMode");
            rootPane.getStyleClass().add("dark-mode");
        }else{
            darkModeToggle.setText("DarkMode");
            rootPane.getStyleClass().remove("dark-mode");
        }

        System.out.println("Active Stylesheets!!" + currentScene.getStylesheets());
    }
}

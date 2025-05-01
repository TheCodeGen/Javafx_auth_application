package com.example.odyssey.controller;

import com.example.odyssey.service.ApiService;
import javafx.animation.*;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.media.MediaView;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.scene.shape.Rectangle;
import java.io.*;
import java.net.URL;
import java.util.ResourceBundle;
import java.util.concurrent.CompletableFuture;
import javafx.util.Duration;

public class AppController implements Initializable {

    @FXML
    private Pane mediaPane;

    @FXML
    private HBox rootPane;

    @FXML
    private TextField nameField;

    @FXML
    private TextField emailField;

    @FXML
    private PasswordField passwordField;

    @FXML
    private Label passwordErrorLabel;

    @FXML
    private Hyperlink loginToggle;

    @FXML
    private VBox signupPane;

    @FXML
    private VBox loginPane;

    @FXML
    private StackPane leftSection;

    @FXML
    private TextField loginUsernameField;

    @FXML
    private PasswordField loginPasswordField;

    private boolean isLogin;

    @Override
    public void initialize (URL url, ResourceBundle resourceBundle)  {
        hidePasswordErrorMessage();
        loginPane.setVisible(false);
        loginPane.setManaged(false);

        Media media = new Media(getClass().getResource("/static/travelvideo.mp4").toExternalForm());
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

    public void hidePasswordErrorMessage(){
        passwordErrorLabel.setVisible(false);
        passwordErrorLabel.setManaged(false);
    }

    public void showPasswordErrorMessage(){
        passwordErrorLabel.setVisible(true);
        passwordErrorLabel.setManaged(true);
    }

    private void initializeLoginInfo(String username, String password) {
        loginUsernameField.setText(username);
        loginPasswordField.setText(password);
    }

    private Boolean allFieldsValid(TextField... field){
        boolean valid = true;
        for (TextField f : field){
            if (f.getText().trim().isEmpty()){
                f.getStyleClass().add("text-field-error");
                valid = false;
                break;
            }else{
                f.getStyleClass().remove("text-field-error");
            }
        }
        return valid;
    }

    @FXML
    public void createAccount(ActionEvent event) throws IOException {
        boolean isAllFieldsValid = allFieldsValid(nameField, emailField, passwordField);

        if(isAllFieldsValid){
            String name = nameField.getText().trim().toLowerCase();
            String email = emailField.getText().trim().toLowerCase();
            String password = passwordField.getText().trim();

            hidePasswordErrorMessage();
            CompletableFuture<Boolean> result = ApiService.registerUser(name, email, password);
            result.thenAccept(success -> Platform.runLater(() -> {
                if (success){
                    new Alert(Alert.AlertType.INFORMATION, "Registration Successful").show();
                    initializeLoginInfo(name, password);
                }else{
                    new Alert(Alert.AlertType.ERROR, "Registration Unsuccessful").show();
                }
            }));
        }
        nameField.clear();
        emailField.clear();
        passwordField.clear();
        switchToLogin(new ActionEvent());
    }

    public void loginUser(ActionEvent event) throws IOException {
        String userInfo = loginUsernameField.getText().toLowerCase();
        String password = loginPasswordField.getText().toLowerCase();
        CompletableFuture<Boolean> result = ApiService.loginUser(userInfo, password);
        result.thenAccept(success -> Platform.runLater(() -> {
            if (success){
                new Alert(Alert.AlertType.INFORMATION, "Login Successful").show();
            }else{
                new Alert(Alert.AlertType.ERROR, "Login Unsuccessful").show();
            }
        }));
    }

    public void switchToLogin(ActionEvent event){
        if (!isLogin){
            TranslateTransition videoTransition = new TranslateTransition();
            videoTransition.setDuration(Duration.seconds(1));
            videoTransition.setByX(leftSection.getLayoutX() - mediaPane.getLayoutX());
            videoTransition.setInterpolator(Interpolator.EASE_BOTH);
            videoTransition.setNode(mediaPane);

            TranslateTransition formTransition = new TranslateTransition();
            formTransition.setDuration(Duration.seconds(1));
            formTransition.setByX(mediaPane.getScene().getWidth() - leftSection.getLayoutX() - leftSection.getLayoutBounds().getWidth());
            formTransition.setInterpolator(Interpolator.EASE_BOTH);
            formTransition.setNode(leftSection);

            FadeTransition fadeoutTransition = new FadeTransition();
            fadeoutTransition.setDuration(Duration.seconds(0.3));
            fadeoutTransition.setFromValue(1);
            fadeoutTransition.setToValue(0);
            fadeoutTransition.setInterpolator(Interpolator.EASE_BOTH);
            fadeoutTransition.setNode(signupPane);

            FadeTransition fadeinTransition = new FadeTransition();
            fadeinTransition.setDuration(Duration.seconds(0.3));
            fadeinTransition.setFromValue(0);
            fadeinTransition.setToValue(1);
            fadeinTransition.setInterpolator(Interpolator.EASE_BOTH);
            fadeinTransition.setNode(loginPane);

            fadeoutTransition.setOnFinished(e -> {
                signupPane.setManaged(false);
                signupPane.setVisible(false);
                loginPane.setManaged(true);
                loginPane.setVisible(true);
                fadeinTransition.play();
            });

            videoTransition.play();
            formTransition.play();
            fadeoutTransition.play();

            isLogin = !isLogin;
        }
    }

    public void switchToSignup(ActionEvent event){
        if (isLogin){
            TranslateTransition videoTransition = new TranslateTransition();
            videoTransition.setDuration(Duration.seconds(1));
            videoTransition.setByX(-(leftSection.getLayoutX() - mediaPane.getLayoutX()));
            videoTransition.setInterpolator(Interpolator.EASE_BOTH);
            videoTransition.setNode(mediaPane);

            TranslateTransition formTransition = new TranslateTransition();
            formTransition.setDuration(Duration.seconds(1));
            formTransition.setByX(-(mediaPane.getScene().getWidth() - leftSection.getLayoutX()  - leftSection.getLayoutBounds().getWidth()));
            formTransition.setInterpolator(Interpolator.EASE_BOTH);
            formTransition.setNode(leftSection);

            FadeTransition fadeoutTransition = new FadeTransition();
            fadeoutTransition.setDuration(Duration.seconds(0.3));
            fadeoutTransition.setFromValue(1);
            fadeoutTransition.setToValue(0);
            fadeoutTransition.setInterpolator(Interpolator.EASE_BOTH);
            fadeoutTransition.setNode(loginPane);

            FadeTransition fadeinTransition = new FadeTransition();
            fadeinTransition.setDuration(Duration.seconds(0.3));
            fadeinTransition.setFromValue(0);
            fadeinTransition.setToValue(1);
            fadeinTransition.setInterpolator(Interpolator.EASE_BOTH);
            fadeinTransition.setNode(signupPane);

            fadeoutTransition.setOnFinished(e -> {
                loginPane.setManaged(false);
                loginPane.setVisible(false);
                signupPane.setManaged(true);
                signupPane.setVisible(true);
                fadeinTransition.play();
            });

            videoTransition.play();
            formTransition.play();
            fadeoutTransition.play();

            isLogin = !isLogin;
        }
    }
}
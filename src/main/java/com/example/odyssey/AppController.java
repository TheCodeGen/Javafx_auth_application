package com.example.odyssey;

import javafx.animation.*;
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
import javafx.util.Duration;
import org.mindrot.jbcrypt.BCrypt;

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
    private Label password_error_message;

    @FXML
    private PrintWriter pw;

    @FXML
    private Hyperlink loginToggle;

    @FXML
    private VBox signupPane;

    @FXML
    private VBox loginPane;

    @FXML
    private StackPane leftSection;

    @FXML
    private TextField loginUserField;

    @FXML
    private PasswordField loginPasswordField;

    private boolean isLogin;

    private UserService userService;

    @Override
    public void initialize (URL url, ResourceBundle resourceBundle)  {
        hidePasswordErrorMessage();

        userService = new UserService();

        loginPane.setVisible(false);
        loginPane.setManaged(false);

        String path = "https://videos.pexels.com/video-files/11340265/11340265-hd_1080_1920_60fps.mp4";
        Media media = new Media(path);
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

    public void hidePasswordErrorMessage(){
        password_error_message.setVisible(false);
        password_error_message.setManaged(false);
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
    public void createAccount(ActionEvent event) throws IOException {
        boolean isNameComplete = validateField(nameField);
        boolean isEmailComplete = validateField(emailField);
        boolean isPasswordComplete = validateField(passwordField);

        if(isNameComplete && isEmailComplete && isPasswordComplete){
            String name = nameField.getText().trim().toLowerCase();
            String email = emailField.getText().trim().toLowerCase();
            String password = passwordField.getText().trim().toLowerCase();

            removePassWordErrorMessage();
            String encryptedPassword = encrypt(password);
            System.out.println("adding user");
            userService.addUser(name, email, encryptedPassword);
        }

        nameField.clear();
        emailField.clear();
        passwordField.clear();
        switchtoLogin(new ActionEvent());
    }

    private void showPassWordErrorMessage() {
        password_error_message.setVisible(true);
        password_error_message.setManaged(true);
    }

    private void removePassWordErrorMessage() {
        password_error_message.setVisible(false);
        password_error_message.setManaged(false);
    }

    private String encrypt(String message){
        return BCrypt.hashpw(message, BCrypt.gensalt());
    }

    private Boolean validateField(TextField field){
        boolean result = true;
        if (field.getText().trim().isEmpty()){
            field.getStyleClass().add("text-field-error");
            result = false;
        }else{
            field.getStyleClass().remove("text-field-error");
        }
        return result;
    }

    public void switchtoLogin(ActionEvent event){
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

    public void switchtoSignup(ActionEvent event){
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

    public void loginUser(ActionEvent event) throws IOException {
        String userInfo = loginUserField.getText().toLowerCase();
        String password = loginPasswordField.getText().toLowerCase();
        if (userService.verifyUser(userInfo, password)){
            new Alert(Alert.AlertType.INFORMATION, "Login Successful").show();
        }else{
            new Alert(Alert.AlertType.ERROR, "Login Unsuccessful").show();
        }
    }
}
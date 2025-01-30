package com.example.odyssey;

import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.event.Event;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Cursor;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.scene.control.RadioButton;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.media.MediaView;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.scene.shape.Rectangle;
import javafx.scene.web.WebView;
import javafx.scene.web.WebEngine;
import javafx.stage.Stage;
import org.w3c.dom.Text;

import java.io.IOException;
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

    @FXML
    private WebView webView;

    @FXML
    private TextField nameField;

    @FXML
    private TextField emailField;

    @FXML
    private PasswordField passwordField;


    private Stage stage;

    private Scene currentScene;

    private final String[] urls = {
            "https://www.icloud.com/",
            "https://accounts.google.com/v3/signin/identifier?continue=https%3A%2F%2Faccounts.google.com%2F&followup=https%3A%2F%2Faccounts.google.com%2F&ifkv=AeZLP989ZgpMl-PYm6nx9J3p6FEJHgLfruptoEE5DkJWJw2GbLhLf6lfgVHq4qnMGbLUpKs1OCX_&passive=1209600&flowName=GlifWebSignIn&flowEntry=ServiceLogin&dsh=S-79867073%3A1735928021022193&ddm=1",
            "https://www.facebook.com/login/"
    };

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
        String[] icons = {"/apple-logo.png", "/google.png", "/facebook.png"};
        Button appleButton = createSocialButton(icons[0], this::openAppleSignIn);
        Button googleButton = createSocialButton(icons[1], this::openGoogleSignIn);
        Button facebookButton = createSocialButton(icons[2], this::openFacebookSignIn);

        socialButtonsBox.getChildren().addAll(appleButton, googleButton, facebookButton);
    }

    private Button createSocialButton(String iconPath, Runnable action){
        ImageView icon = new ImageView(new Image(getClass().getResourceAsStream(iconPath)));
        icon.setFitWidth(20);
        icon.setFitHeight(20);

        Button button = new Button();
        button.setCursor(Cursor.HAND);
        button.setGraphic(icon);
        button.getStyleClass().add("social-button");
        button.setOnAction(e -> action.run());
        return button;
    }

    private void openWebView(String url){
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/example/odyssey/webview-scene.fxml"));
            Scene webViewScene = new Scene(loader.load(), 1060, 670);

            WebViewController controller = loader.getController();
            controller.setStage(stage);
            controller.setOnBackAction(() -> stage.setScene(currentScene));
            controller.loadPage(url);

            stage.setScene(webViewScene);
        }catch (IOException e){
            e.printStackTrace();
        }
    }

    private void openAppleSignIn(){
        openSocialSignIn("https://account.apple.com/sign-in");
    }

    private void openGoogleSignIn(){
        openSocialSignIn("https://accounts.google.com/v3/signin/identifier?continue=https%3A%2F%2Faccounts.google.com%2F&followup=https%3A%2F%2Faccounts.google.com%2F&ifkv=AeZLP989ZgpMl-PYm6nx9J3p6FEJHgLfruptoEE5DkJWJw2GbLhLf6lfgVHq4qnMGbLUpKs1OCX_&passive=1209600&flowName=GlifWebSignIn&flowEntry=ServiceLogin&dsh=S-79867073%3A1735928021022193&ddm=1");
    }

    private void openFacebookSignIn(){
        openSocialSignIn("https://www.facebook.com/login/");
    }

    private void openSocialSignIn(String url){
        openWebView(url);
    }

    public void setScene(Scene scene){
        this.currentScene = scene;
    }

    public void setStage(Stage stage){
        this.stage = stage;
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
    public void createAccount(ActionEvent event){
        validateField(nameField);
        validateField(emailField);
        validateField(passwordField);
    }

    private void validateField(TextField field){
        if (field.getText().trim().isEmpty() && !field.getStyleClass().contains("text-field-error")){
            field.getStyleClass().add("text-field-error");
        }else{
            field.getStyleClass().remove("text-field-error");
        }
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

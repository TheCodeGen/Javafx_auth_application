package com.example.odyssey;

import javafx.animation.Interpolator;
import javafx.animation.TranslateTransition;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.event.Event;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Cursor;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.scene.media.MediaView;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.scene.shape.Rectangle;
import javafx.stage.Stage;
import java.io.*;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.util.Duration;
import org.mindrot.jbcrypt.BCrypt;

public class HelloController implements Initializable {

    @FXML
    private Pane mediaPane;

    @FXML
    private HBox socialButtonsBox;

    @FXML
    private RadioButton darkModeToggle;

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
    private VBox left_Section;

    private Stage stage;

    private boolean isLogin;

    private Scene currentScene;

    private final String[] urls = {
            "https://www.icloud.com/",
            "https://accounts.google.com/v3/signin/identifier?continue=https%3A%2F%2Faccounts.google.com%2F&followup=https%3A%2F%2Faccounts.google.com%2F&ifkv=AeZLP989ZgpMl-PYm6nx9J3p6FEJHgLfruptoEE5DkJWJw2GbLhLf6lfgVHq4qnMGbLUpKs1OCX_&passive=1209600&flowName=GlifWebSignIn&flowEntry=ServiceLogin&dsh=S-79867073%3A1735928021022193&ddm=1",
            "https://www.facebook.com/login/"
    };

    @Override
    public void initialize (URL url, ResourceBundle resourceBundle)  {
        addSocialButtons();
        password_error_message.setVisible(false);
        password_error_message.setManaged(false);

        FileWriter filewriter = null;
        try {
            filewriter = new FileWriter("Database.txt", true);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        pw = new PrintWriter(filewriter);

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
        Button appleButton = createSocialButton(icons[0]);
        Button googleButton = createSocialButton(icons[1]);
        Button facebookButton = createSocialButton(icons[2]);

        socialButtonsBox.getChildren().addAll(appleButton, googleButton, facebookButton);
    }

    private Button createSocialButton(String iconPath){
        ImageView icon = new ImageView(new Image(getClass().getResourceAsStream(iconPath)));
        icon.setFitWidth(20);
        icon.setFitHeight(20);

        Button button = new Button();
        button.setCursor(Cursor.HAND);
        button.setGraphic(icon);
        button.getStyleClass().add("social-button");
        return button;
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
        boolean isNameComplete = validateField(nameField);
        boolean isEmailComplete = validateField(emailField);
        boolean isPasswordComplete = validateField(passwordField);

        if(isNameComplete && isEmailComplete && isPasswordComplete){
            String name = nameField.getText().trim();
            String email = emailField.getText().trim();
            String password = passwordField.getText().trim();
            if (password.length() < 5 && !password.isEmpty()){
                password_error_message.setVisible(true);
                password_error_message.setManaged(true);
            }else if (!name.isEmpty() && !email.isEmpty() && !password.isEmpty()){
                password_error_message.setVisible(false);
                password_error_message.setManaged(false);
                String encryptedPassword = encrypt(password);
                pw.printf("%s,%s,%s%n", name, email, encryptedPassword);
                pw.flush();
            }
        }
        nameField.clear();
        emailField.clear();
        passwordField.clear();
    }

    private String encrypt(String message){
        return BCrypt.hashpw(message, BCrypt.gensalt());
    }

    private Boolean validateField(TextField field){
        boolean result = true;
        System.out.println(field.getStyleClass());
        if (field.getText().trim().isEmpty() || !field.getStyleClass().contains("text-field-error")){
            field.getStyleClass().add("text-field-error");
            result = false;
        }else{
            field.getStyleClass().remove("text-field-error");
        }
        return result;
    }

    @FXML
    public void setDarkMode(ActionEvent event){
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

    public void switchtoLogin(ActionEvent event){
        System.out.println(mediaPane.getLayoutX() + ", " + left_Section.getLayoutX());
        TranslateTransition videoTransition = new TranslateTransition();
        videoTransition.setDuration(Duration.seconds(1));
        videoTransition.setByX(left_Section.getLayoutX() - mediaPane.getLayoutX());

        videoTransition.setInterpolator(Interpolator.EASE_BOTH);
        videoTransition.setNode(mediaPane);
        videoTransition.play();

        TranslateTransition formTransition = new TranslateTransition();
        formTransition.setDuration(Duration.seconds(1));
        formTransition.setByX(currentScene.getWidth() - left_Section.getLayoutX() - left_Section.getLayoutBounds().getWidth());
        formTransition.setInterpolator(Interpolator.EASE_BOTH);
        formTransition.setNode(left_Section);
        formTransition.play();
        System.out.println(mediaPane.getLayoutX() + ", " + left_Section.getLayoutX());
        new Thread(this::updateToLogin).start();

        System.out.println(rootPane.getChildren());
    }

    private void updateToLogin(){
        String videoPath = getClass().getResource("/video3.mp4").toExternalForm();
        Media media = new Media(videoPath);
        MediaPlayer mediaPlayer = new MediaPlayer(media);

        mediaPlayer.setAutoPlay(true);
        mediaPlayer.setCycleCount(MediaPlayer.INDEFINITE);

        Platform.runLater(() -> {
            MediaView mediaView = (MediaView) mediaPane.getChildren().get(0);
            mediaView.setMediaPlayer(mediaPlayer);
            mediaPlayer.play();
        });
    }
}
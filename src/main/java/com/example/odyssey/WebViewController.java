package com.example.odyssey;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.web.WebView;
import javafx.stage.Stage;

public class WebViewController {

    @FXML
    private WebView webView;

    @FXML
    private Button backButton;

    private Stage stage;
    private Runnable onBackAction;

    public void loadPage(String url) {
        webView.getEngine().load(url);
    }

    public void setStage(Stage stage) {
        this.stage = stage;
    }

    public void setOnBackAction(Runnable onBackAction) {
        this.onBackAction = onBackAction;
    }

    @FXML
    private void goBack() {
        if (onBackAction != null) {
            onBackAction.run();
        }
    }
}

module com.example.odyssey {
    requires javafx.controls;
    requires javafx.fxml;
    requires javafx.media;
    requires javafx.web;
    requires jbcrypt;
    requires java.desktop;
    opens com.example.odyssey to javafx.fxml;
    exports com.example.odyssey;
}
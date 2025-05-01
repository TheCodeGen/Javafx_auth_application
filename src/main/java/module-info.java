module com.example.odyssey {
    requires javafx.controls;
    requires javafx.fxml;
    requires javafx.media;
    requires javafx.web;
    requires jbcrypt;
    requires java.desktop;
    requires com.fasterxml.jackson.databind;
    requires java.sql;
    requires io.github.cdimascio.dotenv.java;
    requires java.net.http;
    requires static lombok;
    opens com.example.odyssey to javafx.fxml;
    exports com.example.odyssey;
    exports com.example.odyssey.controller;
    opens com.example.odyssey.controller to javafx.fxml;
    exports com.example.odyssey.model;
    opens com.example.odyssey.model to javafx.fxml;
}
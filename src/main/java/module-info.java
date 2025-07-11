module br.com.arthivia.notifyapp {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.sql;
    requires jdk.jshell;
    requires java.desktop;
    requires com.dustinredmond.fxtrayicon;
    requires javafx.media;

    opens br.com.arthivia.notifyapp to javafx.fxml;
    opens br.com.arthivia.notifyapp.model to javafx.base;

    exports br.com.arthivia.notifyapp;
    exports br.com.arthivia.notifyapp.controllers;
    opens br.com.arthivia.notifyapp.controllers to javafx.fxml;
}
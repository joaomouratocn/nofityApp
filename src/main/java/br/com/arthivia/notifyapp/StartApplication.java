package br.com.arthivia.notifyapp;

import br.com.arthivia.notifyapp.database.DAO;
import br.com.arthivia.notifyapp.util.Util;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

public class StartApplication extends Application {

    @Override
    public void start(Stage stage) throws IOException {
        DAO dao = new DAO();

        FXMLLoader loader = new FXMLLoader(StartApplication.class.getResource("views/home-view.fxml"));
        Scene scene = new Scene(loader.load(), 320, 240);
        stage.setMinWidth(700);
        stage.setMinHeight(500);
        stage.setResizable(false);
        stage.setScene(scene);
        stage.show();

        Util.startNotificationService();
    }

    public static void main(String[] args) {
        launch(args);
    }
}

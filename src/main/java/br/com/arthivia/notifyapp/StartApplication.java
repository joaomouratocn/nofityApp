package br.com.arthivia.notifyapp;

import br.com.arthivia.notifyapp.service.NotificationService;
import br.com.arthivia.notifyapp.util.Util;
import com.dustinredmond.fxtrayicon.FXTrayIcon;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.Objects;

public class StartApplication extends Application {

    @Override
    public void start(Stage stage) throws IOException {
        FXMLLoader loader = new FXMLLoader(StartApplication.class.getResource("views/home-view.fxml"));
        Scene scene = new Scene(loader.load(), 320, 240);
        stage.setMinWidth(700);
        stage.setMinHeight(500);
        stage.setResizable(false);
        stage.setScene(scene);
        configureTray(stage);
        stage.show();

        NotificationService notificationService = new NotificationService();
        notificationService.startNotificationService();
    }

    private void configureTray(Stage stage) {
        FXTrayIcon trayIcon = new FXTrayIcon(stage, Objects.requireNonNull(getClass().getResource("/br/com/arthivia/notifyapp/images/reminder.png")));
        trayIcon.show();

        trayIcon.addMenuItem("Restaurar", actionEvent -> {
            stage.setIconified(false);
            stage.show();
            stage.toFront();

        });
        trayIcon.addMenuItem("Sair", actionEvent -> System.exit(0));

        stage.setOnCloseRequest(windowEvent -> {
            windowEvent.consume();
            stage.hide();
        });
    }

    public static void main(String[] args) {
        launch(args);
    }
}

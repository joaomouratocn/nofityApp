package br.com.arthivia.notifyapp.util;

import br.com.arthivia.notifyapp.StartApplication;
import br.com.arthivia.notifyapp.controllers.InsertUpdateController;
import br.com.arthivia.notifyapp.database.DAO;
import br.com.arthivia.notifyapp.model.NotificationDao;
import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

import java.io.IOException;
import java.time.DayOfWeek;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import static java.time.DayOfWeek.*;

public class Util {

    public static List<DayOfWeek> convertDayWeek(List<String> dayWeek) {
        var outputList = new ArrayList<DayOfWeek>();
        for (String day : dayWeek) {
            var value = switch (day) {
                case "Seg." -> MONDAY;
                case "Ter." -> TUESDAY;
                case "Qua." -> WEDNESDAY;
                case "Qui." -> THURSDAY;
                case "Sex." -> FRIDAY;
                case "Sab." -> SATURDAY;
                case "Dom." -> SUNDAY;
                default -> throw new IllegalStateException("Unexpected value: " + day);
            };
            outputList.add(value);
        }
        return outputList;
    }

    public static void openInsertOrUpdateNotification(String typeAction, NotificationDao notificationDao) {
        try {
            FXMLLoader loader = new FXMLLoader(Util.class.getResource("/br/com/arthivia/notifyapp/views/insert-update-view.fxml"));
            Parent root = loader.load();

            if (notificationDao != null) {
                InsertUpdateController insertUpdateController = loader.getController();
                insertUpdateController.loadNotification(notificationDao);
            }

            Stage stage = new Stage();
            stage.setTitle(typeAction);
            stage.setScene(new Scene(root));
            stage.setResizable(false);
            stage.initModality(Modality.APPLICATION_MODAL);
            stage.showAndWait();

        } catch (IOException e) {
            LogApp.getInstance().logError("Erro ao tentar executar esta operação: " + e.getMessage());
        }
    }

    public static void openNotificationScreen(NotificationDao notificationDao) {
        Platform.runLater(() -> {
            try {
                Stage notificationStage = new Stage();
                notificationStage.initStyle(StageStyle.UNDECORATED);
                FXMLLoader loader = new FXMLLoader(StartApplication.class.getResource("/br/com/arthivia/notifyapp/views/notification-view.fxml"));
                Scene newScene = new Scene(loader.load());
                notificationStage.setScene(newScene);
                notificationStage.setResizable(false);
                notificationStage.show();
                notificationStage.toFront();
            } catch (IOException e) {
                System.out.println(e.getMessage());
                LogApp.getInstance().logError(e.getMessage());
            }
        });
    }
}

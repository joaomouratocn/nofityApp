package br.com.arthivia.notifyapp.controllers;

import br.com.arthivia.notifyapp.database.DAO;
import br.com.arthivia.notifyapp.model.NotificationDao;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.image.ImageView;
import javafx.scene.media.AudioClip;
import javafx.stage.Stage;

import java.util.Objects;

public class NotificationController {
    @FXML
    public Label lbNotification;
    @FXML
    public TextArea taMessage;
    @FXML
    public Button btnConfirm;

    private final DAO dao = DAO.getInstance();
    private NotificationDao received;

    @FXML
    private void initialize() {
        String soundPath = Objects.requireNonNull(getClass().getResource("/br/com/arthivia/notifyapp/sound/new-notification.mp3")).toExternalForm();
        AudioClip sound = new AudioClip(soundPath);
        sound.setVolume(0.7);
        sound.setCycleCount(AudioClip.INDEFINITE);
        sound.play();

        btnConfirm.setOnAction(e -> {
            dao.setNotified(received.getId());
            sound.stop();
            Stage stage = (Stage) btnConfirm.getScene().getWindow();
            stage.close();
        });
    }

    public void setNotification(NotificationDao notificationDao) {
        received = notificationDao;
        lbNotification.setText(notificationDao.getTitle());
        taMessage.setText(notificationDao.getMessage());
    }
}

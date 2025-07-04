package br.com.arthivia.notifyapp.controllers;

import br.com.arthivia.notifyapp.database.DAO;
import br.com.arthivia.notifyapp.model.NotificationDao;
import br.com.arthivia.notifyapp.util.Util;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.stage.Stage;

import java.time.DayOfWeek;
import java.util.Arrays;
import java.util.List;

import static java.time.DayOfWeek.*;

public class InsertUpdateController {
    @FXML
    private TextField txtHour;
    @FXML
    private Label lbErrorHour;
    @FXML
    private CheckBox ckbEnable;
    @FXML
    private CheckBox ckbDom;
    @FXML
    private CheckBox ckbSeg;
    @FXML
    private CheckBox ckbTer;
    @FXML
    private CheckBox ckbQua;
    @FXML
    private CheckBox ckbQui;
    @FXML
    private CheckBox ckbSex;
    @FXML
    private CheckBox ckbSab;
    @FXML
    private Label lbErrorDays;
    @FXML
    private TextField txtTitle;
    @FXML
    private Label lbErrorTitle;
    @FXML
    private TextArea textMessage;
    @FXML
    private Button btnSave;
    @FXML
    private Button btnClose;

    private DAO dao;

    private int notificationId = 0;

    private List<CheckBox> checkBoxes;

    @FXML
    private void initialize() {
        dao = new DAO();
        configureLabels();
        configureTxtHour();
        configureTxtTitle();
        configureButtonSave();
        configureButtonClose();
        checkBoxes = Arrays.asList(ckbDom, ckbSeg, ckbTer, ckbQua, ckbQui, ckbSex, ckbSab);
    }

    private void configureTxtTitle() {
        txtTitle.focusedProperty().addListener((observableValue, wasFocus, focused) -> {
            if (wasFocus) {
                lbErrorTitle.setVisible(titleIsBlank());
            }
        });
    }

    private void configureButtonClose() {
        btnClose.setOnMouseClicked(mouseEvent -> {
            close();
        });
    }

    private void close() {
        Stage stage = (Stage) btnClose.getScene().getWindow();
        stage.close();
    }

    private void configureButtonSave() {
        btnSave.setOnMouseClicked(mouseEvent -> {
            saveNotification();
        });
    }

    private void configureLabels() {
        lbErrorHour.setVisible(false);
        lbErrorDays.setVisible(false);
        lbErrorTitle.setVisible(false);
    }

    private void configureTxtHour() {
        txtHour.setTextFormatter(new TextFormatter<>(change -> {
            String newText = change.getControlNewText();

            if (newText.matches("^(\\d{0,2})(:)?(\\d{0,2})?$")) {
                return change;
            }
            return null;
        }));

        txtHour.focusedProperty().addListener((observableValue, wasFocus, focused) -> {
            if (wasFocus) {
                lbErrorHour.setVisible(hourIsValid());
            }
        });
    }

    private void saveNotification() {
        var result = validateField();
        if (result) {
            var dayWeeks = loadDayWeek();

            var notificationDao = new NotificationDao(
                    notificationId,
                    txtTitle.getText(),
                    textMessage.getText(),
                    dayWeeks,
                    txtHour.getText(),
                    ckbEnable.isSelected() ? 1 : 0,
                    0);

            if (notificationId == 0) {
                boolean insertResult = dao.insertNotification(notificationDao);
                if(!insertResult){
                    System.out.println("Inserido com sucesso!");
                }else {
                    System.out.println("Erro ao inserir, ver LOGS");
                }
            } else {
                boolean updateResult = dao.updateNotification(notificationDao);
                if (updateResult){
                    System.out.println("Atualizado com sucesso!");
                }else{
                    System.out.println("Erro ao atualizar, ver LOGS");
                }
            }
            close();
        }
    }

    private List<DayOfWeek> loadDayWeek() {
        List<String> list = checkBoxes.stream().filter(CheckBox::isSelected).map(CheckBox::getText).toList();
        return Util.convertDayWeek(list);
    }

    public void loadNotification(NotificationDao notificationDao) {
        notificationId = notificationDao.getId();
        txtHour.setText(notificationDao.getHour());
        ckbEnable.setSelected(notificationDao.getEnable() == 1);
        ckbDom.setSelected(notificationDao.getDayWeek().contains(MONDAY));
        ckbSeg.setSelected(notificationDao.getDayWeek().contains(SUNDAY));
        ckbTer.setSelected(notificationDao.getDayWeek().contains(TUESDAY));
        ckbQua.setSelected(notificationDao.getDayWeek().contains(WEDNESDAY));
        ckbQui.setSelected(notificationDao.getDayWeek().contains(THURSDAY));
        ckbSex.setSelected(notificationDao.getDayWeek().contains(FRIDAY));
        ckbSab.setSelected(notificationDao.getDayWeek().contains(SATURDAY));
        txtTitle.setText(notificationDao.getTitle());
        textMessage.setText(notificationDao.getMessage());
    }

    private boolean validateField() {
        if (hourIsValid()) {
            System.out.println("Hora invalída");
            return false;
        } else if (checkBoxes.stream().noneMatch(CheckBox::isSelected)) {
            lbErrorDays.setVisible(true);
            return false;
        } else if (titleIsBlank()) {
            lbErrorTitle.setVisible(true);
            return false;
        }
        return true;
    }

    private boolean hourIsValid() {
        return !txtHour.getText().matches("^(0\\d|1\\d|2[0-3]):([0-5]\\d|60)$");
    }

    private boolean titleIsBlank() {
        return txtTitle.getText().isBlank();
    }
}

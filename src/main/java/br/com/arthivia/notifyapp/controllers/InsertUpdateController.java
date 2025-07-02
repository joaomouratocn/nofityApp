package br.com.arthivia.notifyapp.controllers;

import br.com.arthivia.notifyapp.model.NotificationDao;
import javafx.fxml.FXML;
import javafx.scene.control.*;

import java.time.DayOfWeek;

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
    private void initialize() {
        lbErrorHour.setVisible(false);
        lbErrorDays.setVisible(false);
        lbErrorTitle.setVisible(false);

        txtHour.setTextFormatter(new TextFormatter<>(change -> {
            String newText = change.getControlNewText();

            if (newText.matches("^(\\d{0,2})(:)?(\\d{0,2})?$")) {
                return change;
            }
            return null;
        }));

        txtHour.focusedProperty().addListener((observableValue, wasFocus, focused) -> {
           if (wasFocus){
               System.out.println(hourIsOk() + "isok");
               lbErrorHour.setVisible(hourIsOk());
           }
        });
    }

    public void insertNotification() {}

    public void loadNotification(NotificationDao notificationDao){
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

    private boolean validateField(){

    }

    public boolean hourIsOk(){
        return !txtHour.getText().matches("^(0\\d|1\\d|2[0-3]):([0-5]\\d|60)$");
    }
}

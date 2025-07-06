package br.com.arthivia.notifyapp.controllers;

import br.com.arthivia.notifyapp.database.DAO;
import br.com.arthivia.notifyapp.model.NotificationDao;
import br.com.arthivia.notifyapp.util.LogApp;
import br.com.arthivia.notifyapp.util.Util;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.io.IOException;
import java.time.DayOfWeek;
import java.time.format.TextStyle;
import java.util.List;
import java.util.Locale;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

import static br.com.arthivia.notifyapp.util.Util.openInsertOrUpdateNotification;

public class HomeController {
    @FXML
    private TableView<NotificationDao> tableView;
    @FXML
    private TableColumn<NotificationDao, Integer> colEnable;
    @FXML
    private TableColumn<NotificationDao, String> colDayWeek;
    @FXML
    private TableColumn<NotificationDao, String> colHour;
    @FXML
    private TableColumn<NotificationDao, String> colTitle;
    @FXML
    private TableColumn<NotificationDao, String> colMessage;
    @FXML
    private Button btnInsert;
    @FXML
    private Button btnUpdate;
    @FXML
    private Button btnDelete;

    private DAO dao;

    @FXML
    private void initialize() {
        dao = DAO.getInstance();
        configureTable();
        configureBtnInsert();
        configureBtnUpdate();
        configureBtnDelete();
    }

    private void configureTable() {
        colHour.setCellValueFactory(new PropertyValueFactory<>("hour"));
        colTitle.setCellValueFactory(new PropertyValueFactory<>("title"));
        colMessage.setCellValueFactory(new PropertyValueFactory<>("message"));
        colEnable.setCellValueFactory(new PropertyValueFactory<>("enable"));
        colDayWeek.setCellValueFactory(cellData -> {
            List<DayOfWeek> days = cellData.getValue().getDayWeek();
            String formatedDays = days.stream()
                    .map(d -> d.getDisplayName(TextStyle.SHORT, new Locale("pt", "BR")))
                    .collect(Collectors.joining(" "));
            return new SimpleStringProperty(formatedDays);
        });

        colEnable.setCellFactory(column -> new TableCell<>() {
            private final ImageView imageView = new ImageView();

            @Override
            protected void updateItem(Integer enable, boolean empty) {
                super.updateItem(enable, empty);

                if (empty || enable == null) {
                    setGraphic(null);
                } else {
                    String path = (enable == 1) ? "/br/com/arthivia/notifyapp/images/ball-green.png" : "/br/com/arthivia/notifyapp/images/ball-red.png";
                    imageView.setImage(new Image(Objects.requireNonNull(getClass().getResourceAsStream(path))));
                    imageView.setFitWidth(16);
                    imageView.setFitHeight(16);
                    setGraphic(imageView);
                }
            }
        });

        tableView.setRowFactory(notificationDaoTableView -> {
            TableRow<NotificationDao> row = new TableRow<>();
            row.setOnMouseClicked(mouseEvent -> {
                if (mouseEvent.getClickCount() == 2 && !row.isEmpty()) {
                    NotificationDao item = row.getItem();
                    openInsertOrUpdateNotification("Alterar", item);
                    loadData();
                }
            });
            return row;
        });

        loadData();
    }

    private void loadData() {
        ObservableList<NotificationDao> data = FXCollections.observableArrayList(dao.getAllNotification());
        tableView.setItems(data);
    }

    private void configureBtnInsert() {
        btnInsert.setOnMouseClicked(mouseEvent -> {
            openInsertOrUpdateNotification("Cadastrar", null);
            loadData();
        });
    }

    private void configureBtnUpdate() {
        btnUpdate.setOnMouseClicked(mouseEvent -> {
            var notificationTable = getSelectedItem();
            if (notificationTable == null) {
                return;
            }
            openInsertOrUpdateNotification("Alterar", notificationTable);
            loadData();
        });
    }

    private void configureBtnDelete() {
        btnDelete.setOnMouseClicked(mouseEvent -> {
            var notificationDao = getSelectedItem();
            if (notificationDao == null) {
                return;
            }
            deleteNotification(notificationDao);
        });
    }

    private NotificationDao getSelectedItem() {
        if (tableView.getSelectionModel().getSelectedItem() == null) {
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setContentText("Nenhuma notificação selecionada!");
            alert.showAndWait();
            return null;
        }

        return tableView.getSelectionModel().getSelectedItem();
    }

    private void deleteNotification(NotificationDao notificationDao) {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Confirmação");
        alert.setHeaderText("Excluir notificação");
        alert.setContentText("Tem certeza que deseja excluir a notificação " + notificationDao.getTitle() + "?");

        Optional<ButtonType> result = alert.showAndWait();
        if (result.isPresent() && result.get() == ButtonType.OK) {

            String msg = dao.deleteNotification(notificationDao.getId());

            if (Objects.equals(msg, "Dado deletado com sucesso!")) {
                tableView.getItems().remove(notificationDao);
            }
        }
    }
}
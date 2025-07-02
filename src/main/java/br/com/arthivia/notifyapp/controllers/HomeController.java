package br.com.arthivia.notifyapp.controllers;

import br.com.arthivia.notifyapp.database.DAO;
import br.com.arthivia.notifyapp.model.NotificationDao;
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
import java.util.List;
import java.util.Objects;
import java.util.Optional;

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

    DAO dao = new DAO();

    @FXML
    private void initialize() {
        mock();
        configureTable();
        configureBtnInsert();
        configureBtnUpdate();
        configureBtnDelete();
    }

    private void configureTable() {
        colDayWeek.setCellValueFactory(new PropertyValueFactory<>("dayWeek"));
        colHour.setCellValueFactory(new PropertyValueFactory<>("hour"));
        colTitle.setCellValueFactory(new PropertyValueFactory<>("title"));
        colMessage.setCellValueFactory(new PropertyValueFactory<>("message"));
        colEnable.setCellValueFactory(new PropertyValueFactory<>("enable"));

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

        ObservableList<NotificationDao> data = FXCollections.observableArrayList(dao.getAllNotification());
        tableView.setItems(data);
    }

    private void configureBtnInsert() {
        btnInsert.setOnMouseClicked(mouseEvent -> {
            openInsertOrUpdateNotification("Cadastrar", null);
        });
    }

    private void configureBtnUpdate() {
        btnUpdate.setOnMouseClicked(mouseEvent -> {
            var notificationTable = getSelectedItem();
            if (notificationTable == null) {
                return;
            }
            openInsertOrUpdateNotification("Alterar", notificationTable);
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


    private void openInsertOrUpdateNotification(String typeAction, NotificationDao notificationDao) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/br/com/arthivia/notifyapp/views/insert-update-view.fxml"));
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
            System.out.println(e.getMessage());
        }
    }

    private void mock(){
        dao.insertNotification(
                new NotificationDao(
                        0,
                        "Sistema",
                        "Reunião Sistema",
                        List.of(DayOfWeek.MONDAY, DayOfWeek.SATURDAY),
                        "13:00",
                        1,
                        0)
        );


//        dao.updateNotification(
//                new NotificationDao(
//                        1,
//                        "Sistema",
//                        "Verificar sistema",
//                        List.of(7),
//                        "13:00",
//                        1,
//                        0)
//        );
    }
}
package br.com.arthivia.notifyapp.controllers;

import br.com.arthivia.notifyapp.database.DAO;
import br.com.arthivia.notifyapp.model.NotificationDao;
import br.com.arthivia.notifyapp.model.NotificationTable;
import br.com.arthivia.notifyapp.util.Util;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

public class HomeController {
    @FXML
    private TableView<NotificationTable> tableView;
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

    DAO dao = new DAO();

    @FXML
    public void initialize() {
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

        dao.insertNotification(
                new NotificationDao(
                        0,
                        "Sistema",
                        "Reunião Sistema",
                        List.of(7, 3),
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

        List<NotificationTable> notificationTable = new ArrayList<>();
        List<NotificationDao> allNotification = dao.getAllNotification();
        for (NotificationDao notificationDao : allNotification) {
            Util.convertNotificationTable(notificationDao);
            notificationTable.add(Util.convertNotificationTable(notificationDao));
        }


        ObservableList<NotificationTable> data = FXCollections.observableArrayList(notificationTable);
        tableView.setItems(data);
    }

    @FXML
    public void deleteNotification() {
        NotificationTable notificationTable = tableView.getSelectionModel().getSelectedItem();

        if (notificationTable == null) {
            System.out.println("Nenhum item selecionado!");
            return;
        }

        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Confirmação");
        alert.setHeaderText("Excluir notificação");
        alert.setContentText("Tem certeza que deseja excluir a notificação " + notificationTable.getTitle() + "?");

        Optional<ButtonType> result = alert.showAndWait();
        if (result.isPresent() && result.get() == ButtonType.OK) {

            String rerult = dao.deleteNotification(notificationTable.getId());

            if (Objects.equals(rerult, "Dado deletado com sucesso!")) {
                tableView.getItems().remove(notificationTable);
            }
        }
    }
}
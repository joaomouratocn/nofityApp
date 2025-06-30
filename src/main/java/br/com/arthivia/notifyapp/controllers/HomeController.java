package br.com.arthivia.notifyapp.controllers;

import br.com.arthivia.notifyapp.model.NotificationDao;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

import java.util.Objects;

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
    public void initialize(){
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

        // Dados de exemplo
        ObservableList<NotificationDao> dados = FXCollections.observableArrayList(
                new NotificationDao(1, "Reunião", "Reunião com equipe", "SEGUNDA-FEIRA","10:00", 1, 0),
                new NotificationDao(2, "Alerta", "Verificar sistema", "TERÇA-FEIRA", "15:00", 0, 0)
        );
        tableView.setItems(dados);
    }
}
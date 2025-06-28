module br.com.arthivia.notifyapp {
    requires javafx.controls;
    requires javafx.fxml;


    opens br.com.arthivia.notifyapp to javafx.fxml;
    exports br.com.arthivia.notifyapp;
}
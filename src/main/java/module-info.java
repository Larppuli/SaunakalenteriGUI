module com.example.demo {
    requires javafx.controls;
    requires javafx.fxml;
    requires javafx.graphics;
    requires jbcrypt;


    opens com.example.demo to javafx.fxml;
    exports com.example.demo;
}
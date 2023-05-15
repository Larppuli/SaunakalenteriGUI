package com.example.demo;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import java.io.*;
import java.net.URL;
import java.util.Objects;
import java.util.ResourceBundle;

public class LisaysController implements Initializable {
    private Stage stage;
    @FXML private TextField sauna;
    @FXML private DatePicker paiva;
    @FXML private Label onnistuminen;


    @FXML
    protected void mainScene(ActionEvent event) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(Main.class.getResource("main-view.fxml"));
        Scene scene = new Scene(fxmlLoader.load(), 600, 400);
        stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        stage.setScene(scene);
        stage.show();
    }

    // Määritellään saunomiskerran tallennus
    @FXML
    public void tallennus(ActionEvent event) throws IOException, ClassNotFoundException {
        stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        if (!(sauna.getText()=="") && !Objects.isNull(paiva.getValue())) {
            Kayttaja.kirjoitaTiedostoon(sauna.getText(), paiva.getValue());
            onnistuminen.setTextFill(Color.GREEN);
            onnistuminen.setText("Saunomiskerta tallennettu");
            sauna.setText("");
            paiva.setValue(null);
        }
        else {
            onnistuminen.setTextFill(Color.RED);
            onnistuminen.setText("Täytä molemmat kentät");

        }

    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
    }
}

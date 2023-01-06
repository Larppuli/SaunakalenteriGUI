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

    @FXML
    public void tallennus(ActionEvent event) throws IOException, ClassNotFoundException {
        FXMLLoader fxmlLoader = new FXMLLoader(Main.class.getResource("lisays.fxml"));
        Scene scene = new Scene(fxmlLoader.load(), 600, 400);
        stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        stage.setScene(scene);
        if (!(sauna.getText()=="") && !Objects.isNull(paiva.getValue())) {
            System.out.println(sauna.getText());
            System.out.println(paiva.getValue());
            Saunomiskerta.kirjoitaTiedostoon(sauna.getText(), paiva.getValue());
        }
        onnistuminen.setText("Tallennus onnistui");
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
    }
}

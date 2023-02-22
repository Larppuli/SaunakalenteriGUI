package com.example.demo;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Objects;
import java.util.ResourceBundle;

public class SaunahakuController implements Initializable {
    private Stage stage;
    @FXML private ListView<String> myListView;
    @FXML private ChoiceBox<String> myChoiceBox;
    @FXML private TextField syote;
    @FXML private Label yhteensa;
    @FXML private Label yleisin;

    @FXML
    protected void etsintaScene(ActionEvent event) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(Main.class.getResource("etsinta.fxml"));
        Scene scene = new Scene(fxmlLoader.load(), 600, 400);
        stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        stage.setScene(scene);
        stage.show();
    }

    public void HaeSauna() throws IOException, ClassNotFoundException {
        ArrayList<String> saunalista = new ArrayList<>();
        ArrayList<String> saunalista2 = new ArrayList<>();
        if (!Objects.isNull(myChoiceBox.getValue())) {
            if (myChoiceBox.getValue().equals("Etsi saunan perusteella")) {
                for (Saunomiskerta kerta : Saunomiskerta.avaaLista()) {
                    if (kerta.getSauna().equalsIgnoreCase(syote.getText())) {
                        String tapahtuma = kerta.getSauna() + ", " + kerta.getPaiva();
                        saunalista.add(tapahtuma);
                    }
                }
                yleisin.setText("");
            }
            else if (myChoiceBox.getValue().equals("Etsi vuoden perusteella")) {
                for (Saunomiskerta kerta : Saunomiskerta.avaaLista()) {
                    String paiva = String.valueOf(kerta.getPaiva());
                    if (paiva.substring(0, 4).equals(syote.getText())) {
                        String tapahtuma = kerta.getSauna() + ", " + kerta.getPaiva();
                        saunalista.add(tapahtuma);
                        saunalista2.add(kerta.getSauna());
                    }
                }
                yleisin.setText("Yleisin sauna: " + Saunomiskerta.yleisinJasen(saunalista2));
            }
            myListView.getItems().clear();
            myListView.getItems().addAll(saunalista);
            yhteensa.setText(String.valueOf(saunalista.size()));
        }
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        myChoiceBox.getItems().addAll(new String[]{"Etsi saunan perusteella", "Etsi vuoden perusteella"});
    }
}
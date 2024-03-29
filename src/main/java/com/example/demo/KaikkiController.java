package com.example.demo;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;
import java.io.IOException;
import java.net.URL;
import java.util.Objects;
import java.util.ResourceBundle;

public class KaikkiController implements Initializable {
    private Stage stage;

    @FXML private ListView<String> myListView;
    @FXML private Label yhteensa;
    @FXML private Label yleisin;
    @FXML private String valittu;

    /*
    Määritellään valikko, josta voi valita etsiikö saunomiskertaa saunan tai vuoden
    perusteella vai etsiikö kaikki saunomiskerrat
     */
    @FXML
    protected void etsintaScene(ActionEvent event) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(Main.class.getResource("etsinta.fxml"));
        Scene scene = new Scene(fxmlLoader.load(), 600, 400);
        stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        stage.setScene(scene);
        stage.show();
    }

    @FXML
    public String handleMouseClick() {
        valittu = myListView.getSelectionModel().getSelectedItem();
        return valittu;
    }

    //Määritellään metodi, jolla valittu sauna poistetaan data.bin-tiedostosta
    @FXML
    public void saunanPoisto(ActionEvent event) throws IOException, ClassNotFoundException {
        if (!Objects.isNull(valittu)) {
            Saunomiskerta.poistaListasta(valittu);
            FXMLLoader fxmlLoader = new FXMLLoader(Main.class.getResource("kaikki.fxml"));
            Scene scene = new Scene(fxmlLoader.load(), 600, 400);
            stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            stage.setScene(scene);
        }
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        // Lisätään myListView-komponenttiin kaikki saunomiskerrat
        try {
            myListView.getItems().addAll(Saunomiskerta.luoTapahtumaLista());
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        }

        try {
            // Lasketaan kaikkien saunomiskertojen määrä ja lisätään luku labeliin "yhteensä"
            yhteensa.setText(String.valueOf(Saunomiskerta.avaaKayttajanLista().size()));
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }

        try {
            // Lasketaan yleisin sauna ja lasketaan sen jälkeen saunan esiintymiskerrat
            String yleisin_temp = Saunomiskerta.yleisinJasen(Saunomiskerta.luoSaunaLista());
            int kerrat = Saunomiskerta.saunomiskertojaSaunassa(yleisin_temp, Saunomiskerta.luoSaunaLista());

            // Lisätään labeliin "yleisin" yleisimmän saunan saunomiskerrat
            yleisin.setText(Saunomiskerta.kasaaYleisinString(yleisin_temp, kerrat));
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    }
}

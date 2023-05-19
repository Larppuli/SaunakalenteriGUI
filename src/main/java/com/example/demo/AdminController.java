package com.example.demo;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Objects;
import java.util.ResourceBundle;

public class AdminController implements Initializable {
    private Stage stage;
    @FXML private TextField kayttajatunnus;
    @FXML private TextField salasana1;
    @FXML private TextField salasana2;
    @FXML private Label onnistuminen;
    @FXML private ListView<String> kayttajalista;
    @FXML ObservableList<String> listViewData = FXCollections.observableArrayList();
    @FXML private String valittu;

    // Määritellään uuden käyttäjän lisäys
    @FXML
    protected void luoKayttaja() throws IOException, ClassNotFoundException {
        if (!(kayttajatunnus.getText()=="") && !(salasana1.getText()=="") && salasana1.getText().equals(salasana2.getText()) && Kayttaja.nimitarkistus(kayttajatunnus.getText())) {
            Kayttaja.lisaaKayttajaTiedostoon(kayttajatunnus.getText(), salasana1.getText(), new ArrayList<Saunomiskerta>());
            kayttajatunnus.setText("");
            salasana1.setText("");
            salasana2.setText("");
            onnistuminen.setTextFill(Color.GREEN);
            onnistuminen.setText("Käyttäjä lisätty");
        }
        else if (!Kayttaja.nimitarkistus(kayttajatunnus.getText())) {
            onnistuminen.setTextFill(Color.RED);
            onnistuminen.setText("Käyttäjätunnus on jo käytössä");
        }
        else if (kayttajatunnus.getText().isBlank() || salasana1.getText().isBlank() || salasana2.getText().isBlank()) {
            onnistuminen.setTextFill(Color.RED);
            onnistuminen.setText("Täytä kaikki kentät");
        }
        else {
            onnistuminen.setTextFill(Color.RED);
            onnistuminen.setText("Salasanat eivät täsmää");
        }
        listViewData.clear();
        Kayttaja.avaaLista().stream()
                .filter(kayttaja -> !kayttaja.getNimi().equalsIgnoreCase("admin"))
                .map(Kayttaja::getNimi)
                .forEach(listViewData::add);
    }

    // Määritellään uloskirjautuminen. Palataan kirjautumisvalikkoon ja pyyhitään istuntodata
    @FXML
    public void kirjauduUlos(ActionEvent event) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(Main.class.getResource("login.fxml"));
        Scene scene = new Scene(fxmlLoader.load(), 600, 400);
        stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        stage.setScene(scene);
        stage.show();
        Kayttaja.alustaIstunto();
    }

    @FXML
    public String handleMouseClick(MouseEvent arg0) {
        valittu = kayttajalista.getSelectionModel().getSelectedItem();
        return valittu;
    }

    @FXML
    public void kayttajanPoisto() throws IOException, ClassNotFoundException {
        ArrayList<Kayttaja> lista = new ArrayList<>(Kayttaja.avaaLista());
        lista.removeIf(kayttaja -> kayttaja.getNimi().equals(valittu));
        Kayttaja.korvaaTiedosto(lista);
        listViewData.clear();
        Kayttaja.avaaLista().stream()
                .filter(kayttaja -> !kayttaja.getNimi().equalsIgnoreCase("admin"))
                .map(Kayttaja::getNimi)
                .forEach(listViewData::add);
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        try {
            Kayttaja.avaaLista().stream()
                    .filter(kayttaja -> !kayttaja.getNimi().equalsIgnoreCase("admin"))
                    .map(Kayttaja::getNimi)
                    .forEach(listViewData::add);
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        kayttajalista.setItems(listViewData);
    }
}

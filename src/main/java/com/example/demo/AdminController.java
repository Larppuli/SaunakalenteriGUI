package com.example.demo;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;

public class AdminController implements Initializable {
    private Stage stage;
    @FXML private TextField kayttajatunnus;
    @FXML private TextField salasana1;
    @FXML private TextField salasana2;
    @FXML private Label onnistuminen;

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

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        
    }
}

package com.example.demo;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
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
    protected void luoKayttaja(ActionEvent event) throws IOException, ClassNotFoundException {
        ArrayList lista =  new ArrayList<Saunomiskerta>();
        FXMLLoader fxmlLoader = new FXMLLoader(Main.class.getResource("admin.fxml"));
        Scene scene = new Scene(fxmlLoader.load(), 600, 400);
        stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        if (!(kayttajatunnus.getText()=="") && !(salasana1.getText()=="") && salasana1.getText().equals(salasana2.getText()) && Kayttaja.nimitarkistus(kayttajatunnus.getText())) {
            Kayttaja.lisaaKayttajaTiedostoon(kayttajatunnus.getText(), salasana1.getText(), lista);
        }
        else  {
            onnistuminen.setText("Nimi on jo käytössä");
        }
        stage.setScene(scene);
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

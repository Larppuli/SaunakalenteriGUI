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
import java.util.ResourceBundle;

public class LoginController implements Initializable {
    private Stage stage;
    @FXML private TextField kayttajatunnus;
    @FXML private TextField salasana;
    @FXML private Label virhe;

    public void kirjautuminen(ActionEvent event) throws IOException, ClassNotFoundException {
        for (Kayttaja kayttaja : Kayttaja.avaaLista()) {
            if (kayttaja.getNimi().equalsIgnoreCase(kayttajatunnus.getText()) && !(kayttajatunnus.getText().equalsIgnoreCase("admin")) && kayttaja.getSalasana().equals(salasana.getText())) {
                Kayttaja.luoIstunto(kayttajatunnus.getText());
                FXMLLoader fxmlLoader = new FXMLLoader(Main.class.getResource("main-view.fxml"));
                Scene scene = new Scene(fxmlLoader.load(), 600, 400);
                stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
                stage.setScene(scene);
                stage.show();
            }
            else if (kayttaja.getNimi().equalsIgnoreCase(kayttajatunnus.getText()) && kayttaja.getSalasana().equals(salasana.getText())) {
                Kayttaja.luoIstunto(kayttajatunnus.getText());
                FXMLLoader fxmlLoader = new FXMLLoader(Main.class.getResource("admin.fxml"));
                Scene scene = new Scene(fxmlLoader.load(), 600, 400);
                stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
                stage.setScene(scene);
                stage.show();
            }
            else {
                virhe.setText("Väärä käyttäjätunnus tai salasana");
            }
        }
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
    }
}

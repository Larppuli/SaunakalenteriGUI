package com.example.demo;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

import java.io.IOException;

public class Main extends Application {
    @Override
    public void start(Stage stage) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(Main.class.getResource("login.fxml"));
        Scene scene1 = new Scene(fxmlLoader.load(), 600, 400);
        stage.setTitle("Saunakalenteri");
        stage.initStyle(StageStyle.DECORATED);
        stage.setOnCloseRequest(event -> {
            Kayttaja.alustaIstunto();
        });
        stage.setScene(scene1);
        stage.show();
    }


    public static void main(String[] args) {
        launch();
    }
}
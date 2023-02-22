package com.example.demo;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.chart.*;
import javafx.stage.Stage;
import java.io.IOException;
import java.net.URL;
import java.util.Collections;
import java.util.ResourceBundle;

public class TaulukotController implements Initializable {
    private Stage stage;

    @FXML
    private BarChart<?, ?> myBarChart;

    @FXML
    private PieChart myPieChart;

    @FXML
    protected void mainScene(ActionEvent event) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(Main.class.getResource("main-view.fxml"));
        Scene scene = new Scene(fxmlLoader.load(), 600, 400);
        stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        stage.setScene(scene);
        stage.show();
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

        //Lisätään piirakkadiagrammiin tiedot
        ObservableList<PieChart.Data> pieChartData = FXCollections.observableArrayList();
        try {
            for (Saunamäärä saunamäärä : Saunamäärä.luoPieChartLista()) {
                int määrä = Saunomiskerta.saunomiskertojaSaunassa(saunamäärä.getSauna(), Saunomiskerta.luoSaunaLista());
                pieChartData.add(new PieChart.Data(String.format("%s, %2d", saunamäärä.getSauna(), määrä), määrä));
            }
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        myPieChart.setData(pieChartData);
        myPieChart.setLegendVisible(false);

        //Lisätään pylväsdiagrammiin tiedot
        XYChart.Series set1 = new XYChart.Series();
        try {
            set1.getData().add(new XYChart.Data("Maanantai", Collections.frequency(Saunomiskerta.luoPaivaLista(), "MONDAY")));
            set1.getData().add(new XYChart.Data("Tiistai", Collections.frequency(Saunomiskerta.luoPaivaLista(), "TUESDAY")));
            set1.getData().add(new XYChart.Data("Keskiviikko", Collections.frequency(Saunomiskerta.luoPaivaLista(), "WEDNESDAY")));
            set1.getData().add(new XYChart.Data("Torstai", Collections.frequency(Saunomiskerta.luoPaivaLista(), "THURSDAY")));
            set1.getData().add(new XYChart.Data("Perjantai", Collections.frequency(Saunomiskerta.luoPaivaLista(), "FRIDAY")));
            set1.getData().add(new XYChart.Data("Lauantai", Collections.frequency(Saunomiskerta.luoPaivaLista(), "SATURDAY")));
            set1.getData().add(new XYChart.Data("Sunnuntai", Collections.frequency(Saunomiskerta.luoPaivaLista(), "SUNDAY")));
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }

        myBarChart.getData().addAll(set1);
        myBarChart.setLegendVisible(false);
    }
}

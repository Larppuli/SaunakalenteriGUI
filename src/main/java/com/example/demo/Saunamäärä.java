package com.example.demo;

import java.io.IOException;
import java.util.ArrayList;

public class Saunamäärä {
    private String sauna;

    public Saunamäärä(String sauna, int määrä) {
        this.sauna = sauna;
    }

    public String getSauna() {
        return sauna;
    }

    //Luo Saunamäärä-olioita sisältävän listan piirakkakaaviota varten
    public static ArrayList<Saunamäärä> luoPieChartLista() throws IOException, ClassNotFoundException {
        ArrayList<Saunamäärä> lista = new ArrayList<>();
        ArrayList<String> testilista = new ArrayList<>();
        for (Saunomiskerta saunomiskerta : Saunomiskerta.avaaLista()) {
            if (!testilista.contains(saunomiskerta.getSauna())) {
                testilista.add(saunomiskerta.getSauna());
                lista.add(new Saunamäärä(saunomiskerta.getSauna(), Saunomiskerta.saunomiskertojaSaunassa(saunomiskerta.getSauna(), Saunomiskerta.luoSaunaLista())));
            }
        }
        System.out.println(lista);
        return lista;
    }

}
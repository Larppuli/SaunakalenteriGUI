package com.example.demo;

import java.io.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collections;

public class Saunomiskerta implements Serializable {
    @Serial
    private static final long serialVersionUID = -306921284770384216L;
    private LocalDate paiva;
    private String sauna;
    static String tiedosto = "data.bin";

    public Saunomiskerta(LocalDate paiva, String sauna) {
        this.paiva = paiva;
        this.sauna = sauna;
    }

    public LocalDate getPaiva() {
        return paiva;
    }

    public String getSauna() {
        return sauna;
    }

    /*
    Käyttää metodia avaaLista() avaamaan data.bin-tiedoston, johon saunomiskerrat ovat tallennettu.
    Luo samalla listan ja lisää sinne saunomiskerrat String-tyyppisinä jäseninä muodossa "sauna, päivämäärä".
     */
    public static ArrayList<String> luoTapahtumaLista() throws IOException, ClassNotFoundException {
        ArrayList<String> tapahtumalista = new ArrayList<>();
        for (Saunomiskerta tapahtuma : avaaLista()) {
            String kerta = tapahtuma.getSauna() + ", " + tapahtuma.getPaiva();
            tapahtumalista.add(0, kerta);
        }
        return tapahtumalista;
    }

    // Luo Saunomiskerta-olion parametreista ja lisää olion
    public static void kirjoitaTiedostoon(String sauna, LocalDate paiva) throws IOException, ClassNotFoundException {
        Saunomiskerta tapahtuma = new Saunomiskerta(paiva, sauna);
        ObjectOutputStream oos;
        try {
            ObjectInputStream ois = new ObjectInputStream(new FileInputStream(tiedosto));
            ArrayList<Saunomiskerta> lista = (ArrayList<Saunomiskerta>) ois.readObject();
            lista.add(tapahtuma);
            oos = new ObjectOutputStream(new FileOutputStream(tiedosto));
            oos.writeObject(lista);
            oos.close();
        }
        catch (IOException e) {
            alustaTiedosto();
            ObjectInputStream ois = new ObjectInputStream(new FileInputStream(tiedosto));
            ArrayList<Saunomiskerta> lista = (ArrayList<Saunomiskerta>) ois.readObject();
            lista.add(tapahtuma);
            oos = new ObjectOutputStream(new FileOutputStream(tiedosto));
            oos.writeObject(lista);
            oos.close();
        }
    }

    // Luo uuden data.bin-tiedoston ja luo sinne tyhjän Saunomiskerta-oliota sisältävän ArrayListin
    public static void alustaTiedosto() {
        ObjectOutputStream oos;
        {
            try {
                ArrayList<Saunomiskerta> lista = new ArrayList<>();
                oos = new ObjectOutputStream(new FileOutputStream(tiedosto));
                oos.writeObject(lista);
                oos.close();
                System.out.println("Tiedosto alustettu");
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    // Palauttaa Saunomiskerta-olioita sisältävän listan, jonka jäsenet on lisätty data.bin-tiedostosta
    public static ArrayList<Saunomiskerta> avaaLista() throws IOException, ClassNotFoundException {
        ObjectInputStream ois = new ObjectInputStream(new FileInputStream(tiedosto));
        ArrayList<Saunomiskerta> lista = (ArrayList<Saunomiskerta>) ois.readObject();
        ois.close();
        return lista;
    }

    // Palauttaa parametrina saadun listan yleisimmän jäsenen
    public static String yleisinJasen(ArrayList<String> lista) {
        String yleisin = "ei saunomiskertoja";
        int suurinMaara = 0;
        for (String sauna : lista) {
            if (Collections.frequency(lista, sauna) > suurinMaara) {
                suurinMaara = Collections.frequency(lista, sauna);
                yleisin = sauna;
            }
        }
        return yleisin;
    }

    // Palauttaa String-olioita sisältävän listan, johon lisää avaaLista()-metodin palauttaman Saunomiskerta-olioita sisältävän listan jäsenten sauna
    public static ArrayList<String> luoSaunaLista() throws IOException, ClassNotFoundException {
        ArrayList<String> saunalista = new ArrayList<>();
        for (Saunomiskerta saunomiskerta : avaaLista()) {
            saunalista.add(saunomiskerta.getSauna());
        }
        return saunalista;
    }

    // Palauttaa parametrinä saadun saunan esiintymiskerrat parametrina saadussa listassa
    public static int saunomiskertojaSaunassa(String sauna, ArrayList<String> lista) {
        int counter = 0;
        for (String kerta : lista) {
            if (kerta.equals(sauna)) {
                counter += 1;
            }
        }
        return counter;
    }

    // Palauttaa merkkijonon parametreina saaduista saunasta ja määrästä
    public static String kasaaYleisinString(String sauna, int maara) {
        return sauna + ", " + String.valueOf(maara) + "kpl";
    }
}

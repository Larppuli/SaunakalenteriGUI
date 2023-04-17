package com.example.demo;

import java.io.*;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Objects;

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
            tapahtumalista.add(kerta);
        }
        return tapahtumalista;
    }

    // Luo Saunomiskerta-olion parametreista ja lisää olion listaan
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

    // Luodaan merkkijonotyyppisistä saunomiskertoja kuvaavista olioista lista, josta on poistettu valittu alkio. Lista välitetään kirjoitaListaTiedostoon-metodille
    public static void poistaListasta(String poistettava) throws IOException, ClassNotFoundException {
        ArrayList<String> poistolista = luoTapahtumaLista();
        poistolista.remove(poistettava);
        kirjoitaListaTiedostoon(poistolista);
    }

    // Muunnetaan listan merkkijonotyyppiset saunomiskertoja kuvaavat oliot Saunomiskerta-olioiksi ja kirjoitetaan ne data.bin-tiedostoon
    public static void kirjoitaListaTiedostoon(ArrayList<String> lista) throws IOException, ClassNotFoundException {
        alustaTiedosto();
        for (String alkio : lista) {
            Saunomiskerta olio = muunnaOlioksi(alkio);
            kirjoitaTiedostoon(olio.getSauna(), olio.getPaiva());
        }
    }

    // Muunnetaan muodossa "sauna, päivämäärä" oleva merkkijono-olio Saunomiskerta-olioksi
    public static Saunomiskerta muunnaOlioksi(String tapahtuma) {
        String paivaString = tapahtuma.substring(tapahtuma.length()-10);
        String sauna = tapahtuma.substring(0, tapahtuma.length()-12);
        LocalDate localDate = LocalDate.parse(paivaString, DateTimeFormatter.ofPattern("yyyy-MM-dd"));

        return new Saunomiskerta(localDate, sauna);
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

    // Palauttaa String-olioita sisältävän listan, johon lisää avaaLista()-metodin palauttaman Saunomiskerta-olioita sisältävän listan jäsenten saunat
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
        return String.format("%s, %2d kpl",sauna, maara);
    }

    //Luodaan lista, joka sisältää tietyn viikonpäivän niin monta kertaa kuin sinä päivänä on saunottu
    public static ArrayList<String> luoPaivaLista() throws IOException, ClassNotFoundException {
        ArrayList<String> lista = new ArrayList<>();
        for (Saunomiskerta saunomiskerta : avaaLista()) {
            lista.add(String.valueOf(saunomiskerta.getPaiva().getDayOfWeek()));
        }
        return lista;
    }

    // Tarkastetaan onko saunomiskertoja tallennettu senhetkiselle päivämäärälle
    public static ArrayList<Saunomiskerta> etsiVuosipaiva() throws IOException, ClassNotFoundException {
        ArrayList<Saunomiskerta> saunalista = new ArrayList<>();
        for (Saunomiskerta saunakerta : avaaLista()) {
            if (String.valueOf(saunakerta.getPaiva()).substring(5,10).equals(String.valueOf(LocalDateTime.now()).substring(5,10))) {
                saunalista.add(saunakerta);
            }
        }
        return saunalista;
    }

    //Muunnetaan etsiVuosipäivä()-metodin palauttama lista merkkijonoksi. METODI EI KÄYTÖSSÄ
    public static String muunnaVuosipaivaMerkkijonoksi() throws IOException, ClassNotFoundException {
        String alimerkkijono;
        String merkkijono = "";
        for (Saunomiskerta kerta : etsiVuosipaiva()) {
            alimerkkijono = "";
            alimerkkijono = alimerkkijono.concat(kerta.getSauna() + ", " + kerta.getPaiva() + "\n");
            merkkijono = merkkijono.concat(alimerkkijono);
        }
        return merkkijono;
    }

    public static ArrayList<String> palautaVuosipaivalista() throws IOException, ClassNotFoundException {
        ArrayList<String> vuosipaivalista = new ArrayList<>();
        if (etsiVuosipaiva().size() != 0) {
            etsiVuosipaiva().stream()
                    .forEach(item -> {
                        String item2 = item.getSauna() + ", " + item.getPaiva();
                        vuosipaivalista.add(item2);
                    });
        }
        else {
            vuosipaivalista.add("Ei saunomiskertoja tällä päivämäärällä");
        }
        return vuosipaivalista;
    }
}

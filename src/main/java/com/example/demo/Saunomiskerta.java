package com.example.demo;

import java.io.*;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Collections;

public class Saunomiskerta implements Serializable {
    @Serial
    private static final long serialVersionUID = -306921284770384216L;
    private LocalDate paiva;
    private String sauna;

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

    // Palauttaa data.bin tiedostossa olevan Kayttaja-olion saunalistan, jonka nimi-attribuutti vastaa istunnon nimeä
    public static ArrayList<Saunomiskerta> avaaKayttajanLista() throws IOException, ClassNotFoundException {
        ObjectInputStream ois = new ObjectInputStream(new FileInputStream(Kayttaja.tiedosto));
        ArrayList<Kayttaja> lista = (ArrayList<Kayttaja>) ois.readObject();
        for (Kayttaja kayttaja : lista) {
            if (kayttaja.getNimi().equalsIgnoreCase(Kayttaja.tarkistaIstunto())) {
                return kayttaja.getSaunalista();
            }
        }
        ois.close();
        return null;
    }

    /*
    Käyttää metodia avaaKayttajanLista() avaamaan data.bin-tiedoston johon, Kayttaja-oliot ovat tallennettu.
    Luo samalla listan ja lisää sinne istuntoon tallennetun käyttäjän saunomiskerrat String-tyyppisinä jäseninä muodossa "sauna, päivämäärä".
     */
    public static ArrayList<String> luoTapahtumaLista() throws IOException, ClassNotFoundException {
        ArrayList<String> tapahtumalista = new ArrayList<>();
        for (Saunomiskerta tapahtuma : avaaKayttajanLista()) {
            String kerta = tapahtuma.getSauna() + ", " + tapahtuma.getPaiva();
            tapahtumalista.add(kerta);
        }
        return tapahtumalista;
    }

    // Luodaan merkkijonotyyppisistä saunomiskertoja kuvaavista olioista lista, josta on poistettu valittu alkio. Lista välitetään kirjoitaListaTiedostoon-metodille
    public static void poistaListasta(String poistettava) throws IOException, ClassNotFoundException {
        ArrayList<String> poistolista = luoTapahtumaLista();
        poistolista.remove(poistettava);
        Kayttaja.kirjoitaListaTiedostoon(poistolista);
    }

    // Muunnetaan muodossa "sauna, päivämäärä" oleva merkkijono-olio Saunomiskerta-olioksi
    public static Saunomiskerta muunnaOlioksi(String tapahtuma) {
        String paivaString = tapahtuma.substring(tapahtuma.length()-10);
        String sauna = tapahtuma.substring(0, tapahtuma.length()-12);
        LocalDate localDate = LocalDate.parse(paivaString, DateTimeFormatter.ofPattern("yyyy-MM-dd"));

        return new Saunomiskerta(localDate, sauna);
    }

    // Palauttaa parametrina saadun listan yleisimmän jäsenen
    public static String yleisinJasen(ArrayList<String> lista) {
        String yleisin = "";
        int suurinMaara = 0;
        for (String sauna : lista) {
            if (Collections.frequency(lista, sauna) > suurinMaara) {
                suurinMaara = Collections.frequency(lista, sauna);
                yleisin = sauna;
            }
        }
        return yleisin;
    }

    // Palauttaa String-olioita sisältävän listan, johon lisää avaaKayttajanLista()-metodin palauttaman Saunomiskerta-olioita sisältävän listan jäsenten saunat
    public static ArrayList<String> luoSaunaLista() throws IOException, ClassNotFoundException {
        ArrayList<String> saunalista = new ArrayList<>();
        for (Saunomiskerta saunomiskerta : avaaKayttajanLista()) {
            saunalista.add(saunomiskerta.getSauna().toLowerCase());
        }
        return saunalista;
    }

    // Palauttaa parametrinä saadun saunan esiintymiskerrat parametrina saadussa listassa
    public static int saunomiskertojaSaunassa(String sauna, ArrayList<String> lista) {
        return (int) lista.stream()
                .filter(kerta -> kerta.equalsIgnoreCase(sauna))
                .count();
    }

    // Palauttaa merkkijonon parametreina saaduista saunasta ja määrästä
    public static String kasaaYleisinString(String sauna, int maara) {
        if (!sauna.equals("")) {
            return String.format("%s, %2d kpl", sauna, maara);
        }
        else
        {
            return String.format("Ei saunomiskertoja");
        }
    }

    //Luodaan lista, joka sisältää tietyn viikonpäivän niin monta kertaa kuin sinä päivänä on saunottu
    public static ArrayList<String> luoPaivaLista() throws IOException, ClassNotFoundException {
        ArrayList<String> lista = new ArrayList<>();
        for (Saunomiskerta saunomiskerta : avaaKayttajanLista()) {
            lista.add(String.valueOf(saunomiskerta.getPaiva().getDayOfWeek()));
        }
        return lista;
    }

    // Tarkastetaan onko saunomiskertoja tallennettu senhetkiselle päivämäärälle
    public static ArrayList<Saunomiskerta> etsiVuosipaiva() throws IOException, ClassNotFoundException {
        ArrayList<Saunomiskerta> saunalista = new ArrayList<>();
        for (Saunomiskerta saunakerta : avaaKayttajanLista()) {
            if (String.valueOf(saunakerta.getPaiva()).substring(5,10).equals(String.valueOf(LocalDateTime.now()).substring(5,10))) {
                saunalista.add(saunakerta);
            }
        }
        return saunalista;
    }

    // Palauttaa senhetkiselle päivämäärälle aikaisempina vuosina tallennetut saunomiskerrat merkkijonolistassa
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

package com.example.demo;

import java.io.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.stream.Collectors;

public class Kayttaja implements Serializable{

    String nimi;
    String salasana;
    ArrayList<Saunomiskerta> saunalista;
    static String tiedosto = "data.bin";
    static String istunto = "session.bin";

    public void setSaunalista(ArrayList<Saunomiskerta> saunalista) {
        this.saunalista = saunalista;
    }

    public Kayttaja(String nimi, String salasana, ArrayList<Saunomiskerta> saunalista) {
        this.nimi = nimi;
        this.salasana = salasana;
        this.saunalista = saunalista;
    }

    protected String getNimi() {
        return nimi;
    }

    protected String getSalasana() {
        return salasana;
    }

    protected ArrayList<Saunomiskerta> getSaunalista() {
        return saunalista;
    }

    // Palauttaa Kayttaja-olioita sisältävän listan, jonka jäsenet on lisätty data.bin-tiedostosta
    public static ArrayList<Kayttaja> avaaLista() throws IOException, ClassNotFoundException {
        ObjectInputStream ois = new ObjectInputStream(new FileInputStream(tiedosto));
        ArrayList<Kayttaja> lista = (ArrayList<Kayttaja>) ois.readObject();
        ois.close();
        return lista;
    }

    // Luo uuden data.bin-tiedoston ja luo sinne tyhjän Kayttaja-oliota sisältävän ArrayListin
    public static void alustaTiedosto() {
        ObjectOutputStream oos;
        {
            try {
                ArrayList<Kayttaja> lista = new ArrayList<>();
                oos = new ObjectOutputStream(new FileOutputStream(tiedosto));
                oos.writeObject(lista);
                oos.close();
                System.out.println("Tiedosto alustettu");
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    // Luo uuden session.bin-tiedoston ja luo sinne tyhjän Merkkijonoja sisältävän ArrayListin
    public static void alustaIstunto() {
        ObjectOutputStream oos;
        {
            try {
                ArrayList<String> lista = new ArrayList<>();
                oos = new ObjectOutputStream(new FileOutputStream(istunto));
                oos.writeObject(lista);
                oos.close();
                System.out.println("Istunto alustettu");
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    // Tarkistaa mikä käyttäjä on kirjautunut sisään
    public static String tarkistaIstunto() throws IOException, ClassNotFoundException {
        ObjectInputStream ois = new ObjectInputStream(new FileInputStream(istunto));
        ArrayList<String> kayttajalista = (ArrayList<String>) ois.readObject();
        String kayttaja = kayttajalista.get(0);
        ois.close();
        return kayttaja;
    }

    public static void luoIstunto(String kayttajatunnus) throws IOException, ClassNotFoundException {
        ObjectOutputStream oos;
        try {
            ObjectInputStream ois = new ObjectInputStream(new FileInputStream(istunto));
            ArrayList<String> lista = (ArrayList<String>) ois.readObject();
            lista.add(kayttajatunnus);
            oos = new ObjectOutputStream(new FileOutputStream(istunto));
            oos.writeObject(lista);
            oos.close();
        }
        catch (IOException e) {
            alustaIstunto();
            ObjectInputStream ois = new ObjectInputStream(new FileInputStream(istunto));
            ArrayList<String> lista = (ArrayList<String>) ois.readObject();
            lista.add(kayttajatunnus);
            oos = new ObjectOutputStream(new FileOutputStream(istunto));
            oos.writeObject(lista);
            oos.close();
        }

    }

    // Luo Saunomiskerta-olion parametreista ja lisää olion listaan
    public static void lisaaKayttajaTiedostoon(String nimi, String salasana, ArrayList<Saunomiskerta> saunalista) throws IOException, ClassNotFoundException {
        Kayttaja kayttaja = new Kayttaja(nimi, salasana, saunalista);
        ObjectOutputStream oos;
        try {
            ObjectInputStream ois = new ObjectInputStream(new FileInputStream(tiedosto));
            ArrayList<Kayttaja> lista = (ArrayList<Kayttaja>) ois.readObject();
            lista.add(kayttaja);
            oos = new ObjectOutputStream(new FileOutputStream(tiedosto));
            oos.writeObject(lista);
            oos.close();
        }
        catch (IOException e) {
            alustaTiedosto();
            ObjectInputStream ois = new ObjectInputStream(new FileInputStream(tiedosto));
            ArrayList<Kayttaja> lista = (ArrayList<Kayttaja>) ois.readObject();
            lista.add(kayttaja);
            oos = new ObjectOutputStream(new FileOutputStream(tiedosto));
            oos.writeObject(lista);
            oos.close();
        }
    }

    // Metodi lisää uuden saunomiskerran istuntoa vastaavan käyttäjän saunalistaan ja päivitetty lista kierjoitetaan tiedostoon
     public static void kirjoitaTiedostoon(String sauna, LocalDate paiva) throws IOException, ClassNotFoundException {
        String istuja = tarkistaIstunto();
        ObjectOutputStream oos;
        ArrayList<Kayttaja> lista = new ArrayList<>(avaaLista());
        for (Kayttaja kayttaja : avaaLista()) {
            if (istuja.equalsIgnoreCase(kayttaja.getNimi())) {
                lista.remove(lista.indexOf(lista.stream().filter(k -> istuja.equals(k.getNimi())).findFirst().orElse(null)));
                kayttaja.saunalista.add(new Saunomiskerta(paiva, sauna));
                lista.add(kayttaja);
            }
        }
        alustaTiedosto();
        oos = new ObjectOutputStream(new FileOutputStream(tiedosto));
        oos.writeObject(lista);
        oos.close();
     }

    public static void kirjoitaListaTiedostoon(ArrayList<String> tapahtumalista) throws IOException, ClassNotFoundException {
        String istuja = tarkistaIstunto();
        ObjectOutputStream oos;
        ArrayList<Kayttaja> kayttajalista = new ArrayList<>(avaaLista());
        ArrayList<Saunomiskerta> saunalista = (ArrayList<Saunomiskerta>) tapahtumalista.stream().map(tapahtuma -> Saunomiskerta.muunnaOlioksi(tapahtuma)).collect(Collectors.toList());
        kayttajalista.stream().filter(k -> istuja.equalsIgnoreCase(k.getNimi())).findFirst().orElse(null).setSaunalista(saunalista);
        alustaTiedosto();
        oos = new ObjectOutputStream(new FileOutputStream(tiedosto));
        oos.writeObject(kayttajalista);
        oos.close();
    }

    // Tarkistetaan onko haluttu nimi jo käytössä
    public static boolean nimitarkistus(String nimi)  {
        try
        {
            for (Kayttaja kayttaja : avaaLista()) {
                if (kayttaja.getNimi().equalsIgnoreCase(nimi)) {
                    return false;
                }
            }
            return true;
        }
        catch (Exception e)
        {
            return true;
        }
    }

}

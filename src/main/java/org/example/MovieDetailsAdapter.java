package org.example;

public class MovieDetailsAdapter {
    private int id_filmu;
    private String tytul;
    private int czas_trwania;
    private double ocena;
    private String opis;
    private int id_gatunku;
    private byte[] okladka = null;
    private double cena;

    public MovieDetailsAdapter(int id_filmu, String tytul, int czas_trwania, double ocena, String opis, int id_gatunku, byte[] okladka, double cena) {
        this.id_filmu = id_filmu;
        this.tytul = tytul;
        this.czas_trwania = czas_trwania;
        this.ocena = ocena;
        this.opis = opis;
        this.id_gatunku = id_gatunku;
        this.okladka = okladka;
        this.cena = cena;
    }
    public int getId_filmu() {
        return id_filmu;
    }

    public void setId_filmu(int id_filmu) {
        this.id_filmu = id_filmu;
    }

    public String getTytul() {
        return tytul;
    }

    public void setTytul(String tytul) {
        this.tytul = tytul;
    }

    public int getCzas_trwania() {
        return czas_trwania;
    }

    public void setCzas_trwania(int czas_trwania) {
        this.czas_trwania = czas_trwania;
    }

    public double getOcena() {
        return ocena;
    }

    public void setOcena(double ocena) {
        this.ocena = ocena;
    }

    public String getOpis() {
        return opis;
    }

    public void setOpis(String opis) {
        this.opis = opis;
    }

    public int getId_gatunku() {
        return id_gatunku;
    }

    public void setId_gatunku(int id_gatunku) {
        this.id_gatunku = id_gatunku;
    }

    public byte[] getOkladka() {
        return okladka;
    }

    public void setOkladka(byte[] okladka) {
        this.okladka = okladka;
    }

    public double getCena() {
        return cena;
    }

    public void setCena(double cena) {
        this.cena = cena;
    }

}

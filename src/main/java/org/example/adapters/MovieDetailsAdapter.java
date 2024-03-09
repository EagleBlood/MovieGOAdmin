package org.example.adapters;

public class MovieDetailsAdapter {
    private String tytul;
    private int czas_trwania;
    private double ocena;
    private String opis;
    private String nazwa_gatunku;
    private String okladka;
    private double cena;

    public MovieDetailsAdapter(String tytul, String opis, int czas_trwania, double ocena, String nazwa_gatunku, String okladka, double cena) {
        this.tytul = tytul;
        this.czas_trwania = czas_trwania;
        this.ocena = ocena;
        this.opis = opis;
        this.nazwa_gatunku = nazwa_gatunku;
        this.okladka = okladka;
        this.cena = cena;
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

    public String getNazwa_gatunku() {
        return nazwa_gatunku;
    }

    public void setNazwa_gatunku(String nazwa_gatunku) {
        this.nazwa_gatunku = nazwa_gatunku;
    }

    public String getOkladka() {
        return okladka;
    }

    public void setOkladka(String okladka) {
        this.okladka = okladka;
    }

    public double getCena() {
        return cena;
    }

    public void setCena(double cena) {
        this.cena = cena;
    }

}

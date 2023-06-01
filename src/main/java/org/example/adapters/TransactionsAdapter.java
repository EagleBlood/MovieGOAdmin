package org.example.adapters;

import java.util.ArrayList;
import java.util.List;

public class TransactionsAdapter {
    private String nr_rezerwacji;
    private String tytul;
    private String login;
    private int id_biletu;
    private List<String> miejsca;
    private double cena;

    public TransactionsAdapter(String nr_rezerwacji, String tytul, String login, int id_biletu, List<String> miejsca, double cena) {
        this.nr_rezerwacji = nr_rezerwacji;
        this.tytul = tytul;
        this.login = login;
        this.id_biletu = id_biletu;
        this.miejsca = miejsca;
        this.cena = cena;
    }

    public String getNr_rezerwacji() {
        return nr_rezerwacji;
    }

    public void setNr_rezerwacji(String nr_rezerwacji) {
        this.nr_rezerwacji = nr_rezerwacji;
    }

    public String getTytul() {
        return tytul;
    }

    public void setTytul(String tytul) {
        this.tytul = tytul;
    }

    public String getLogin() {
        return login;
    }

    public void setLogin(String login) {
        this.login = login;
    }

    public int getId_biletu() {
        return id_biletu;
    }

    public void setId_biletu(int id_biletu) {
        this.id_biletu = id_biletu;
    }

    public List<String> getMiejsca() {
        return miejsca;
    }

    public void setMiejsca(List<String> miejsca) {
        this.miejsca = miejsca;
    }

    public double getCena() {
        return cena;
    }

    public void setCena(double cena) {
        this.cena = cena;
    }
}
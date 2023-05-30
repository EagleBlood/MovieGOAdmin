package org.example.adapters;

import java.time.LocalDate;

public class MovieShowtimeAdapter {
    private int id_seansu;
    private String tytul;
    private int id_sala;
    private String data;
    private String pora_emisji;

    public MovieShowtimeAdapter(int id_seansu, String tytul, int id_sala, String data, String pora_emisji) {
        this.id_seansu = id_seansu;
        this.tytul = tytul;
        this.id_sala = id_sala;
        this.data = data;
        this.pora_emisji = pora_emisji;
    }

    public int getId_seansu() {
        return id_seansu;
    }

    public void setId_seansu(int id_seansu) {
        this.id_seansu = id_seansu;
    }

    public String getTytul() {
        return tytul;
    }

    public void setTytul(String tytul) {
        this.tytul = tytul;
    }

    public int getId_sala() {
        return id_sala;
    }

    public void setId_sala(int id_sala) {
        this.id_sala = id_sala;
    }

    public String getData() {
        return data;
    }

    public void setData(String data) {
        this.data = data;
    }

    public String getPora_emisji() {
        return pora_emisji;
    }

    public void setPora_emisji(String pora_emisji) {
        this.pora_emisji = pora_emisji;
    }
}

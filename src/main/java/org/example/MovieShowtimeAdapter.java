package org.example;

import java.util.List;

public class MovieShowtimeAdapter {
    private int id_seansu;
    private int id_filmu;
    private int id_sala;
    private String data;
    private String pora_emisji;

    public MovieShowtimeAdapter(int id_seansu, int id_filmu, int id_sala, String data, String pora_emisji) {
        this.id_seansu = id_seansu;
        this.id_filmu = id_filmu;
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

    public int getId_filmu() {
        return id_filmu;
    }

    public void setId_filmu(int id_filmu) {
        this.id_filmu = id_filmu;
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

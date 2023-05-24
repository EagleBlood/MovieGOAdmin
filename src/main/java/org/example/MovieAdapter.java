package org.example;

public class MovieAdapter {
    private int id_filmu;
    private String tytul;

    public MovieAdapter(int id_filmu, String tytul) {
        this.id_filmu = id_filmu;
        this.tytul = tytul;
    }

    public int getId_filmu() {
        return id_filmu;
    }

    public String getTytul() {
        return tytul;
    }

    @Override
    public String toString() {
        return getTytul(); // Display the title when the object is converted to a string
    }
}

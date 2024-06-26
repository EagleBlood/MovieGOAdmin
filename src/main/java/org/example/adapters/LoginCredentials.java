package org.example.adapters;

public class LoginCredentials {
    private static String username;
    private static String password;
    private static String dbUrl = "jdbc:mysql:yourDomain";

    public static void setCredentials(String username, String password) {
        LoginCredentials.username = username;
        LoginCredentials.password = password;
    }

    public static String getUsername() {
        return username;
    }

    public static String getPassword() {
        return password;
    }

    public static String getDbUrl() {
        return dbUrl;
    }
}

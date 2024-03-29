package it.interno.normalizzatore.util;

public class Util {


    public static String extractCivicNumber(String address) {

        String[] s = address.split(",");
        return s.length == 2 || s.length > 2 ? s[1].trim().replace(" ", "") : "";
    }
}

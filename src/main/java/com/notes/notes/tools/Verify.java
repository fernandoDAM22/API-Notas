package com.notes.notes.tools;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Verify {
    /**
     * Patron para verificar que el nombre es correcto
     */
    private static String PATTERN_NAME = "^[a-zA-Z0-9]+$";
    /**
     * Patron para verificar que el email es correcto
     */
    private static String PATTERN_EMAIL = "^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,}$";

    /**
     * Este metodo permite comprobar que el nombre de un usuario cumple con los requisitos
     * @param name es el nombre que queremos comprobar
     * @return true si el nombre es correcto, false si no
     */
    public static boolean verifyName(String name) {
        Pattern pattern = Pattern.compile(PATTERN_NAME);
        Matcher matcher = pattern.matcher(name);
        return matcher.matches();
    }

    /**
     * Este metodo permite comprobar si el email de un usuario es correcto
     * @param email es el email que queremos comprobar
     * @return true si el email es correcto, false si no
     */
    public static boolean verifyEmail(String email) {
        Pattern pattern = Pattern.compile(PATTERN_EMAIL);
        Matcher matcher = pattern.matcher(email);
        return matcher.matches();
    }
}

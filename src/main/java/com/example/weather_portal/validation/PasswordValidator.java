package com.example.weather_portal.validation;

public class PasswordValidator {

    private static final String PASSWORD_REGEX =
            "^(?=.*[A-Za-z])(?=.*\\d)(?=.*[^A-Za-z0-9]).{8,}$";

    public static boolean isValid(String password) {
        if (password == null) return false;
        return password.matches(PASSWORD_REGEX);
    }
}

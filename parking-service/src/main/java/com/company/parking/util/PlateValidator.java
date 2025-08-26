package com.company.parking.util;

public final class PlateValidator {
    private PlateValidator() {}
    public static boolean isValid(String plate) {
        // 6 alfanuméricos, sin ñ ni especiales
        return plate != null && plate.matches("(?i)^[A-HJ-NP-Za-hj-np-z0-9]{6}$");
    }
}

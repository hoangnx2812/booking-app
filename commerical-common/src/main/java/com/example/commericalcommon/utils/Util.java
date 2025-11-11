package com.example.commericalcommon.utils;

import lombok.experimental.UtilityClass;

import java.security.SecureRandom;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Base64;

@UtilityClass
public class Util {
    private static final SecureRandom SR = new SecureRandom();
    private static final DateTimeFormatter TIME_FORMAT =
            DateTimeFormatter.ofPattern("ddMMyyHHmmssSSS");

    public static String generateSalt(Integer numBytes) {
        if (numBytes == null || numBytes <= 0) {
            numBytes = 32;
        }
        byte[] b = new byte[numBytes];
        SR.nextBytes(b);
        return Base64.getUrlEncoder().withoutPadding().encodeToString(b);
    }

    public static String generateNo(String prefix) {
        String timePart = LocalDateTime.now().format(TIME_FORMAT);
        String randPart = generateSalt(4).toUpperCase();
        return prefix + timePart + randPart;
    }

}

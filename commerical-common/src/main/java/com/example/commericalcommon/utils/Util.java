package com.example.commericalcommon.utils;

import lombok.experimental.UtilityClass;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.sql.Timestamp;
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

    public static String encryptSHA256(String input) {
        StringBuilder hex;
        try {
            MessageDigest md = MessageDigest.getInstance("SHA-256");
            byte[] hash = md.digest(input.getBytes(StandardCharsets.UTF_8));
            hex = new StringBuilder();
            for (byte b : hash) {
                hex.append(String.format("%02x", b));
            }
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException(e);
        }
        return hex.toString();
    }

    public static String getStackTrace(Exception e) {
        StringWriter sWriter = new StringWriter();
        PrintWriter pWriter = new PrintWriter(sWriter);
        e.printStackTrace(pWriter);
        return sWriter.toString();
    }

    public static LocalDateTime isNotNull(Timestamp obj) {
        return obj == null ? null : obj.toLocalDateTime();
    }


    public static void main(String[] args) {
        System.out.println(encryptSHA256("6f2cb9dd8f4b65e24e1c3f3fa5bc57982349237f11abceacd45bbcb74d621c25"));
    }

}

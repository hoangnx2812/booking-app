package com.example.commericalcommon.utils;


import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.function.Function;

@Component
public class DateTimeFormatter {
    Map<Long, Function<LocalDateTime, String>> strategyMap = new LinkedHashMap<>();

    public DateTimeFormatter() {
        strategyMap.put(60L, this::formatInSeconds);
        strategyMap.put(3600L, this::formatInMinutes);
        strategyMap.put(86400L, this::formatInHours);
        strategyMap.put(Long.MAX_VALUE, this::formatInDate);
    }

    public String format(LocalDateTime localDateTime) {
        long elapseSeconds = ChronoUnit.SECONDS.between(localDateTime, LocalDateTime.now());

        var strategy = strategyMap.entrySet()
                .stream()
                .filter(longFunctionEntry ->
                        elapseSeconds < longFunctionEntry.getKey())
                .findFirst()
                .orElse(null);
        return strategy != null ? strategy.getValue().apply(localDateTime) : "";
    }

    private String formatInSeconds(LocalDateTime localDateTime) {
        long elapseSeconds = ChronoUnit.SECONDS.between(localDateTime, LocalDateTime.now());
        return String.format("%s giây trước", elapseSeconds);
    }

    private String formatInMinutes(LocalDateTime localDateTime) {
        long elapseMinutes = ChronoUnit.MINUTES.between(localDateTime, LocalDateTime.now());
        return String.format("%s phút trước", elapseMinutes);
    }

    private String formatInHours(LocalDateTime localDateTime) {
        long elapseHours = ChronoUnit.HOURS.between(localDateTime, LocalDateTime.now());
        return String.format("%s giờ trước", elapseHours);
    }

    private String formatInDate(LocalDateTime localDateTime) {
        return localDateTime.format(java.time.format.DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm"));
    }
}

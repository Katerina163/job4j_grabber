package ru.job4j.utils;

import java.time.LocalDate;
import java.time.LocalDateTime;

public class SqlRuDateTimeParser implements DateTimeParser {
    @Override
    public LocalDateTime parse(String parse) {
        LocalDateTime date = null;
        String[] array = parse.split(",");
        String[] dataArray = array[0].split(" ");
        String[] time = array[1].split(":");
        if (dataArray[0].startsWith("с")) {
            LocalDate now = LocalDate.now();
            date = now.atTime(Integer.parseInt(time[0].trim()), Integer.parseInt(time[1]), 0);
        } else if (dataArray[0].startsWith("в")) {
            LocalDate now = LocalDate.now().minusDays(1);
            date = now.atTime(Integer.parseInt(time[0].trim()), Integer.parseInt(time[1]), 0);
        } else {
            int year = 2000 + Integer.parseInt(dataArray[2].substring(0, 2));
            date = LocalDateTime.of(year, month(dataArray[1]),
                    Integer.parseInt(dataArray[0]),
                    Integer.parseInt(time[0].trim()),
                    Integer.parseInt(time[1]));
        }
        return date;
    }

    private int month(String str) {
        switch (str) {
            case "янв": return 1;
            case "фев": return 2;
            case "мар": return 3;
            case "апр": return 4;
            case "май": return 5;
            case "июн": return 6;
            case "июл": return 7;
            case "авг": return 8;
            case "сен": return 9;
            case "окт": return 10;
            case "ноя": return 11;
            case "дек": return 12;
            default: return -1;
        }
    }
}

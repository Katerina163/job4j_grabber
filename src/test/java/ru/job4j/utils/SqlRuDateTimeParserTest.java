package ru.job4j.utils;

import org.junit.Test;

import java.time.LocalDateTime;

import static org.hamcrest.Matchers.is;
import static org.junit.Assert.*;

public class SqlRuDateTimeParserTest {
    @Test
    public void testWhenToday() {
        SqlRuDateTimeParser parser = new SqlRuDateTimeParser();
        String date = "сегодня, 07:16";
        LocalDateTime time = LocalDateTime.now().withHour(7).withMinute(16).withSecond(0).withNano(0);
        assertThat(parser.parse(date), is(time));
    }

    @Test
    public void testWhenYesterday() {
        SqlRuDateTimeParser parser = new SqlRuDateTimeParser();
        String date = "вчера, 07:16";
        LocalDateTime time = LocalDateTime.now().withHour(7).withMinute(16).withSecond(0).withNano(0).minusDays(1);
        assertThat(parser.parse(date), is(time));
    }

    @Test
    public void testWhenDate() {
        SqlRuDateTimeParser parser = new SqlRuDateTimeParser();
        String date = "1 янв 20, 07:16";
        LocalDateTime time = LocalDateTime.of(2020, 1, 1, 7, 16);
        assertThat(parser.parse(date), is(time));
    }
}
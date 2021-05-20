package ru.job4j.grabber;

import org.junit.Test;

import java.sql.Date;

import static org.hamcrest.Matchers.is;
import static org.junit.Assert.*;

public class SqlRuParseTest {
    @Test
    public void testDetail() {
        String link = "https://www.sql.ru/forum/1335852/midl-java-razrabotchik-mikroservisy-spb";
        String name = "Мидл Java разработчик. Микросервисы. СПб";
        String text = "Добрый день! " +
                "В банке есть вакансия Java-разработчика. Микросервисы, spring boot, Hibernate. "
                + "Желателен опыт работы с Oracle - написание SQL запросов хотя бы. Ценник не "
                + "выдающийся - 250 гросс + ДМС + премии. Флудить по этому поводу давайте не будем "
                + "- сколько есть по штатному расписанию. Понимаю что не много, но уже и не слишком "
                + "мало. Если интересно - пишите в личку.";
        Date date = new Date(2021, 5, 4);
        SqlRuParse sql = new SqlRuParse();
        Post result = sql.detail(link);
        assertThat(result.getName(), is(name));
        assertThat(result.getText(), is(text));
        assertThat(result.getLink(), is(link));
        assertThat(result.getDate(), is(date));
    }
}
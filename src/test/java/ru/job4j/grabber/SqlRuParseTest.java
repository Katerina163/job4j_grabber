package ru.job4j.grabber;

import org.junit.Test;
import ru.job4j.utils.SqlRuDateTimeParser;

import java.sql.Date;
import java.time.LocalDateTime;

import static org.hamcrest.Matchers.is;
import static org.junit.Assert.*;

public class SqlRuParseTest {
    @Test
    public void test() {
        String link = "https://www.sql.ru/forum/1335852/midl-java-razrabotchik-mikroservisy-spb";
        String name = "Мидл Java разработчик. Микросервисы. СПб";
        String text = "Добрый день! "
                + "В банке есть вакансия Java-разработчика. Микросервисы, spring boot, Hibernate. "
                + "Желателен опыт работы с Oracle - написание SQL запросов хотя бы. Ценник не "
                + "выдающийся - 250 гросс + ДМС + премии. Флудить по этому поводу давайте не будем "
                + "- сколько есть по штатному расписанию. Понимаю что не много, но уже и не слишком "
                + "мало. Если интересно - пишите в личку.";
        SqlRuParse sql = new SqlRuParse();
        SqlRuDateTimeParser parser = new SqlRuDateTimeParser();
        LocalDateTime time = parser.parse("4 май 21, 10:34");
        Date date = Date.valueOf(time.toLocalDate());
        Post post = new Post();
        post.setName(name);
        post.setText(text);
        post.setLink(link);
        post.setDate(date);
        Post result = sql.detail(link);
        assertThat(result, is(post));
    }
}
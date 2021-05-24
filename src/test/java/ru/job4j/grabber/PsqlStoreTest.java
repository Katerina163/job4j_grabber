package ru.job4j.grabber;

import org.junit.Test;
import ru.job4j.utils.SqlRuDateTimeParser;

import java.io.BufferedInputStream;
import java.io.FileInputStream;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.Date;
import java.sql.DriverManager;
import java.time.LocalDateTime;
import java.util.Properties;

import static org.hamcrest.Matchers.is;
import static org.junit.Assert.*;

public class PsqlStoreTest {
    public Connection init() {
        try (InputStream in = new BufferedInputStream(new FileInputStream("./properties/app.properties"))) {
            Properties config = new Properties();
            config.load(in);
            Class.forName(config.getProperty("driver"));
            return DriverManager.getConnection(
                    config.getProperty("url"),
                    config.getProperty("username"),
                    config.getProperty("password")
            );
        } catch (Exception e) {
            throw new IllegalStateException(e);
        }
    }

    @Test
    public void test() throws Exception {
        try (PsqlStore store = new PsqlStore(ConnectionRollback.create(this.init()))) {
            Post post = new Post();
            SqlRuDateTimeParser sql = new SqlRuDateTimeParser();
            post.setName("name1");
            post.setText("text1");
            post.setLink("link1");
            LocalDateTime time = sql.parse("4 янв 21, 12:14");
            Date date = Date.valueOf(time.toLocalDate());
            post.setDate(date.toLocalDate().atTime(12, 14));
            store.deleteAll();
            store.save(post);
            Post result = store.getAll().get(0);
            assertThat(post, is(result));
        }
    }

}
package ru.job4j.grabber;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import ru.job4j.utils.SqlRuDateTimeParser;

import java.io.IOException;
import java.time.LocalDateTime;

import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.sql.Date;
import java.util.List;

public class SqlRuParse implements Parse {
    @Override
    public List<Post> list(String link) {
        List<Post> result = new ArrayList<>(53);
        int id = 1;
        SqlRuDateTimeParser parser = new SqlRuDateTimeParser();
        try {
            Document doc = Jsoup.connect(link).get();
            Elements row = doc.select("tr");
            Elements posts = row.select(".postslisttopic");
            for (Element td : posts) {
                Post post = new Post();
                Element href = td.child(0);
                String html = href.attr("href");
                post.setLink(html);
                Document document = Jsoup.connect(html).get();
                Elements texts = document.select(".msgBody");
                post.setText(texts.eachText().get(1));
                Elements dates = document.select(".msgFooter");
                String date = dates.eachText().get(0);
                LocalDateTime session = parser.parse(date.substring(0, date.indexOf("[")));
                ZonedDateTime zdt = session.atZone(ZoneId.systemDefault());
                long millis = zdt.toInstant().toEpochMilli();
                post.setDate(new Date(millis));
                post.setName(href.text());
                post.setId(id++);
                result.add(post);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return result;
    }

    @Override
    public Post detail(String link) {
        Post post = new Post();
        SqlRuDateTimeParser parser = new SqlRuDateTimeParser();
        post.setLink(link);
        try {
            Document document = Jsoup.connect(link).get();
            Elements texts = document.select(".msgBody");
            post.setText(texts.eachText().get(1));
            Elements dates = document.select(".msgFooter");
            String date = dates.eachText().get(0);
            LocalDateTime session = parser.parse(date.substring(0, date.indexOf("[")));
            ZonedDateTime zdt = session.atZone(ZoneId.systemDefault());
            long millis = zdt.toInstant().toEpochMilli();
            post.setDate(new Date(millis));
            Elements names = document.select(".messageHeader");
            String name = names.eachText().get(0);
            post.setName(name.substring(0, name.indexOf("[")).trim());
        } catch (IOException e) {
            e.printStackTrace();
        }
        return post;
    }
}

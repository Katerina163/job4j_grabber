package ru.job4j.html;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import ru.job4j.utils.SqlRuDateTimeParser;

import java.util.ArrayList;
import java.util.List;

public class SqlRuParse {
    public static void main(String[] args) throws Exception {
        for (int i = 1; i < 6; i++) {
            Document doc = Jsoup.connect("https://www.sql.ru/forum/job-offers/" + i).get();
            Elements row = doc.select(".postslisttopic");
            for (Element td : row) {
                Element href = td.child(0);
                System.out.println(href.attr("href"));
                System.out.println(href.text());
            }
            Elements data = doc.select(".altCol");
            for (Element td : data) {
                if (!td.attr("style").isEmpty()) {
                    System.out.println(td.text());
                }
            }
        }
    }

    public static List<Post> load(int page) throws Exception {
        List<Post> result = new ArrayList<>(53);
        int id = 1;
        SqlRuDateTimeParser parser = new SqlRuDateTimeParser();
        for (int i = 1; i <= page; i++) {
            Document doc = Jsoup.connect("https://www.sql.ru/forum/job-offers/" + i).get();
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
                post.setCreated(parser.parse(date.substring(0, date.indexOf("[")).trim()));
                post.setName(href.text());
                post.setId(id++);
                result.add(post);
            }

        }
        return result;
    }
}

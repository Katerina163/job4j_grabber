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
        Post post = load("https://www.sql.ru/forum/1325330/lidy-be-fe-senior-cistemnye-analitiki-qa-i-devops-moskva-do-200t");
        System.out.println(post.getText());
        System.out.println(post.getCreated());
    }

    public static Post load(String html) throws Exception {
        Post post = new Post();
        SqlRuDateTimeParser parser = new SqlRuDateTimeParser();
        Document document = Jsoup.connect(html).get();
        Elements texts = document.select(".msgBody");
        post.setText(texts.eachText().get(1));
        Elements dates = document.select(".msgFooter");
        String date = dates.eachText().get(0);
        post.setCreated(parser.parse(date.substring(0, date.indexOf("[")).trim()));
        return post;
    }
}

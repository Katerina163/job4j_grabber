package ru.job4j.grabber;

import java.sql.Date;
import java.util.Objects;

public class Post {
    private int id;
    private String name;
    private String text;
    private String link;
    private Date date;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public String getLink() {
        return link;
    }

    public void setLink(String link) {
        this.link = link;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Post post = (Post) o;
        return Objects.equals(name, post.name) && Objects.equals(text, post.text)
                && Objects.equals(link, post.link) && Objects.equals(date, post.date);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, text, link, date);
    }
}

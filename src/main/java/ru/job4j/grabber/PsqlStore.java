package ru.job4j.grabber;

import java.io.*;
import java.sql.*;
import java.util.List;
import java.util.Properties;

public class PsqlStore implements Store, AutoCloseable {
    private Connection con;

    public PsqlStore(Properties cfg) {
        try {
            Class.forName(cfg.getProperty("driver"));
            con = DriverManager.getConnection(cfg.getProperty("url"),
                    cfg.getProperty("username"),cfg.getProperty("password"));
            } catch (Exception e) {
                throw new IllegalStateException(e);
            }
        }

    public static void main(String[] args) {
        Properties properties = new Properties();
        try (Reader in = new BufferedReader(
                new FileReader("./properties/app.properties"))) {
            properties.load(in);
        } catch (Exception e) {
            e.printStackTrace();
        }
        PsqlStore psqlStore = new PsqlStore(properties);
        Post post1 = new Post();
        post1.setName("name1");
        post1.setText("text1");
        post1.setLink("link1");
        post1.setDate(new Date(2021, 12, 12));
        Post post2 = new Post();
        post2.setName("name2");
        post2.setText("text2");
        post2.setLink("link2");
        post2.setDate(new Date(2020, 10, 10));
        psqlStore.save(post1);
        psqlStore.save(post2);
        System.out.println("findById: " + psqlStore.findById("1"));
    }

    @Override
    public void save(Post post) {
        try (PreparedStatement ps = con.prepareStatement(
                "insert into post(name, text, link, created) values(?, ?, ?, ?);")) {
            ps.setString(1, post.getName());
            ps.setString(2, post.getText());
            ps.setString(3, post.getLink());
            ps.setDate(4, post.getDate());
            ps.execute();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public List<Post> getAll() {
        List<Post> list = null;
        try (PreparedStatement ps = con.prepareStatement(
                "select * from post;")) {
            try(ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    Post post = new Post();
                    post.setId(rs.getInt("id"));
                    post.setName(rs.getString("name"));
                    post.setText(rs.getString("text"));
                    post.setLink(rs.getString("link"));
                    post.setDate(rs.getDate("created"));
                    list.add(post);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }

    @Override
    public Post findById(String id) {
        Post post = null;
        try (PreparedStatement ps = con.prepareStatement(
                "select * from post where id = ?;")) {
            ps.setString(1, id);
            try(ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    post.setId(rs.getInt("id"));
                    post.setName(rs.getString("name"));
                    post.setText(rs.getString("text"));
                    post.setLink(rs.getString("link"));
                    post.setDate(rs.getDate("created"));
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return post;
    }

    @Override
    public void close() throws Exception {
        if (con != null) {
            con.close();
        }
    }
}

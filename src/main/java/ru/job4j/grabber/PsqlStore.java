package ru.job4j.grabber;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

public class PsqlStore implements Store, AutoCloseable {
    private Connection con;

    public PsqlStore(Connection con) {
        this.con = con;
    }

    public PsqlStore(Properties cfg) {
        try {
            Class.forName(cfg.getProperty("driver"));
            con = DriverManager.getConnection(cfg.getProperty("url"),
                    cfg.getProperty("username"), cfg.getProperty("password"));
            } catch (Exception e) {
                throw new IllegalStateException(e);
            }
        }

    @Override
    public void save(Post post) {
        try (PreparedStatement ps = con.prepareStatement(
                "insert into post(name, text, link, created) values(?, ?, ?, ?);")) {
            ps.setString(1, post.getName());
            ps.setString(2, post.getText());
            ps.setString(3, post.getLink());
            ps.setTimestamp(4, Timestamp.valueOf(post.getDate()));
            ps.execute();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public List<Post> getAll() {
        List<Post> list = new ArrayList<>();
        try (PreparedStatement ps = con.prepareStatement(
                "select * from post;")) {
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    Post post = new Post();
                    post.setId(rs.getInt("id"));
                    post.setName(rs.getString("name"));
                    post.setText(rs.getString("text"));
                    post.setLink(rs.getString("link"));
                    post.setDate(rs.getTimestamp("created").toLocalDateTime());
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
        Post post = new Post();
        try (PreparedStatement ps = con.prepareStatement(
                "select * from post where id = ?;")) {
            ps.setString(1, id);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    post.setId(rs.getInt("id"));
                    post.setName(rs.getString("name"));
                    post.setText(rs.getString("text"));
                    post.setLink(rs.getString("link"));
                    post.setDate(rs.getTimestamp("created").toLocalDateTime());
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return post;
    }

    public boolean deleteAll() {
        boolean result = false;
        try (PreparedStatement ps = con.prepareStatement(
                "delete from post;")) {
            result = ps.executeUpdate() > 0;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }

    @Override
    public void close() throws Exception {
        if (con != null) {
            con.close();
        }
    }
}

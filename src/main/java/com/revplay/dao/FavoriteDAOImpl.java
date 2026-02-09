package com.revplay.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

import com.revplay.model.Song;
import com.revplay.util.DBConnection;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class FavoriteDAOImpl implements FavoriteDAO {
    private static final Logger logger = LogManager.getLogger(FavoriteDAOImpl.class);

    @Override
    public void addToFavorites(int userId, int songId) {

        if (isFavorite(userId, songId)) {
            System.out.println("⚠ Song is already in your favorites.");
            return;
        }

        String sql = "INSERT INTO favorites (user_id, song_id) VALUES (?, ?)";

        try (
                Connection con = DBConnection.getConnection();
                PreparedStatement ps = con.prepareStatement(sql);) {
            ps.setInt(1, userId);
            ps.setInt(2, songId);
            ps.executeUpdate();
            System.out.println("❤️ Added to favorites");
        } catch (java.sql.SQLIntegrityConstraintViolationException e) {
            // High level check just in case of race conditions
            System.out.println("⚠ Song is already in your favorites.");
        } catch (java.sql.SQLException e) {
            if (e.getErrorCode() == 1) { // ORA-00001
                System.out.println("⚠ Song is already in your favorites.");
            } else if (e.getErrorCode() == 2291) { // ORA-02291: integrity constraint violated - parent key not found
                System.out.println("❌ Song ID " + songId + " does not exist.");
            } else {
                logger.error("DB error in FavoriteDAOImpl.addToFavorites()", e);
            }
        } catch (Exception e) {
            logger.error("Error in FavoriteDAOImpl.addToFavorites()", e);
        }

    }

    @Override
    public List<Song> getFavoriteSongs(int userId) {

        List<Song> songs = new ArrayList<>();

        String sql = "SELECT s.song_id, s.title, s.genre, s.play_count FROM songs s JOIN favorites f ON s.song_id = f.song_id WHERE f.user_id = ?";

        try (
                Connection con = DBConnection.getConnection();
                PreparedStatement ps = con.prepareStatement(sql);) {

            ps.setInt(1, userId);
            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                Song s = new Song();
                s.setSongId(rs.getInt("song_id"));
                s.setTitle(rs.getString("title"));
                s.setGenre(rs.getString("genre"));
                s.setPlayCount(rs.getInt("play_count"));
                songs.add(s);
            }

        } catch (Exception e) {
            logger.error("DB error in FavoriteDAOImpl.getFavoriteSongs()", e);
        }

        return songs;
    }

    @Override
    public boolean isFavorite(int userId, int songId) {
        String sql = "SELECT 1 FROM favorites WHERE user_id = ? AND song_id = ?";
        try (
                Connection con = DBConnection.getConnection();
                PreparedStatement ps = con.prepareStatement(sql);) {
            ps.setInt(1, userId);
            ps.setInt(2, songId);
            ResultSet rs = ps.executeQuery();
            return rs.next();
        } catch (Exception e) {
            logger.error("DB error in FavoriteDAOImpl.isFavorite()", e);
        }
        return false;
    }
}

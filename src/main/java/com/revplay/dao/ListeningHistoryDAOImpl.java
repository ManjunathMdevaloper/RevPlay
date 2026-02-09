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

public class ListeningHistoryDAOImpl implements ListeningHistoryDAO {
	private static final Logger logger =
	        LogManager.getLogger(ListeningHistoryDAOImpl.class);

    @Override
    public void addHistory(int userId, int songId) {

        String sql =
            "INSERT INTO listening_history (history_id, user_id, song_id, played_at) " +
            "VALUES (history_seq.NEXTVAL, ?, ?, SYSDATE)";

        try (
            Connection con = DBConnection.getConnection();
            PreparedStatement ps = con.prepareStatement(sql);
        ) {

            ps.setInt(1, userId);
            ps.setInt(2, songId);
            ps.executeUpdate();

        } catch (Exception e) {
            logger.error("DB error while adding listening history. userId="
                    + userId + ", songId=" + songId, e);
   }

    }

    @Override
    public List<Song> getListeningHistory(int userId) {

        List<Song> songs = new ArrayList<>();

        String sql =
            "SELECT s.song_id, s.title, s.genre " +
            "FROM listening_history h " +
            "JOIN songs s ON h.song_id = s.song_id " +
            "WHERE h.user_id = ? " +
            "ORDER BY h.played_at DESC";

        try (
            Connection con = DBConnection.getConnection();
            PreparedStatement ps = con.prepareStatement(sql);
        ) {

            ps.setInt(1, userId);
            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                Song s = new Song();
                s.setSongId(rs.getInt("song_id"));
                s.setTitle(rs.getString("title"));
                s.setGenre(rs.getString("genre"));
                songs.add(s);
            }

        } catch (Exception e) {
            logger.error("DB error in ListeningHistoryDAOImpl.getListeningHistory()", e);
        }


        return songs;
    }
    @Override
    public List<Song> getRecentPlays(int userId, int limit) {

        List<Song> list = new ArrayList<>();

        String sql = "SELECT s.song_id, s.title, s.genre FROM listening_history h JOIN songs s ON h.song_id = s.song_id WHERE h.user_id = ? ORDER BY h.played_at DESC FETCH FIRST ? ROWS ONLY";

        try (
            Connection con = DBConnection.getConnection();
            PreparedStatement ps = con.prepareStatement(sql)
        ) {

            ps.setInt(1, userId);
            ps.setInt(2, limit);

            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                Song s = new Song();
                s.setSongId(rs.getInt("song_id"));
                s.setTitle(rs.getString("title"));
                s.setGenre(rs.getString("genre"));
                list.add(s);
            }

        } catch (Exception e) {
            logger.error("DB error in ListeningHistoryDAOImpl.getRecentPlays()", e);
        }


        return list;
    }


}

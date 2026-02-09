package com.revplay.dao;

import java.sql.*;
import java.util.*;

import com.revplay.model.Podcast;
import com.revplay.model.PodcastEpisode;
import com.revplay.util.DBConnection;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class PodcastDAOImpl implements PodcastDAO {
    private static final Logger logger = LogManager.getLogger(PodcastDAOImpl.class);

    @Override
    public List<Podcast> searchPodcasts(String keyword) {

        List<Podcast> list = new ArrayList<>();

        String sql = "SELECT * FROM podcasts WHERE LOWER(title) LIKE ? OR LOWER(host_name) LIKE ? OR LOWER(category) LIKE ? OR LOWER(description) LIKE ?";

        try (
                Connection con = DBConnection.getConnection();
                PreparedStatement ps = con.prepareStatement(sql)) {

            String key = "%" + keyword.toLowerCase() + "%";

            ps.setString(1, key);
            ps.setString(2, key);
            ps.setString(3, key);
            ps.setString(4, key);

            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                list.add(mapPodcast(rs));
            }

        } catch (Exception e) {
            logger.error("DB error in PodcastDAOImpl.searchPodcasts()", e);
        }

        return list;
    }

    @Override
    public List<Podcast> getAllPodcasts() {
        List<Podcast> list = new ArrayList<>();
        String sql = "SELECT * FROM podcasts";

        try (Connection con = DBConnection.getConnection();
                PreparedStatement ps = con.prepareStatement(sql);
                ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                list.add(mapPodcast(rs));
            }
        } catch (Exception e) {
            logger.error("DB error in PodcastDAOImpl.getAllPodcasts()", e);
        }
        return list;
    }

    @Override
    public List<PodcastEpisode> getEpisodesByPodcast(int podcastId) {
        List<PodcastEpisode> list = new ArrayList<>();
        String sql = "SELECT * FROM podcast_episodes WHERE podcast_id = ? ORDER BY release_date";
        try (Connection con = DBConnection.getConnection();
                PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, podcastId);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                list.add(mapEpisode(rs));
            }
        } catch (Exception e) {
            logger.error("DB error in PodcastDAOImpl.getEpisodesByPodcast()", e);
        }
        return list;
    }

    @Override
    public PodcastEpisode getEpisodeById(int episodeId) {
        String sql = "SELECT * FROM podcast_episodes WHERE episode_id = ?";
        try (Connection con = DBConnection.getConnection();
                PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, episodeId);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                return mapEpisode(rs);
            }
        } catch (Exception e) {
            logger.error("DB error in PodcastDAOImpl.getEpisodeById()", e);
        }
        return null;
    }

    @Override
    public void incrementEpisodePlayCount(int episodeId) {
        String sql = "UPDATE podcast_episodes SET play_count = play_count + 1 WHERE episode_id = ?";
        try (Connection con = DBConnection.getConnection();
                PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, episodeId);
            ps.executeUpdate();
        } catch (Exception e) {
            logger.error("DB error in PodcastDAOImpl.incrementEpisodePlayCount()", e);
        }
    }

    @Override
    public void createPodcast(Podcast podcast) {
        String sql = "INSERT INTO podcasts (podcast_id, title, host_name, category, description, created_at, artist_id) VALUES (podcasts_seq.NEXTVAL, ?, ?, ?, ?, CURRENT_DATE, ?)";
        try (Connection con = DBConnection.getConnection();
                PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, podcast.getTitle());
            ps.setString(2, podcast.getHostName());
            ps.setString(3, podcast.getCategory());
            ps.setString(4, podcast.getDescription());
            ps.setInt(5, podcast.getArtistId());
            ps.executeUpdate();
        } catch (Exception e) {
            logger.error("DB error in PodcastDAOImpl.createPodcast()", e);
        }
    }

    @Override
    public void addEpisode(PodcastEpisode episode) {
        String sql = "INSERT INTO podcast_episodes (episode_id, podcast_id, title, duration, release_date, play_count) VALUES (episodes_seq.NEXTVAL, ?, ?, ?, ?, 0)";
        try (Connection con = DBConnection.getConnection();
                PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, episode.getPodcastId());
            ps.setString(2, episode.getTitle());
            ps.setInt(3, episode.getDuration());
            ps.setDate(4, episode.getReleaseDate());
            ps.executeUpdate();
        } catch (Exception e) {
            logger.error("DB error in PodcastDAOImpl.addEpisode()", e);
        }
    }

    @Override
    public List<Podcast> getPodcastsByArtist(int artistId) {
        List<Podcast> list = new ArrayList<>();
        String sql = "SELECT * FROM podcasts WHERE artist_id = ?";
        try (Connection con = DBConnection.getConnection();
                PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, artistId);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                list.add(mapPodcast(rs));
            }
        } catch (Exception e) {
            logger.error("DB error in PodcastDAOImpl.getPodcastsByArtist()", e);
        }
        return list;
    }

    @Override
    public boolean updatePodcast(Podcast podcast) {
        String sql = "UPDATE podcasts SET title = ?, host_name = ?, category = ?, description = ? WHERE podcast_id = ? AND artist_id = ?";
        try (Connection con = DBConnection.getConnection();
                PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, podcast.getTitle());
            ps.setString(2, podcast.getHostName());
            ps.setString(3, podcast.getCategory());
            ps.setString(4, podcast.getDescription());
            ps.setInt(5, podcast.getPodcastId());
            ps.setInt(6, podcast.getArtistId());
            return ps.executeUpdate() > 0;
        } catch (Exception e) {
            logger.error("DB error in PodcastDAOImpl.updatePodcast()", e);
        }
        return false;
    }

    @Override
    public boolean deletePodcast(int podcastId, int artistId) {
        String deleteEpisodesSql = "DELETE FROM podcast_episodes WHERE podcast_id = ?";
        String deletePodcastSql = "DELETE FROM podcasts WHERE podcast_id = ? AND artist_id = ?";
        try (Connection con = DBConnection.getConnection()) {
            con.setAutoCommit(false);
            try {
                try (PreparedStatement ps1 = con.prepareStatement(deleteEpisodesSql)) {
                    ps1.setInt(1, podcastId);
                    ps1.executeUpdate();
                }
                try (PreparedStatement ps2 = con.prepareStatement(deletePodcastSql)) {
                    ps2.setInt(1, podcastId);
                    ps2.setInt(2, artistId);
                    int rows = ps2.executeUpdate();
                    if (rows > 0) {
                        con.commit();
                        return true;
                    }
                }
                con.rollback();
            } catch (Exception e) {
                con.rollback();
                throw e;
            } finally {
                con.setAutoCommit(true);
            }
        } catch (Exception e) {
            logger.error("DB error in PodcastDAOImpl.deletePodcast()", e);
        }
        return false;
    }

    @Override
    public boolean updateEpisode(PodcastEpisode episode) {
        String sql = "UPDATE podcast_episodes SET title = ?, duration = ?, release_date = ? WHERE episode_id = ?";
        try (Connection con = DBConnection.getConnection();
                PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, episode.getTitle());
            ps.setInt(2, episode.getDuration());
            ps.setDate(3, episode.getReleaseDate());
            ps.setInt(4, episode.getEpisodeId());
            return ps.executeUpdate() > 0;
        } catch (Exception e) {
            logger.error("DB error in PodcastDAOImpl.updateEpisode()", e);
        }
        return false;
    }

    @Override
    public boolean deleteEpisode(int episodeId) {
        String sql = "DELETE FROM podcast_episodes WHERE episode_id = ?";
        try (Connection con = DBConnection.getConnection();
                PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, episodeId);
            return ps.executeUpdate() > 0;
        } catch (Exception e) {
            logger.error("DB error in PodcastDAOImpl.deleteEpisode()", e);
        }
        return false;
    }

    private Podcast mapPodcast(ResultSet rs) throws SQLException {
        Podcast p = new Podcast();
        p.setPodcastId(rs.getInt("podcast_id"));
        p.setTitle(rs.getString("title"));
        p.setHostName(rs.getString("host_name"));
        p.setCategory(rs.getString("category"));
        p.setDescription(rs.getString("description"));
        p.setCreatedAt(rs.getDate("created_at"));
        try {
            p.setArtistId(rs.getInt("artist_id"));
        } catch (Exception e) {
            // artist_id might not exist yet in some environments
        }
        return p;
    }

    private PodcastEpisode mapEpisode(ResultSet rs) throws SQLException {
        PodcastEpisode e = new PodcastEpisode();
        e.setEpisodeId(rs.getInt("episode_id"));
        e.setPodcastId(rs.getInt("podcast_id"));
        e.setTitle(rs.getString("title"));
        e.setDuration(rs.getInt("duration"));
        e.setReleaseDate(rs.getDate("release_date"));
        e.setPlayCount(rs.getInt("play_count"));
        return e;
    }
}

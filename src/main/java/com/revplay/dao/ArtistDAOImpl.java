package com.revplay.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

import com.revplay.model.Album;
import com.revplay.model.Artist;
import com.revplay.model.Song;
import com.revplay.util.DBConnection;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class ArtistDAOImpl implements ArtistDAO {
    private static final Logger logger = LogManager.getLogger(ArtistDAOImpl.class);

    @Override
    public List<Artist> getAllArtists() {

        List<Artist> artists = new ArrayList<>();

        String sql = "SELECT a.artist_id, a.user_id, a.bio, a.genre, a.social_links, u.name " +
                "FROM artists a JOIN users u ON a.user_id = u.user_id";

        try (
                Connection con = DBConnection.getConnection();
                PreparedStatement ps = con.prepareStatement(sql);
                ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                Artist artist = new Artist();
                artist.setArtistId(rs.getInt("artist_id"));
                artist.setUserId(rs.getInt("user_id"));
                artist.setBio(rs.getString("bio"));
                artist.setGenre(rs.getString("genre"));
                artist.setSocialLinks(rs.getString("social_links"));
                artist.setArtistName(rs.getString("name"));

                artists.add(artist);
            }
        } catch (Exception e) {
            logger.error("DB error in ArtistDAOImpl.getAllArtists()", e);
        }

        return artists;
    }

    @Override
    public List<Album> getAllAlbums() {

        List<Album> albums = new ArrayList<>();

        String sql = "SELECT album_id, title, release_date, artist_id FROM albums";

        try (
                Connection con = DBConnection.getConnection();
                PreparedStatement ps = con.prepareStatement(sql);
                ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                Album album = new Album();
                album.setAlbumId(rs.getInt("album_id"));
                album.setTitle(rs.getString("title"));
                album.setReleaseDate(rs.getDate("release_date"));
                album.setArtistId(rs.getInt("artist_id"));

                albums.add(album);
            }
        } catch (Exception e) {
            logger.error("DB error in ArtistDAOImpl.getAllAlbums()", e);
        }
        return albums;
    }

    @Override
    public Artist getArtistByUserId(int userId) {

        String sql = "SELECT a.artist_id, a.user_id, a.bio, a.genre, a.social_links, u.name " +
                "FROM artists a JOIN users u ON a.user_id = u.user_id " +
                "WHERE a.user_id = ?";
        Artist artist = null;

        try (
                Connection con = DBConnection.getConnection();
                PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, userId);
            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                artist = new Artist();
                artist.setArtistId(rs.getInt("artist_id"));
                artist.setUserId(rs.getInt("user_id"));
                artist.setBio(rs.getString("bio"));
                artist.setGenre(rs.getString("genre"));
                artist.setSocialLinks(rs.getString("social_links"));
                artist.setArtistName(rs.getString("name"));
            }

        } catch (Exception e) {
            logger.error("DB error in ArtistDAOImpl.getArtistByUserId()", e);
        }

        return artist;
    }

    @Override
    public void saveOrUpdateArtist(Artist artist) {

        System.out.println(
                "DEBUG DAO: saveOrUpdateArtist userId=" + artist.getUserId());

        String selectSql = "SELECT artist_id FROM artists WHERE user_id = ?";

        String insertSql = "INSERT INTO artists (artist_id, user_id, bio, genre, social_links) " +
                "VALUES (artists_seq.NEXTVAL, ?, ?, ?, ?)";

        String updateSql = "UPDATE artists SET bio = ?, genre = ?, social_links = ? " +
                "WHERE artist_id = ?";

        try (Connection con = DBConnection.getConnection()) {

            // 🔹 1. Check existing artist row
            PreparedStatement selectPs = con.prepareStatement(selectSql);
            selectPs.setInt(1, artist.getUserId());

            ResultSet rs = selectPs.executeQuery();

            if (rs.next()) {
                // 🔹 2. UPDATE using artist_id (PK)
                int artistId = rs.getInt("artist_id");

                PreparedStatement updatePs = con.prepareStatement(updateSql);
                updatePs.setString(1, artist.getBio());
                updatePs.setString(2, artist.getGenre());
                updatePs.setString(3, artist.getSocialLinks());
                updatePs.setInt(4, artistId);

                int rows = updatePs.executeUpdate();
                System.out.println(
                        "DEBUG DAO: UPDATE artist_id=" + artistId +
                                " rows=" + rows);

            } else {
                // 🔹 3. INSERT (first time)
                PreparedStatement insertPs = con.prepareStatement(insertSql);
                insertPs.setInt(1, artist.getUserId());
                insertPs.setString(2, artist.getBio());
                insertPs.setString(3, artist.getGenre());
                insertPs.setString(4, artist.getSocialLinks());

                int rows = insertPs.executeUpdate();
                System.out.println(
                        "DEBUG DAO: INSERT rows=" + rows);
            }

        } catch (Exception e) {
            logger.error("DB error in ArtistDAOImpl.saveOrUpdateArtist()", e);
        }
    }

    @Override
    public List<Song> getSongsByArtist(int artistId) {

        List<Song> songs = new ArrayList<>();

        String sql = "SELECT song_id, title, genre, duration, album_id, play_count " +
                "FROM songs WHERE artist_id = ?";

        try (
                Connection con = DBConnection.getConnection();
                PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, artistId);
            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                Song song = new Song();
                song.setSongId(rs.getInt("song_id"));
                song.setTitle(rs.getString("title"));
                song.setGenre(rs.getString("genre"));
                song.setDuration(rs.getInt("duration"));
                song.setAlbumId(rs.getInt("album_id"));
                song.setPlayCount(rs.getInt("play_count"));

                songs.add(song);
            }

        } catch (Exception e) {
            logger.error("DB error in ArtistDAOImpl.getSongsByArtist()", e);
        }
        return songs;
    }

}

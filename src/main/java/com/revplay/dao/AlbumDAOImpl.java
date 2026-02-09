package com.revplay.dao;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

import com.revplay.main.RevPlayApp;
import com.revplay.model.Album;
import com.revplay.util.DBConnection;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class AlbumDAOImpl implements AlbumDAO {
	private static final Logger logger =
	        LogManager.getLogger(AlbumDAOImpl.class);

	@Override
	public List<Album> getAllAlbums() {

	    List<Album> albums = new ArrayList<>();

	    String sql = "SELECT album_id, title, release_date, artist_id FROM albums";

	    try (
	        Connection con = DBConnection.getConnection();
	        PreparedStatement ps = con.prepareStatement(sql);
	        ResultSet rs = ps.executeQuery()
	    ) {
	        while (rs.next()) {
	            Album album = new Album();
	            album.setAlbumId(rs.getInt("album_id"));
	            album.setTitle(rs.getString("title"));
	            album.setReleaseDate(rs.getDate("release_date"));
	            album.setArtistId(rs.getInt("artist_id"));

	            albums.add(album);
	        }
	    } catch (Exception e) {
	        logger.error("DB error while fetching Albums", e);
	    }


	    return albums;
	}
	@Override
	public boolean hasSongs(int albumId) {

	    String sql = "SELECT COUNT(*) FROM songs WHERE album_id = ?";

	    try (
	        Connection con = DBConnection.getConnection();
	        PreparedStatement ps = con.prepareStatement(sql)
	    ) {
	        ps.setInt(1, albumId);
	        ResultSet rs = ps.executeQuery();

	        if (rs.next()) {
	            return rs.getInt(1) > 0;
	        }
	    } catch (Exception e) {
	        logger.error("DB error in AlbumDAOImpl.hasSongs()", e);
	    }

	    return false;
	}
	@Override
	public void updateAlbum(Album album) {

	    String sql = "UPDATE albums SET title = ?, release_date = ? WHERE album_id = ? AND artist_id = ?";

	    try (
	        Connection con = DBConnection.getConnection();
	        PreparedStatement ps = con.prepareStatement(sql)
	    ) {
	        ps.setString(1, album.getTitle());
	        ps.setDate(2, album.getReleaseDate());
	        ps.setInt(3, album.getAlbumId());
	        ps.setInt(4, album.getArtistId());

	        int rows = ps.executeUpdate();
	        if (rows == 0) {
	            System.out.println("❌ Album update failed (not your album)");
	        }

	    } catch (Exception e) {
	        logger.error("DB error in AlbumDAOImpl.updateAlbum()", e);
	    }

	}
	@Override
	public void deleteSongsByAlbum(int albumId) {

	    String sql = "DELETE FROM songs WHERE album_id = ?";

	    try (
	        Connection con = DBConnection.getConnection();
	        PreparedStatement ps = con.prepareStatement(sql)
	    ) {
	        ps.setInt(1, albumId);
	        ps.executeUpdate();

	    } catch (Exception e) {
	        logger.error("DB error in AlbumDAOImpl.deleteSongsByAlbum()", e);
	    }

	}
	@Override
	public void deleteFavoritesByAlbum(int albumId) {

	    String sql = "DELETE FROM favorites WHERE song_id IN ( SELECT song_id FROM songs WHERE album_id = ?)";

	    try (Connection con = DBConnection.getConnection();
	         PreparedStatement ps = con.prepareStatement(sql)) {
	        ps.setInt(1, albumId);
	        ps.executeUpdate();
	    } catch (Exception e) {
	        logger.error("DB error in AlbumDAOImpl.deleteFavoritesByAlbum()", e);
	    }

	}

	@Override
	public void deletePlaylistSongsByAlbum(int albumId) {

	    String sql = "DELETE FROM playlist_songs WHERE song_id IN ( SELECT song_id FROM songs WHERE album_id = ? )";

	    try (Connection con = DBConnection.getConnection();
	         PreparedStatement ps = con.prepareStatement(sql)) {
	        ps.setInt(1, albumId);
	        ps.executeUpdate();
	    } catch (Exception e) {
	        logger.error("DB error in AlbumDAOImpl.deletePlaylistSongsByAlbum()", e);
	    }

	}

	@Override
	public void deleteListeningHistoryByAlbum(int albumId) {

	    String sql = "DELETE FROM listening_history WHERE song_id IN ( SELECT song_id FROM songs WHERE album_id = ?)";

	    try (Connection con = DBConnection.getConnection();
	         PreparedStatement ps = con.prepareStatement(sql)) {
	        ps.setInt(1, albumId);
	        ps.executeUpdate();
	    } catch (Exception e) {
	        logger.error("DB error in AlbumDAOImpl.deleteListeningHistoryByAlbum()", e);
	    }

	}

	@Override
	public void deleteAlbum(int albumId, int artistId) {

	    String sql = "DELETE FROM albums WHERE album_id = ? AND artist_id = ?";

	    try (
	        Connection con = DBConnection.getConnection();
	        PreparedStatement ps = con.prepareStatement(sql)
	    ) {
	        ps.setInt(1, albumId);
	        ps.setInt(2, artistId);

	        int rows = ps.executeUpdate();
	        if (rows == 0) {
	            System.out.println("❌ Album delete failed (not your album)");
	        }

	    } catch (Exception e) {
	        logger.error("DB error in AlbumDAOImpl.deleteAlbum()", e);
	    }

	}

    @Override
    public void createAlbum(Album album) {

        String sql = "INSERT INTO albums (album_id, title, release_date, artist_id) VALUES (albums_seq.NEXTVAL, ?, ?, ?)";

        try (
            Connection con = DBConnection.getConnection();
            PreparedStatement ps = con.prepareStatement(sql)
        ) {

            ps.setString(1, album.getTitle());
            ps.setDate(2, album.getReleaseDate());
            ps.setInt(3, album.getArtistId());

            ps.executeUpdate();

        } catch (Exception e) {
            logger.error("DB error while creating album: " + album.getTitle(), e);
        }

    }

    @Override
    public List<Album> getAlbumsByArtist(int artistId) {

        List<Album> list = new ArrayList<>();
        String sql = "SELECT * FROM albums WHERE artist_id = ?";

        try (
            Connection con = DBConnection.getConnection();
            PreparedStatement ps = con.prepareStatement(sql)
        ) {

            ps.setInt(1, artistId);
            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                Album a = new Album();
                a.setAlbumId(rs.getInt("album_id"));
                a.setTitle(rs.getString("title"));
                a.setReleaseDate(rs.getDate("release_date"));
                a.setArtistId(rs.getInt("artist_id"));
                list.add(a);
            }

        } catch (Exception e) {
            logger.error("DB error in AlbumDAOImpl.getAlbumsByArtist()", e);
        }


        return list;
    }
}

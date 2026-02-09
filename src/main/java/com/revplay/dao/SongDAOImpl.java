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

public class SongDAOImpl implements SongDAO {
	private static final Logger logger = LogManager.getLogger(SongDAOImpl.class);

	@Override
	public List<Song> getAllSongs() {

		List<Song> songs = new ArrayList<>();
		String sql = "SELECT * FROM songs ORDER BY song_id";

		try (Connection con = DBConnection.getConnection();
				PreparedStatement ps = con.prepareStatement(sql);
				ResultSet rs = ps.executeQuery();) {

			while (rs.next()) {
				Song song = new Song();
				song.setSongId(rs.getInt("song_id"));
				song.setTitle(rs.getString("title"));
				song.setGenre(rs.getString("genre"));
				song.setDuration(rs.getInt("duration"));
				song.setPlayCount(rs.getInt("play_count"));
				song.setArtistId(rs.getInt("artist_id"));
				song.setAlbumId(rs.getInt("album_id"));

				songs.add(song);
			}

		} catch (Exception e) {
			logger.error("DB error in SongDAOImpl.getAllSongs()", e);
		}

		return songs;
	}

	@Override
	public Song getSongById(int songId) {

		Song song = null;
		String sql = "SELECT * FROM songs WHERE song_id = ?";

		try (Connection con = DBConnection.getConnection(); PreparedStatement ps = con.prepareStatement(sql);) {

			ps.setInt(1, songId);
			ResultSet rs = ps.executeQuery();

			if (rs.next()) {
				song = new Song();
				song.setSongId(rs.getInt("song_id"));
				song.setTitle(rs.getString("title"));
				song.setGenre(rs.getString("genre"));
				song.setDuration(rs.getInt("duration"));
				song.setPlayCount(rs.getInt("play_count"));
				song.setArtistId(rs.getInt("artist_id"));
				song.setAlbumId(rs.getInt("album_id"));
			}

		} catch (Exception e) {
			logger.error("DB error in SongDAOImpl.getSongById()", e);
		}

		return song;
	}

	@Override
	public void incrementPlayCount(int songId) {

		String sql = "UPDATE songs SET play_count = play_count + 1 WHERE song_id = ?";

		try (Connection con = DBConnection.getConnection(); PreparedStatement ps = con.prepareStatement(sql);) {

			ps.setInt(1, songId);
			ps.executeUpdate();

		} catch (Exception e) {
			logger.error("DB error in SongDAOImpl.incrementPlayCount()", e);
		}
	}

	@Override
	public List<Song> searchSongs(String keyword) {

		List<Song> songs = new ArrayList<>();

		String sql = "SELECT * FROM songs WHERE LOWER(title) LIKE ? OR LOWER(genre) LIKE ?";

		try (Connection con = DBConnection.getConnection(); PreparedStatement ps = con.prepareStatement(sql);) {

			ps.setString(1, "%" + keyword.toLowerCase() + "%");
			ps.setString(2, "%" + keyword.toLowerCase() + "%");

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
			logger.error("DB error in SongDAOImpl.searchSongs()", e);
		}

		return songs;
	}

	@Override
	public List<Song> getSongsByGenre(String genre) {

		List<Song> songs = new ArrayList<>();

		String sql = "SELECT * FROM songs WHERE LOWER(genre) = ?";

		try (Connection con = DBConnection.getConnection(); PreparedStatement ps = con.prepareStatement(sql);) {

			ps.setString(1, genre.toLowerCase());
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
			logger.error("DB error in SongDAOImpl.getSongsByGenre()", e);
		}

		return songs;
	}

	@Override
	public void uploadSong(Song song) {

		String sql = "INSERT INTO songs (song_id, title, genre, duration, release_date, play_count, artist_id, album_id) VALUES (songs_seq.NEXTVAL, ?, ?, ?, SYSDATE, 0, ?, ?)";

		try (Connection con = DBConnection.getConnection(); PreparedStatement ps = con.prepareStatement(sql);) {
			ps.setString(1, song.getTitle());
			ps.setString(2, song.getGenre());
			ps.setInt(3, song.getDuration());
			ps.setInt(4, song.getArtistId());
			ps.setInt(5, song.getAlbumId());
			ps.executeUpdate();
		} catch (Exception e) {
			logger.error("DB error in SongDAOImpl.uploadSong()", e);
		}

	}

	@Override
	public List<Song> getSongsByArtist(int artistId) {

		List<Song> songs = new ArrayList<>();

		String sql = "SELECT song_id, title, genre, duration, play_count FROM songs WHERE artist_id = ?";

		try (Connection con = DBConnection.getConnection(); PreparedStatement ps = con.prepareStatement(sql)) {

			ps.setInt(1, artistId);
			ResultSet rs = ps.executeQuery();

			while (rs.next()) {
				Song song = new Song();
				song.setSongId(rs.getInt("song_id"));
				song.setTitle(rs.getString("title"));
				song.setGenre(rs.getString("genre"));
				song.setDuration(rs.getInt("duration"));
				song.setPlayCount(rs.getInt("play_count"));

				songs.add(song);
			}

		} catch (Exception e) {
			logger.error("DB error in SongDAOImpl.getSongsByArtist()", e);
		}

		return songs;
	}

	@Override
	public List<Song> getSongsByAlbum(int albumId) {

		List<Song> songs = new ArrayList<>();

		String sql = "SELECT song_id, title, genre, duration, play_count FROM songs WHERE album_id = ?";

		try (Connection con = DBConnection.getConnection(); PreparedStatement ps = con.prepareStatement(sql)) {
			ps.setInt(1, albumId);
			ResultSet rs = ps.executeQuery();

			while (rs.next()) {
				Song song = new Song();
				song.setSongId(rs.getInt("song_id"));
				song.setTitle(rs.getString("title"));
				song.setGenre(rs.getString("genre"));
				song.setDuration(rs.getInt("duration"));
				song.setPlayCount(rs.getInt("play_count"));

				songs.add(song);
			}
		} catch (Exception e) {
			logger.error("DB error in SongDAOImpl.getSongsByAlbum()", e);
		}
		return songs;
	}

	@Override
	public List<String> getAllGenres() {
		List<String> genres = new ArrayList<>();
		String sql = "SELECT DISTINCT genre FROM songs WHERE genre IS NOT NULL";

		try (Connection con = DBConnection.getConnection();
				PreparedStatement ps = con.prepareStatement(sql);
				ResultSet rs = ps.executeQuery()) {

			while (rs.next()) {
				genres.add(rs.getString("genre"));
			}
		} catch (Exception e) {
			logger.error("DB error in SongDAOImpl.getAllGenres()", e);
		}
		return genres;
	}
}

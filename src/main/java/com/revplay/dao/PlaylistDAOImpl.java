package com.revplay.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

import com.revplay.model.Playlist;
import com.revplay.model.Song;
import com.revplay.util.DBConnection;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class PlaylistDAOImpl implements PlaylistDAO {
	private static final Logger logger = LogManager.getLogger(PlaylistDAOImpl.class);

	@Override
	public void createPlaylist(Playlist playlist) {

		String sql = "INSERT INTO playlists VALUES (playlists_seq.NEXTVAL, ?, ?, ?, ?)";

		try (Connection con = DBConnection.getConnection(); PreparedStatement ps = con.prepareStatement(sql);) {

			ps.setString(1, playlist.getName());
			ps.setString(2, playlist.getDescription());
			ps.setString(3, playlist.getIsPublic());
			ps.setInt(4, playlist.getUserId());

			ps.executeUpdate();

		} catch (Exception e) {
			logger.error("DB error in PlaylistDAOImpl.createPlaylist()", e);
		}

	}

	@Override
	public List<Playlist> getMyPlaylistsWithSongCount(int userId) {

		List<Playlist> list = new ArrayList<>();

		String sql = "SELECT p.playlist_id, p.name, p.is_public, COUNT(ps.song_id) AS song_count FROM playlists p LEFT JOIN playlist_songs ps ON p.playlist_id = ps.playlist_id WHERE p.user_id = ? GROUP BY p.playlist_id, p.name, p.is_public";

		try (Connection con = DBConnection.getConnection();
				PreparedStatement ps = con.prepareStatement(sql)) {

			ps.setInt(1, userId);
			ResultSet rs = ps.executeQuery();

			while (rs.next()) {
				Playlist p = new Playlist();
				p.setPlaylistId(rs.getInt("playlist_id"));
				p.setName(rs.getString("name"));
				p.setIsPublic(rs.getString("is_public"));
				p.setSongCount(rs.getInt("song_count")); // 🔥 NEW
				list.add(p);
			}
		} catch (Exception e) {
			logger.error("DB error in PlaylistDAOImpl.getMyPlaylistsWithSongCount()", e);
		}

		return list;
	}

	@Override
	public List<Song> getSongsInPlaylist(int playlistId) {

		List<Song> list = new ArrayList<>();

		String sql = "SELECT s.song_id, s.title FROM playlist_songs ps JOIN songs s ON ps.song_id = s.song_id WHERE ps.playlist_id = ?";

		try (Connection con = DBConnection.getConnection();
				PreparedStatement ps = con.prepareStatement(sql)) {

			ps.setInt(1, playlistId);
			ResultSet rs = ps.executeQuery();

			while (rs.next()) {
				Song s = new Song();
				s.setSongId(rs.getInt("song_id"));
				s.setTitle(rs.getString("title"));
				list.add(s);
			}
		} catch (Exception e) {
			logger.error("DB error in PlaylistDAOImpl.getSongsInPlaylist()", e);
		}

		return list;
	}

	@Override
	public List<Playlist> getPlaylistsByUser(int userId) {

		List<Playlist> playlists = new ArrayList<>();
		String sql = "SELECT * FROM playlists WHERE user_id = ?";

		try (Connection con = DBConnection.getConnection(); PreparedStatement ps = con.prepareStatement(sql);) {

			ps.setInt(1, userId);
			ResultSet rs = ps.executeQuery();

			while (rs.next()) {
				Playlist p = new Playlist();
				p.setPlaylistId(rs.getInt("playlist_id"));
				p.setName(rs.getString("name"));
				p.setDescription(rs.getString("description"));
				p.setIsPublic(rs.getString("is_public"));
				p.setUserId(rs.getInt("user_id"));

				playlists.add(p);
			}

		} catch (Exception e) {
			logger.error("DB error in PlaylistDAOImpl.getPlaylistsByUser()", e);
		}

		return playlists;
	}

	@Override
	public boolean addSongToPlaylist(int playlistId, int songId) {

		String sql = "INSERT INTO playlist_songs VALUES (?, ?)";

		try (Connection con = DBConnection.getConnection(); PreparedStatement ps = con.prepareStatement(sql);) {

			ps.setInt(1, playlistId);
			ps.setInt(2, songId);
			int rows = ps.executeUpdate();
			return rows > 0;

		} catch (Exception e) {
			// Duplicate key or other DB error
		}
		return false;
	}

	@Override
	public boolean removeSongFromPlaylist(int playlistId, int songId) {

		String sql = "DELETE FROM playlist_songs WHERE playlist_id = ? AND song_id = ?";

		try (Connection con = DBConnection.getConnection(); PreparedStatement ps = con.prepareStatement(sql);) {

			ps.setInt(1, playlistId);
			ps.setInt(2, songId);
			int rows = ps.executeUpdate();
			return rows > 0;

		} catch (Exception e) {
			logger.error("DB error in PlaylistDAOImpl.removeSongFromPlaylist()", e);
		}
		return false;
	}

	@Override
	public void updatePlaylist(Playlist playlist) {

		String sql = "UPDATE playlists SET name = ?, description = ?, is_public = ? WHERE playlist_id = ? AND user_id = ?";

		try (Connection con = DBConnection.getConnection(); PreparedStatement ps = con.prepareStatement(sql)) {

			ps.setString(1, playlist.getName());
			ps.setString(2, playlist.getDescription());
			ps.setString(3, playlist.getIsPublic());
			ps.setInt(4, playlist.getPlaylistId());
			ps.setInt(5, playlist.getUserId());

			int rows = ps.executeUpdate();

			if (rows == 0) {
				System.out.println("❌ Playlist not found or access denied");
			}

		} catch (Exception e) {
			logger.error("DB error in PlaylistDAOImpl.updatePlaylist()", e);
		}

	}

	@Override
	public boolean deletePlaylist(int playlistId, int userId) {

		String deleteSongsSql = "DELETE FROM playlist_songs WHERE playlist_id = ?";
		String deletePlaylistSql = "DELETE FROM playlists WHERE playlist_id = ? AND user_id = ?";

		try (Connection con = DBConnection.getConnection()) {
			con.setAutoCommit(false); // Transactions for safety
			try {
				// 1️⃣ Delete songs from playlist
				try (PreparedStatement ps1 = con.prepareStatement(deleteSongsSql)) {
					ps1.setInt(1, playlistId);
					ps1.executeUpdate();
				}

				// 2️⃣ Delete playlist itself
				try (PreparedStatement ps2 = con.prepareStatement(deletePlaylistSql)) {
					ps2.setInt(1, playlistId);
					ps2.setInt(2, userId);

					int rows = ps2.executeUpdate();
					if (rows > 0) {
						con.commit();
						return true;
					} else {
						con.rollback();
						return false;
					}
				}
			} catch (Exception e) {
				con.rollback();
				logger.error("Error during delete playlist transaction", e);
				return false;
			} finally {
				con.setAutoCommit(true);
			}

		} catch (Exception e) {
			logger.error("DB connection error in PlaylistDAOImpl.deletePlaylist()", e);
		}
		return false;
	}

	@Override
	public List<Playlist> getPublicPlaylists(int currentUserId) {

		List<Playlist> list = new ArrayList<>();

		String sql = "SELECT p.playlist_id, p.name, p.description, p.is_public, u.name AS owner FROM playlists p JOIN users u ON p.user_id = u.user_id WHERE p.is_public IN ('Y','y') AND p.user_id != ?";

		try (Connection con = DBConnection.getConnection(); PreparedStatement ps = con.prepareStatement(sql)) {

			ps.setInt(1, currentUserId);
			ResultSet rs = ps.executeQuery();

			while (rs.next()) {
				Playlist p = new Playlist();
				p.setPlaylistId(rs.getInt("playlist_id"));
				p.setName(rs.getString("name"));
				p.setDescription(rs.getString("description"));
				p.setIsPublic(rs.getString("is_public"));
				p.setOwnerName(rs.getString("owner")); // transient field
				list.add(p);
			}

		} catch (Exception e) {
			logger.error("DB error in PlaylistDAOImpl.getPublicPlaylists()", e);
		}

		return list;
	}

}

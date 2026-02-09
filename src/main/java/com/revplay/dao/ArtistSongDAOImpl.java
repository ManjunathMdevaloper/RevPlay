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

public class ArtistSongDAOImpl implements ArtistSongDAO {
	private static final Logger logger =
	        LogManager.getLogger(ArtistSongDAOImpl.class);

	@Override
	public List<String> getSongStatistics(int artistId) {

	    List<String> stats = new ArrayList<>();

	    String sql = "SELECT s.song_id, s.title, s.play_count, COUNT(f.song_id) AS fav_count FROM songs s LEFT JOIN favorites f ON s.song_id = f.song_id WHERE s.artist_id = ? GROUP BY s.song_id, s.title, s.play_count ORDER BY s.song_id";

	    try (
	        Connection con = DBConnection.getConnection();
	        PreparedStatement ps = con.prepareStatement(sql)
	    ) {

	        ps.setInt(1, artistId);
	        ResultSet rs = ps.executeQuery();

	        while (rs.next()) {
	            String row =
	                rs.getInt("song_id") + " | " +
	                rs.getString("title") + " | Plays: " +
	                rs.getInt("play_count") + " | Favorites: " +
	                rs.getInt("fav_count");

	            stats.add(row);
	        }

	    } catch (Exception e) {
	        logger.error("DB error in ArtistSongDAOImpl.getSongStatistics()", e);
	    }


	    return stats;
	}

@Override
public List<Song> getSongsByArtist(int artistId) {

    List<Song> songs = new ArrayList<>();

    String sql =
        "SELECT song_id, title, genre, duration, album_id, play_count " +
        "FROM songs WHERE artist_id = ?";

    try (
        Connection con = DBConnection.getConnection();
        PreparedStatement ps = con.prepareStatement(sql)
    ) {

        ps.setInt(1, artistId);
        ResultSet rs = ps.executeQuery();

        while (rs.next()) {

            Song song = new Song();

            song.setSongId(rs.getInt("song_id"));
            song.setTitle(rs.getString("title"));
            song.setGenre(rs.getString("genre"));
            song.setDuration(rs.getInt("duration"));

            // 🔥 THIS LINE IS THE FIX
            song.setAlbumId(rs.getInt("album_id"));

            song.setPlayCount(rs.getInt("play_count"));

            songs.add(song);
        }

    } catch (Exception e) {
        logger.error("DB error in ArtistSongDAOImpl.getSongsByArtist()", e);
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

		}catch (Exception e) {
		    logger.error("DB error in ArtistSongDAOImpl.uploadSong()", e);
		}

	}
	@Override
	public boolean isSongOwnedByArtist(int songId, int artistId) {

	    String sql =
	        "SELECT 1 FROM songs WHERE song_id = ? AND artist_id = ?";

	    try (
	        Connection con = DBConnection.getConnection();
	        PreparedStatement ps = con.prepareStatement(sql)
	    ) {

	        ps.setInt(1, songId);
	        ps.setInt(2, artistId);

	        ResultSet rs = ps.executeQuery();
	        return rs.next();

	    } catch (Exception e) {
	        logger.error("DB error in ArtistSongDAOImpl.isSongOwnedByArtist()", e);
	    }

	    return false;
	}

	@Override
	public void updateSong(Song song) {

		String sql = "UPDATE songs SET title = ?, genre = ?, duration = ?, album_id = ? WHERE song_id = ? AND artist_id = ?";

		try (Connection con = DBConnection.getConnection(); PreparedStatement ps = con.prepareStatement(sql)) {
			ps.setString(1, song.getTitle());
			ps.setString(2, song.getGenre());
			ps.setInt(3, song.getDuration());
			ps.setInt(4, song.getAlbumId());
			ps.setInt(5, song.getSongId());
			ps.setInt(6, song.getArtistId());

			int rows = ps.executeUpdate();

			if (rows == 0) {
				System.out.println("❌ Update failed (not your song)");
			}

		} catch (Exception e) {
		    logger.error("DB error in ArtistSongDAOImpl.updateSong()", e);
		}

	}

@Override
public boolean deleteSong(int songId, int artistId) {

    String deleteFavorites =
        "DELETE FROM favorites WHERE song_id = ?";

    String deletePlaylistSongs =
        "DELETE FROM playlist_songs WHERE song_id = ?";

    String deleteHistory =
        "DELETE FROM listening_history WHERE song_id = ?";

    String deleteSong =
        "DELETE FROM songs WHERE song_id = ? AND artist_id = ?";

    try (Connection con = DBConnection.getConnection()) {

        con.setAutoCommit(false); // 🔒 TRANSACTION START

        try (PreparedStatement ps1 = con.prepareStatement(deleteFavorites);
             PreparedStatement ps2 = con.prepareStatement(deletePlaylistSongs);
             PreparedStatement ps3 = con.prepareStatement(deleteHistory);
             PreparedStatement ps4 = con.prepareStatement(deleteSong)) {

            ps1.setInt(1, songId);
            ps1.executeUpdate();

            ps2.setInt(1, songId);
            ps2.executeUpdate();

            ps3.setInt(1, songId);
            ps3.executeUpdate();

            ps4.setInt(1, songId);
            ps4.setInt(2, artistId);

            int rows = ps4.executeUpdate();

            con.commit(); // ✅ TRANSACTION END

            return rows > 0;
        }

    } catch (Exception e) {
        logger.error("DB error in ArtistSongDAOImpl.deleteSong()", e);
    }

    return false;
}

	@Override
	public List<String> getUsersWhoFavoritedMySongs(int artistId) {

		List<String> result = new ArrayList<>();

		String sql = "SELECT s.title AS song_title, u.name AS user_name, u.email AS user_email FROM favorites f JOIN songs s ON f.song_id = s.song_id JOIN users u ON f.user_id = u.user_id WHERE s.artist_id = ? ORDER BY s.title";

		try (Connection con = DBConnection.getConnection(); PreparedStatement ps = con.prepareStatement(sql)) {

			ps.setInt(1, artistId);
			ResultSet rs = ps.executeQuery();

			while (rs.next()) {
				String row = "🎵 " + rs.getString("song_title") + " | ❤️ By: " + rs.getString("user_name") + " ("
						+ rs.getString("user_email") + ")";
				result.add(row);
			}

		} catch (Exception e) {
		    logger.error("DB error in ArtistSongDAOImpl.getUsersWhoFavoritedMySongs()", e);
		}


		return result;
	}

}

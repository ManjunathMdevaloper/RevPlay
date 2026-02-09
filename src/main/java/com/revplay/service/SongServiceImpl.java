package com.revplay.service;

import java.util.List;

import com.revplay.dao.ListeningHistoryDAO;
import com.revplay.dao.ListeningHistoryDAOImpl;
import com.revplay.dao.SongDAO;
import com.revplay.dao.SongDAOImpl;
import com.revplay.model.Song;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class SongServiceImpl implements SongService {

	private SongDAO songDAO = new SongDAOImpl();
	private ListeningHistoryDAO historyDAO = new ListeningHistoryDAOImpl();
	private static final Logger logger = LogManager.getLogger(SongServiceImpl.class);

	@Override
	public List<Song> getAllSongs() {
		return songDAO.getAllSongs();
	}

	@Override
	public Song playSong(int userId, int songId) {

		logger.info("User " + userId + " requested to play song " + songId);

		Song song = songDAO.getSongById(songId);

		if (song == null) {
			logger.warn("Song not found for songId=" + songId);
			return null;
		}

		// increment play count
		songDAO.incrementPlayCount(songId);
		logger.info("Play count incremented for songId=" + songId);

		// add listening history
		historyDAO.addHistory(userId, songId);
		logger.info("Listening history added for userId=" + userId +
				", songId=" + songId);

		logger.info("Song played successfully: " + song.getTitle());

		return song;
	}

	@Override
	public Song getSongById(int songId) {
		return songDAO.getSongById(songId);
	}

	@Override
	public List<Song> searchSongs(String keyword) {
		return songDAO.searchSongs(keyword);
	}

	@Override
	public List<Song> getSongsByGenre(String genre) {
		return songDAO.getSongsByGenre(genre);
	}

	@Override
	public void uploadSong(Song song) {
		songDAO.uploadSong(song); // 🔥 FIXED
	}

	@Override
	public List<Song> getSongsByArtist(int artistId) {
		return songDAO.getSongsByArtist(artistId);
	}

	@Override
	public List<Song> getSongsByAlbum(int albumId) {
		return songDAO.getSongsByAlbum(albumId);
	}

	@Override
	public List<String> getAllGenres() {
		return songDAO.getAllGenres();
	}
}

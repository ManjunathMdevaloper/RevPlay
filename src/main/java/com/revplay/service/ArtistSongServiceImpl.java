package com.revplay.service;

import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.core.Logger;

import com.revplay.dao.ArtistSongDAO;
import com.revplay.dao.ArtistSongDAOImpl;
import com.revplay.model.Song;

public class ArtistSongServiceImpl implements ArtistSongService {

	private ArtistSongDAO dao = new ArtistSongDAOImpl();
	private static final Logger logger = (Logger) LogManager.getLogger(ArtistSongServiceImpl.class);

	@Override
	public List<Song> getMySongs(int artistId) {
		return dao.getSongsByArtist(artistId);
	}

	@Override
	public void uploadSong(Song song) {
		logger.info("Artist " + song.getArtistId()
				+ " uploading song: " + song.getTitle());
		dao.uploadSong(song);
		logger.info("Song uploaded successfully: " + song.getTitle());
	}

	@Override
	public void updateSong(Song song) {
		logger.info("Updating songId=" + song.getSongId()
				+ " by artistId=" + song.getArtistId());
		dao.updateSong(song);
		logger.info("Song updated successfully. songId="
				+ song.getSongId());
	}

	@Override
	public boolean deleteSong(int songId, int artistId) {
		logger.info("Deleting songId=" + songId
				+ " by artistId=" + artistId);
		boolean f = dao.deleteSong(songId, artistId);
		logger.info("Song deleted successfully. songId=" + songId);
		return f;

	}

	@Override
	public List<Song> getSongsByArtist(int artistId) {
		return dao.getSongsByArtist(artistId);
	}

	@Override
	public List<String> getUsersWhoFavoritedMySongs(int artistId) {
		return dao.getUsersWhoFavoritedMySongs(artistId);
	}

	@Override
	public boolean isSongOwnedByArtist(int songId, int artistId) {
		return dao.isSongOwnedByArtist(songId, artistId);
	}

}

package com.revplay.service;

import java.util.List;

import com.revplay.dao.PlaylistDAO;
import com.revplay.dao.PlaylistDAOImpl;
import com.revplay.model.Playlist;
import com.revplay.model.Song;

public class PlaylistServiceImpl implements PlaylistService {

	private PlaylistDAO playlistDAO = new PlaylistDAOImpl();

	@Override
	public void createPlaylist(Playlist playlist) {
		playlistDAO.createPlaylist(playlist);
	}

	@Override
	public List<Playlist> getMyPlaylists(int userId) {
		return playlistDAO.getPlaylistsByUser(userId);
	}

	@Override
	public boolean addSong(int playlistId, int songId) {
		return playlistDAO.addSongToPlaylist(playlistId, songId);
	}

	@Override
	public boolean removeSong(int playlistId, int songId) {
		return playlistDAO.removeSongFromPlaylist(playlistId, songId);
	}

	@Override
	public void updatePlaylist(Playlist playlist) {
		playlistDAO.updatePlaylist(playlist);
	}

	@Override
	public boolean deletePlaylist(int playlistId, int userId) {
		return playlistDAO.deletePlaylist(playlistId, userId);
	}

	@Override
	public List<Playlist> getPublicPlaylists(int currentUserId) {
		return playlistDAO.getPublicPlaylists(currentUserId);
	}

	@Override
	public List<Playlist> getMyPlaylistsWithSongCount(int userId) {
		return playlistDAO.getMyPlaylistsWithSongCount(userId);
	}

	@Override
	public List<Song> getSongsInPlaylist(int playlistId) {
		return playlistDAO.getSongsInPlaylist(playlistId);
	}

}

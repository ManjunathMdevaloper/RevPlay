package com.revplay.service;

import java.util.List;
import com.revplay.model.Playlist;
import com.revplay.model.Song;

public interface PlaylistService {

	void createPlaylist(Playlist playlist);

	List<Playlist> getMyPlaylists(int userId);

	boolean addSong(int playlistId, int songId);

	boolean removeSong(int playlistId, int songId);

	void updatePlaylist(Playlist playlist);

	boolean deletePlaylist(int playlistId, int userId);

	List<Playlist> getPublicPlaylists(int currentUserId);

	List<Playlist> getMyPlaylistsWithSongCount(int userId);

	List<Song> getSongsInPlaylist(int playlistId);

}

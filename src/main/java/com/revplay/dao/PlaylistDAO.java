package com.revplay.dao;

import java.util.List;
import com.revplay.model.Playlist;
import com.revplay.model.Song;

public interface PlaylistDAO {

	void createPlaylist(Playlist playlist);

	List<Playlist> getPlaylistsByUser(int userId);

	boolean addSongToPlaylist(int playlistId, int songId);

	boolean removeSongFromPlaylist(int playlistId, int songId);

	void updatePlaylist(Playlist playlist);

	boolean deletePlaylist(int playlistId, int userId);

	List<Playlist> getPublicPlaylists(int currentUserId);

	List<Playlist> getMyPlaylistsWithSongCount(int userId);

	List<Song> getSongsInPlaylist(int playlistId);

}

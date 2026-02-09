package com.revplay.dao;

import java.util.List;
import com.revplay.model.Song;

public interface ArtistSongDAO {

	List<Song> getSongsByArtist(int artistId);

	void uploadSong(Song song);

	void updateSong(Song song);

	boolean deleteSong(int songId, int artistId);

	List<String> getUsersWhoFavoritedMySongs(int artistId);

	List<String> getSongStatistics(int artistId);

	boolean isSongOwnedByArtist(int songId, int artistId);

}

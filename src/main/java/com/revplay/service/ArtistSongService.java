package com.revplay.service;

import java.util.List;
import com.revplay.model.Song;

public interface ArtistSongService {

	List<Song> getMySongs(int artistId);

	void uploadSong(Song song);

	void updateSong(Song song);

	boolean deleteSong(int songId, int artistId);

	List<Song> getSongsByArtist(int artistId);

	List<String> getUsersWhoFavoritedMySongs(int artistId);

	boolean isSongOwnedByArtist(int songId, int artistId);

}

package com.revplay.dao;

import java.util.List;
import com.revplay.model.Song;

public interface SongDAO {

	List<Song> getAllSongs();

	Song getSongById(int songId);

	void uploadSong(Song song);

	void incrementPlayCount(int songId);

	List<Song> searchSongs(String keyword);

	List<Song> getSongsByGenre(String genre);

	List<Song> getSongsByArtist(int artistId);

	List<Song> getSongsByAlbum(int albumId);

	List<String> getAllGenres();

}

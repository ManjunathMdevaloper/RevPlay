package com.revplay.service;

import java.util.List;
import com.revplay.model.Song;

public interface SongService {

	List<Song> getAllSongs();

	Song playSong(int userId, int songId);

	Song getSongById(int songId);

	List<Song> searchSongs(String keyword);

	List<Song> getSongsByGenre(String genre);

	void uploadSong(Song song);

	List<Song> getSongsByArtist(int artistId);

	List<Song> getSongsByAlbum(int albumId);

	List<String> getAllGenres();

}

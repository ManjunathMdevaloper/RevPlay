package com.revplay.dao;

import java.util.List;

import com.revplay.model.Album;
import com.revplay.model.Artist;
import com.revplay.model.Song;

public interface ArtistDAO {

	Artist getArtistByUserId(int userId);

	void saveOrUpdateArtist(Artist artist);

	List<Song> getSongsByArtist(int artistId);

	List<Artist> getAllArtists();
	List<Album> getAllAlbums();

}

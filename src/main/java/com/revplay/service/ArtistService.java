package com.revplay.service;

import java.util.List;

import com.revplay.model.Artist;

public interface ArtistService {

	Artist getArtistByUserId(int userId);

	void saveOrUpdateArtist(Artist artist);

	List<String> getSongStatistics(int artistId);

	List<Artist> getAllArtists();

}

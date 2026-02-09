package com.revplay.service;

import java.util.List;

import com.revplay.dao.ArtistDAO;
import com.revplay.dao.ArtistDAOImpl;
import com.revplay.dao.ArtistSongDAO;
import com.revplay.dao.ArtistSongDAOImpl;
import com.revplay.model.Artist;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class ArtistServiceImpl implements ArtistService {

	private ArtistDAO dao = new ArtistDAOImpl();
	private ArtistSongDAO artisSongtDao = new ArtistSongDAOImpl();
	private static final Logger logger = LogManager.getLogger(ArtistServiceImpl.class);

	@Override
	public Artist getArtistByUserId(int userId) {
		return dao.getArtistByUserId(userId);
	}

	@Override
	public void saveOrUpdateArtist(Artist artist) {
		logger.info("Saving artist profile for userId=" + artist.getUserId());
		dao.saveOrUpdateArtist(artist);
		logger.info("Artist profile saved successfully for userId="
				+ artist.getUserId());
	}

	@Override
	public List<String> getSongStatistics(int artistId) {
		return artisSongtDao.getSongStatistics(artistId);
	}

	@Override
	public List<Artist> getAllArtists() {
		return dao.getAllArtists();
	}

}

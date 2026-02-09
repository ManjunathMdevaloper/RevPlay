package com.revplay.service;

import java.util.List;

import com.revplay.dao.AlbumDAO;
import com.revplay.dao.AlbumDAOImpl;
import com.revplay.model.Album;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class AlbumServiceImpl implements AlbumService {

    private AlbumDAO dao = new AlbumDAOImpl();
    private static final Logger logger = LogManager.getLogger(AlbumServiceImpl.class);

    @Override
    public List<Album> getAllAlbums() {
        return dao.getAllAlbums();
    }

    @Override
    public void createAlbum(Album album) {
        logger.info("Artist " + album.getArtistId()
                + " creating album: " + album.getTitle());
        dao.createAlbum(album);
        logger.info("Album created successfully: " + album.getTitle());

    }

    @Override
    public List<Album> getMyAlbums(int artistId) {
        return dao.getAlbumsByArtist(artistId);
    }

    @Override
    public void updateAlbum(Album album) {
        logger.info("Updating albumId=" + album.getAlbumId()
                + " by artistId=" + album.getArtistId());
        dao.updateAlbum(album);
        logger.info("Album updated successfully. albumId="
                + album.getAlbumId());
    }

    @Override
    public void deleteAlbum(int albumId, int artistId) {
        logger.info("Attempting to delete albumId=" + albumId
                + " by artistId=" + artistId);
        dao.deleteAlbum(albumId, artistId);
        logger.info("Album deleted successfully. albumId=" + albumId);

    }

    @Override
    public void deleteSongsByAlbum(int albumId) {
        dao.deleteSongsByAlbum(albumId);
    }

    @Override
    public void deleteFavoritesByAlbum(int albumId) {
        dao.deleteFavoritesByAlbum(albumId);
    }

    @Override
    public void deletePlaylistSongsByAlbum(int albumId) {
        dao.deletePlaylistSongsByAlbum(albumId);
    }

    @Override
    public void deleteListeningHistoryByAlbum(int albumId) {
        dao.deleteListeningHistoryByAlbum(albumId);
    }

    @Override
    public boolean hasSongs(int albumId) {
        return dao.hasSongs(albumId);
    }

}

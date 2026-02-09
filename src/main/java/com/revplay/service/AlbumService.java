package com.revplay.service;

import java.util.List;
import com.revplay.model.Album;

public interface AlbumService {

	void createAlbum(Album album);

	List<Album> getMyAlbums(int artistId);

	List<Album> getAllAlbums();

	void updateAlbum(Album album);

	void deleteAlbum(int albumId, int artistId);

	boolean hasSongs(int albumId);

	void deleteSongsByAlbum(int albumId);

	void deleteFavoritesByAlbum(int albumId);

	void deletePlaylistSongsByAlbum(int albumId);

	void deleteListeningHistoryByAlbum(int albumId);

}

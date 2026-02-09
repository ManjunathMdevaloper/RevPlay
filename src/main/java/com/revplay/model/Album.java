package com.revplay.model;

import java.sql.Date;

public class Album {

	private int albumId;
	private String title;
	private Date releaseDate;
	private int artistId;

	public Album() {
	}

	public Album(int albumId, String title, Date releaseDate, int artistId) {
		this.albumId = albumId;
		this.title = title;
		this.releaseDate = releaseDate;
		this.artistId = artistId;
	}

	public int getAlbumId() {
		return albumId;
	}

	public void setAlbumId(int albumId) {
		this.albumId = albumId;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public Date getReleaseDate() {
		return releaseDate;
	}

	public void setReleaseDate(Date releaseDate) {
		this.releaseDate = releaseDate;
	}

	public int getArtistId() {
		return artistId;
	}

	public void setArtistId(int artistId) {
		this.artistId = artistId;
	}
}

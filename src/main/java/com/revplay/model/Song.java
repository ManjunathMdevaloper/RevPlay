package com.revplay.model;

import java.sql.Date;

public class Song {

	private int songId;
	private String title;
	private String genre;
	private int duration;
	private Date releaseDate;
	private int playCount;
	private int artistId;
	private int albumId;

	public Song() {
	}

	public Song(int songId, String title, String genre, int duration, Date releaseDate, int playCount, int artistId,
			int albumId) {
		this.songId = songId;
		this.title = title;
		this.genre = genre;
		this.duration = duration;
		this.releaseDate = releaseDate;
		this.playCount = playCount;
		this.artistId = artistId;
		this.albumId = albumId;
	}

	// getters & setters
	public int getSongId() {
		return songId;
	}

	public void setSongId(int songId) {
		this.songId = songId;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getGenre() {
		return genre;
	}

	public void setGenre(String genre) {
		this.genre = genre;
	}

	public int getDuration() {
		return duration;
	}

	public void setDuration(int duration) {
		this.duration = duration;
	}

	public Date getReleaseDate() {
		return releaseDate;
	}

	public void setReleaseDate(Date releaseDate) {
		this.releaseDate = releaseDate;
	}

	public int getPlayCount() {
		return playCount;
	}

	public void setPlayCount(int playCount) {
		this.playCount = playCount;
	}

	public int getArtistId() {
		return artistId;
	}

	public void setArtistId(int artistId) {
		this.artistId = artistId;
	}

	public int getAlbumId() {
		return albumId;
	}

	public void setAlbumId(int albumId) {
		this.albumId = albumId;
	}
}

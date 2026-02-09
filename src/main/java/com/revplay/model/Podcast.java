package com.revplay.model;

import java.sql.Date;

public class Podcast {

	private int podcastId;
	private String title;
	private String hostName;
	private String category;
	private String description;
	private int artistId;
	private Date createdAt;

	public Podcast() {
	}

	public Podcast(int podcastId, int artistId, String title, String hostName, String category, String description,
			Date createdAt) {
		this.podcastId = podcastId;
		this.artistId = artistId;
		this.title = title;
		this.hostName = hostName;
		this.category = category;
		this.description = description;
		this.createdAt = createdAt;
	}

	public int getPodcastId() {
		return podcastId;
	}

	public void setPodcastId(int podcastId) {
		this.podcastId = podcastId;
	}

	public int getArtistId() {
		return artistId;
	}

	public void setArtistId(int artistId) {
		this.artistId = artistId;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getHostName() {
		return hostName;
	}

	public void setHostName(String hostName) {
		this.hostName = hostName;
	}

	public String getCategory() {
		return category;
	}

	public void setCategory(String category) {
		this.category = category;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public Date getCreatedAt() {
		return createdAt;
	}

	public void setCreatedAt(Date createdAt) {
		this.createdAt = createdAt;
	}
}

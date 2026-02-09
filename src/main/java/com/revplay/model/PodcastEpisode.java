package com.revplay.model;

import java.sql.Date;

public class PodcastEpisode {

	private int episodeId;
	private int podcastId;
	private String title;
	private int duration;
	private Date releaseDate;
	private int playCount;

	public PodcastEpisode() {
	}

	public PodcastEpisode(int episodeId, int podcastId, String title, int duration, Date releaseDate, int playCount) {
		this.episodeId = episodeId;
		this.podcastId = podcastId;
		this.title = title;
		this.duration = duration;
		this.releaseDate = releaseDate;
		this.playCount = playCount;
	}

	public int getEpisodeId() {
		return episodeId;
	}

	public void setEpisodeId(int episodeId) {
		this.episodeId = episodeId;
	}

	public int getPodcastId() {
		return podcastId;
	}

	public void setPodcastId(int podcastId) {
		this.podcastId = podcastId;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
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
}

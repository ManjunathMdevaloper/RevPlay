package com.revplay.model;

import java.sql.Date;

public class ListeningHistory {

	private int historyId;
	private int userId;
	private int songId;
	private Date playedAt;

	public ListeningHistory() {
	}

	public ListeningHistory(int historyId, int userId, int songId, Date playedAt) {
		this.historyId = historyId;
		this.userId = userId;
		this.songId = songId;
		this.playedAt = playedAt;
	}

	public int getHistoryId() {
		return historyId;
	}

	public void setHistoryId(int historyId) {
		this.historyId = historyId;
	}

	public int getUserId() {
		return userId;
	}

	public void setUserId(int userId) {
		this.userId = userId;
	}

	public int getSongId() {
		return songId;
	}

	public void setSongId(int songId) {
		this.songId = songId;
	}

	public Date getPlayedAt() {
		return playedAt;
	}

	public void setPlayedAt(Date playedAt) {
		this.playedAt = playedAt;
	}
}

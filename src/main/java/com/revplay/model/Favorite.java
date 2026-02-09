package com.revplay.model;

import java.sql.Date;

public class Favorite {

	private int userId;
	private int songId;
	private Date addedAt;

	public Favorite() {
	}

	public Favorite(int userId, int songId, Date addedAt) {
		this.userId = userId;
		this.songId = songId;
		this.addedAt = addedAt;
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

	public Date getAddedAt() {
		return addedAt;
	}

	public void setAddedAt(Date addedAt) {
		this.addedAt = addedAt;
	}
}

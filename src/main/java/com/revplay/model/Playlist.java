package com.revplay.model;

public class Playlist {

	private int playlistId;
	private String name;
	private String description;
	private String isPublic; // Y / N
	private int userId;
	private String ownerName; // not stored, only for display
	private int songCount;

	public int getSongCount() {
		return songCount;
	}

	public void setSongCount(int songCount) {
		this.songCount = songCount;
	}

	public Playlist() {
	}

	public String getOwnerName() {
		return ownerName;
	}

	public void setOwnerName(String ownerName) {
		this.ownerName = ownerName;
	}

	public Playlist(int playlistId, String name, String description, String isPublic, int userId) {
		this.playlistId = playlistId;
		this.name = name;
		this.description = description;
		this.isPublic = isPublic;
		this.userId = userId;
	}

	public int getPlaylistId() {
		return playlistId;
	}

	public void setPlaylistId(int playlistId) {
		this.playlistId = playlistId;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getIsPublic() {
		return isPublic;
	}

	public void setIsPublic(String isPublic) {
		this.isPublic = isPublic;
	}

	public int getUserId() {
		return userId;
	}

	public void setUserId(int userId) {
		this.userId = userId;
	}
}

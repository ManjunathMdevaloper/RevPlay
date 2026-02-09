package com.revplay.dao;

import java.util.List;

import com.revplay.model.Song;

public interface ListeningHistoryDAO {

	void addHistory(int userId, int songId);

	List<Song> getListeningHistory(int userId);

	List<Song> getRecentPlays(int userId, int limit);

}

package com.revplay.dao;

import java.util.List;
import com.revplay.model.Song;

public interface FavoriteDAO {

    void addToFavorites(int userId, int songId);

    List<Song> getFavoriteSongs(int userId);

    boolean isFavorite(int userId, int songId);
}

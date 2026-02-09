package com.revplay.test;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.sql.SQLException;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.MockedStatic;

import com.revplay.dao.*;
import com.revplay.util.DBConnection;

class DAOExceptionTest {

    private MockedStatic<DBConnection> mockedDbConnection;

    @BeforeEach
    void setUp() {
        mockedDbConnection = mockStatic(DBConnection.class);
        mockedDbConnection.when(DBConnection::getConnection).thenThrow(new SQLException("DB Down"));
    }

    @AfterEach
    void tearDown() {
        mockedDbConnection.close();
    }

    @Test
    void testDAOExceptions() {
        // This hits the catch blocks in every DAO method
        assertDoesNotThrow(() -> new UserDAOImpl().login("e", "p"));
        assertDoesNotThrow(() -> new SongDAOImpl().getAllSongs());
        assertDoesNotThrow(() -> new PodcastDAOImpl().getAllPodcasts());
        assertDoesNotThrow(() -> new ArtistDAOImpl().getAllArtists());
        assertDoesNotThrow(() -> new AlbumDAOImpl().getAllAlbums());
        assertDoesNotThrow(() -> new PlaylistDAOImpl().getPlaylistsByUser(1));
        assertDoesNotThrow(() -> new ArtistSongDAOImpl().getSongStatistics(1));
        assertDoesNotThrow(() -> new FavoriteDAOImpl().getFavoriteSongs(1));
        assertDoesNotThrow(() -> new ListeningHistoryDAOImpl().getListeningHistory(1));
    }
}

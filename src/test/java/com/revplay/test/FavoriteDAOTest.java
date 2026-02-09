package com.revplay.test;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.List;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.MockedStatic;

import com.revplay.dao.FavoriteDAOImpl;
import com.revplay.model.Song;
import com.revplay.util.DBConnection;

class FavoriteDAOTest {

    private MockedStatic<DBConnection> mockedDbConnection;
    private Connection mockCon;
    private PreparedStatement mockPs;
    private ResultSet mockRs;
    private FavoriteDAOImpl dao;

    @BeforeEach
    void setUp() throws Exception {
        mockedDbConnection = mockStatic(DBConnection.class);
        mockCon = mock(Connection.class);
        mockPs = mock(PreparedStatement.class);
        mockRs = mock(ResultSet.class);
        dao = new FavoriteDAOImpl();

        mockedDbConnection.when(DBConnection::getConnection).thenReturn(mockCon);
    }

    @AfterEach
    void tearDown() {
        mockedDbConnection.close();
    }

    @Test
    void testAddToFavorites() throws Exception {
        when(mockCon.prepareStatement(anyString())).thenReturn(mockPs);
        when(mockPs.executeQuery()).thenReturn(mockRs);
        when(mockRs.next()).thenReturn(false); // Not already a favorite
        when(mockPs.executeUpdate()).thenReturn(1);

        dao.addToFavorites(1, 301);
        verify(mockPs).executeUpdate();
    }

    @Test
    void testGetFavoriteSongs() throws Exception {
        when(mockCon.prepareStatement(anyString())).thenReturn(mockPs);
        when(mockPs.executeQuery()).thenReturn(mockRs);
        when(mockRs.next()).thenReturn(true, false);
        when(mockRs.getInt("song_id")).thenReturn(301);

        List<Song> songs = dao.getFavoriteSongs(1);
        assertEquals(1, songs.size());
    }

    @Test
    void testIsFavorite() throws Exception {
        when(mockCon.prepareStatement(anyString())).thenReturn(mockPs);
        when(mockPs.executeQuery()).thenReturn(mockRs);
        when(mockRs.next()).thenReturn(true);

        assertTrue(dao.isFavorite(1, 301));
    }
}

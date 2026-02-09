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

import com.revplay.dao.PlaylistDAOImpl;
import com.revplay.model.Playlist;
import com.revplay.util.DBConnection;

class PlaylistDAOTest {

    private MockedStatic<DBConnection> mockedDbConnection;
    private Connection mockCon;
    private PreparedStatement mockPs;
    private ResultSet mockRs;
    private PlaylistDAOImpl dao;

    @BeforeEach
    void setUp() throws Exception {
        mockedDbConnection = mockStatic(DBConnection.class);
        mockCon = mock(Connection.class);
        mockPs = mock(PreparedStatement.class);
        mockRs = mock(ResultSet.class);
        dao = new PlaylistDAOImpl();

        mockedDbConnection.when(DBConnection::getConnection).thenReturn(mockCon);
    }

    @AfterEach
    void tearDown() {
        mockedDbConnection.close();
    }

    @Test
    void testCreatePlaylist() throws Exception {
        when(mockCon.prepareStatement(anyString())).thenReturn(mockPs);
        when(mockPs.executeUpdate()).thenReturn(1);

        Playlist p = new Playlist();
        dao.createPlaylist(p);
        verify(mockPs).executeUpdate();
    }

    @Test
    void testGetPlaylistsByUser() throws Exception {
        when(mockCon.prepareStatement(anyString())).thenReturn(mockPs);
        when(mockPs.executeQuery()).thenReturn(mockRs);
        when(mockRs.next()).thenReturn(true, false);
        when(mockRs.getInt("playlist_id")).thenReturn(401);

        List<Playlist> list = dao.getPlaylistsByUser(1);
        assertEquals(1, list.size());
    }

    @Test
    void testAddSongToPlaylist() throws Exception {
        when(mockCon.prepareStatement(anyString())).thenReturn(mockPs);
        when(mockPs.executeUpdate()).thenReturn(1);

        assertTrue(dao.addSongToPlaylist(401, 301));
    }

    @Test
    void testRemoveSongFromPlaylist() throws Exception {
        when(mockCon.prepareStatement(anyString())).thenReturn(mockPs);
        when(mockPs.executeUpdate()).thenReturn(1);

        assertTrue(dao.removeSongFromPlaylist(401, 301));
    }

    @Test
    void testUpdatePlaylist() throws Exception {
        when(mockCon.prepareStatement(anyString())).thenReturn(mockPs);
        when(mockPs.executeUpdate()).thenReturn(1);

        Playlist p = new Playlist();
        p.setPlaylistId(401);
        p.setUserId(1);
        dao.updatePlaylist(p);
        verify(mockPs).executeUpdate();
    }

    @Test
    void testDeletePlaylist() throws Exception {
        when(mockCon.prepareStatement(anyString())).thenReturn(mockPs);
        when(mockPs.executeUpdate()).thenReturn(1);
        when(mockCon.getAutoCommit()).thenReturn(true);

        assertTrue(dao.deletePlaylist(401, 1));
    }

    @Test
    void testGetPublicPlaylists() throws Exception {
        when(mockCon.prepareStatement(anyString())).thenReturn(mockPs);
        when(mockPs.executeQuery()).thenReturn(mockRs);
        when(mockRs.next()).thenReturn(true, false);

        assertNotNull(dao.getPublicPlaylists(1));
    }

    @Test
    void testGetMyPlaylistsWithSongCount() throws Exception {
        when(mockCon.prepareStatement(anyString())).thenReturn(mockPs);
        when(mockPs.executeQuery()).thenReturn(mockRs);
        when(mockRs.next()).thenReturn(true, false);

        assertNotNull(dao.getMyPlaylistsWithSongCount(1));
    }
}

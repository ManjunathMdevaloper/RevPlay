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

import com.revplay.dao.SongDAOImpl;
import com.revplay.model.Song;
import com.revplay.util.DBConnection;

class SongDAOTest {

    private MockedStatic<DBConnection> mockedDbConnection;
    private Connection mockCon;
    private PreparedStatement mockPs;
    private ResultSet mockRs;
    private SongDAOImpl dao;

    @BeforeEach
    void setUp() throws Exception {
        mockedDbConnection = mockStatic(DBConnection.class);
        mockCon = mock(Connection.class);
        mockPs = mock(PreparedStatement.class);
        mockRs = mock(ResultSet.class);
        dao = new SongDAOImpl();

        mockedDbConnection.when(DBConnection::getConnection).thenReturn(mockCon);
    }

    @AfterEach
    void tearDown() {
        mockedDbConnection.close();
    }

    @Test
    void testUploadSong() throws Exception {
        when(mockCon.prepareStatement(anyString())).thenReturn(mockPs);
        when(mockPs.executeUpdate()).thenReturn(1);

        Song s = new Song();
        s.setTitle("T");
        dao.uploadSong(s);
        verify(mockPs).executeUpdate();
    }

    @Test
    void testGetAllSongs() throws Exception {
        when(mockCon.prepareStatement(anyString())).thenReturn(mockPs);
        when(mockPs.executeQuery()).thenReturn(mockRs);
        when(mockRs.next()).thenReturn(true, false);
        when(mockRs.getInt("song_id")).thenReturn(1);
        when(mockRs.getString("title")).thenReturn("T");

        List<Song> songs = dao.getAllSongs();
        assertFalse(songs.isEmpty());
        assertEquals(1, songs.get(0).getSongId());
    }

    @Test
    void testGetSongsByGenre() throws Exception {
        when(mockCon.prepareStatement(anyString())).thenReturn(mockPs);
        when(mockPs.executeQuery()).thenReturn(mockRs);
        when(mockRs.next()).thenReturn(true, false);

        List<Song> songs = dao.getSongsByGenre("Pop");
        assertFalse(songs.isEmpty());
    }

    @Test
    void testGetSongById() throws Exception {
        when(mockCon.prepareStatement(anyString())).thenReturn(mockPs);
        when(mockPs.executeQuery()).thenReturn(mockRs);
        when(mockRs.next()).thenReturn(true);
        when(mockRs.getInt("song_id")).thenReturn(301);

        assertNotNull(dao.getSongById(301));
    }

    @Test
    void testIncrementPlayCount() throws Exception {
        when(mockCon.prepareStatement(anyString())).thenReturn(mockPs);
        when(mockPs.executeUpdate()).thenReturn(1);

        dao.incrementPlayCount(301);
        verify(mockPs).executeUpdate();
    }

    @Test
    void testSearchSongs() throws Exception {
        when(mockCon.prepareStatement(anyString())).thenReturn(mockPs);
        when(mockPs.executeQuery()).thenReturn(mockRs);
        when(mockRs.next()).thenReturn(true, false);

        assertFalse(dao.searchSongs("keyword").isEmpty());
    }

    @Test
    void testGetAllGenres() throws Exception {
        when(mockCon.prepareStatement(anyString())).thenReturn(mockPs);
        when(mockPs.executeQuery()).thenReturn(mockRs);
        when(mockRs.next()).thenReturn(true, false);
        when(mockRs.getString("genre")).thenReturn("Rock");

        assertTrue(dao.getAllGenres().contains("Rock"));
    }
}

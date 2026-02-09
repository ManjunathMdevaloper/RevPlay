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

import com.revplay.dao.AlbumDAOImpl;
import com.revplay.model.Album;
import com.revplay.util.DBConnection;

class AlbumDAOTest {

    private MockedStatic<DBConnection> mockedDbConnection;
    private Connection mockCon;
    private PreparedStatement mockPs;
    private ResultSet mockRs;
    private AlbumDAOImpl dao;

    @BeforeEach
    void setUp() throws Exception {
        mockedDbConnection = mockStatic(DBConnection.class);
        mockCon = mock(Connection.class);
        mockPs = mock(PreparedStatement.class);
        mockRs = mock(ResultSet.class);
        dao = new AlbumDAOImpl();

        mockedDbConnection.when(DBConnection::getConnection).thenReturn(mockCon);
    }

    @AfterEach
    void tearDown() {
        mockedDbConnection.close();
    }

    @Test
    void testCreateAlbum() throws Exception {
        when(mockCon.prepareStatement(anyString())).thenReturn(mockPs);
        when(mockPs.executeUpdate()).thenReturn(1);

        Album album = new Album();
        album.setReleaseDate(new java.sql.Date(System.currentTimeMillis()));
        dao.createAlbum(album);
        verify(mockPs).executeUpdate();
    }

    @Test
    void testGetAlbumsByArtist() throws Exception {
        when(mockCon.prepareStatement(anyString())).thenReturn(mockPs);
        when(mockPs.executeQuery()).thenReturn(mockRs);
        when(mockRs.next()).thenReturn(true, false);
        when(mockRs.getInt("album_id")).thenReturn(201);
        when(mockRs.getString("title")).thenReturn("Album 1");

        List<Album> albums = dao.getAlbumsByArtist(101);
        assertEquals(1, albums.size());
        assertEquals(201, albums.get(0).getAlbumId());
    }

    @Test
    void testUpdateAlbum() throws Exception {
        when(mockCon.prepareStatement(anyString())).thenReturn(mockPs);
        when(mockPs.executeUpdate()).thenReturn(1);

        Album album = new Album();
        album.setAlbumId(201);
        album.setTitle("Updated");
        album.setReleaseDate(new java.sql.Date(System.currentTimeMillis()));
        dao.updateAlbum(album);
        verify(mockPs).executeUpdate();
    }

    @Test
    void testDeleteAlbum() throws Exception {
        when(mockCon.prepareStatement(anyString())).thenReturn(mockPs);
        when(mockPs.executeUpdate()).thenReturn(1);

        dao.deleteAlbum(201, 101);
        verify(mockPs).executeUpdate();
    }

    @Test
    void testHasSongs() throws Exception {
        when(mockCon.prepareStatement(anyString())).thenReturn(mockPs);
        when(mockPs.executeQuery()).thenReturn(mockRs);
        when(mockRs.next()).thenReturn(true);
        when(mockRs.getInt(1)).thenReturn(1);

        assertTrue(dao.hasSongs(201));
    }

    @Test
    void testDeleteSongsByAlbum() throws Exception {
        when(mockCon.prepareStatement(anyString())).thenReturn(mockPs);
        when(mockPs.executeUpdate()).thenReturn(1);

        dao.deleteSongsByAlbum(201);
        verify(mockPs).executeUpdate();
    }

    @Test
    void testGetAllAlbums() throws Exception {
        when(mockCon.prepareStatement(anyString())).thenReturn(mockPs);
        when(mockPs.executeQuery()).thenReturn(mockRs);
        when(mockRs.next()).thenReturn(true, false);

        assertNotNull(dao.getAllAlbums());
    }
}

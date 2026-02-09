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

import com.revplay.dao.ArtistDAOImpl;
import com.revplay.model.Artist;
import com.revplay.util.DBConnection;

class ArtistDAOTest {

    private MockedStatic<DBConnection> mockedDbConnection;
    private Connection mockCon;
    private PreparedStatement mockPs;
    private ResultSet mockRs;
    private ArtistDAOImpl dao;

    @BeforeEach
    void setUp() throws Exception {
        mockedDbConnection = mockStatic(DBConnection.class);
        mockCon = mock(Connection.class);
        mockPs = mock(PreparedStatement.class);
        mockRs = mock(ResultSet.class);
        dao = new ArtistDAOImpl();

        mockedDbConnection.when(DBConnection::getConnection).thenReturn(mockCon);
    }

    @AfterEach
    void tearDown() {
        mockedDbConnection.close();
    }

    @Test
    void testSaveOrUpdateArtist() throws Exception {
        when(mockCon.prepareStatement(anyString())).thenReturn(mockPs);
        when(mockPs.executeQuery()).thenReturn(mockRs);
        when(mockRs.next()).thenReturn(false); // Does not exist, so insert
        when(mockPs.executeUpdate()).thenReturn(1);

        Artist a = new Artist();
        a.setUserId(1);
        dao.saveOrUpdateArtist(a);
        verify(mockPs, atLeastOnce()).executeUpdate();
    }

    @Test
    void testGetArtistByUserId() throws Exception {
        when(mockCon.prepareStatement(anyString())).thenReturn(mockPs);
        when(mockPs.executeQuery()).thenReturn(mockRs);
        when(mockRs.next()).thenReturn(true);
        when(mockRs.getInt("artist_id")).thenReturn(101);

        Artist a = dao.getArtistByUserId(1);
        assertNotNull(a);
        assertEquals(101, a.getArtistId());
    }

    @Test
    void testGetAllArtists() throws Exception {
        when(mockCon.prepareStatement(anyString())).thenReturn(mockPs);
        when(mockPs.executeQuery()).thenReturn(mockRs);
        when(mockRs.next()).thenReturn(true, false);

        List<Artist> list = dao.getAllArtists();
        assertEquals(1, list.size());
    }

    @Test
    void testGetAllAlbums() throws Exception {
        when(mockCon.prepareStatement(anyString())).thenReturn(mockPs);
        when(mockPs.executeQuery()).thenReturn(mockRs);
        when(mockRs.next()).thenReturn(true, false);

        assertNotNull(dao.getAllAlbums());
    }

    @Test
    void testSaveOrUpdateArtist_Update() throws Exception {
        when(mockCon.prepareStatement(anyString())).thenReturn(mockPs);
        when(mockPs.executeQuery()).thenReturn(mockRs);
        when(mockRs.next()).thenReturn(true); // Exists
        when(mockRs.getInt("artist_id")).thenReturn(101);
        when(mockPs.executeUpdate()).thenReturn(1);

        Artist a = new Artist();
        a.setUserId(1);
        dao.saveOrUpdateArtist(a);
        verify(mockPs, atLeastOnce()).executeUpdate();
    }

    @Test
    void testGetSongsByArtist() throws Exception {
        when(mockCon.prepareStatement(anyString())).thenReturn(mockPs);
        when(mockPs.executeQuery()).thenReturn(mockRs);
        when(mockRs.next()).thenReturn(true, false);

        assertNotNull(dao.getSongsByArtist(101));
    }
}

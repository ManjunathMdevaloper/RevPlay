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

import com.revplay.dao.PodcastDAOImpl;
import com.revplay.model.Podcast;
import com.revplay.model.PodcastEpisode;
import com.revplay.util.DBConnection;

class PodcastDAOTest {

    private MockedStatic<DBConnection> mockedDbConnection;
    private Connection mockCon;
    private PreparedStatement mockPs;
    private ResultSet mockRs;
    private PodcastDAOImpl dao;

    @BeforeEach
    void setUp() throws Exception {
        mockedDbConnection = mockStatic(DBConnection.class);
        mockCon = mock(Connection.class);
        mockPs = mock(PreparedStatement.class);
        mockRs = mock(ResultSet.class);
        dao = new PodcastDAOImpl();

        mockedDbConnection.when(DBConnection::getConnection).thenReturn(mockCon);
    }

    @AfterEach
    void tearDown() {
        mockedDbConnection.close();
    }

    @Test
    void testCreatePodcast() throws Exception {
        when(mockCon.prepareStatement(anyString())).thenReturn(mockPs);
        when(mockPs.executeUpdate()).thenReturn(1);

        Podcast p = new Podcast();
        dao.createPodcast(p);
        verify(mockPs).executeUpdate();
    }

    @Test
    void testGetAllPodcasts() throws Exception {
        when(mockCon.prepareStatement(anyString())).thenReturn(mockPs);
        when(mockPs.executeQuery()).thenReturn(mockRs);
        when(mockRs.next()).thenReturn(true, false);
        when(mockRs.getInt("podcast_id")).thenReturn(601);

        List<Podcast> list = dao.getAllPodcasts();
        assertEquals(1, list.size());
    }

    @Test
    void testAddEpisode() throws Exception {
        when(mockCon.prepareStatement(anyString())).thenReturn(mockPs);
        when(mockPs.executeUpdate()).thenReturn(1);

        PodcastEpisode ep = new PodcastEpisode();
        dao.addEpisode(ep);
        verify(mockPs).executeUpdate();
    }

    @Test
    void testGetEpisodesByPodcast() throws Exception {
        when(mockCon.prepareStatement(anyString())).thenReturn(mockPs);
        when(mockPs.executeQuery()).thenReturn(mockRs);
        when(mockRs.next()).thenReturn(true, false);

        List<PodcastEpisode> list = dao.getEpisodesByPodcast(601);
        assertEquals(1, list.size());
    }

    @Test
    void testSearchPodcasts() throws Exception {
        when(mockCon.prepareStatement(anyString())).thenReturn(mockPs);
        when(mockPs.executeQuery()).thenReturn(mockRs);
        when(mockRs.next()).thenReturn(true, false);

        List<Podcast> results = dao.searchPodcasts("crime");
        assertFalse(results.isEmpty());
    }

    @Test
    void testDeletePodcast() throws Exception {
        when(mockCon.prepareStatement(anyString())).thenReturn(mockPs);
        when(mockPs.executeUpdate()).thenReturn(1);

        dao.deletePodcast(601, 5);
        verify(mockPs, times(2)).executeUpdate();
    }

    @Test
    void testIncrementEpisodePlayCount() throws Exception {
        when(mockCon.prepareStatement(anyString())).thenReturn(mockPs);
        when(mockPs.executeUpdate()).thenReturn(1);

        dao.incrementEpisodePlayCount(701);
        verify(mockPs).executeUpdate();
    }

    @Test
    void testGetPodcastsByArtist() throws Exception {
        when(mockCon.prepareStatement(anyString())).thenReturn(mockPs);
        when(mockPs.executeQuery()).thenReturn(mockRs);
        when(mockRs.next()).thenReturn(true, false);

        assertNotNull(dao.getPodcastsByArtist(5));
    }

    @Test
    void testUpdatePodcast() throws Exception {
        when(mockCon.prepareStatement(anyString())).thenReturn(mockPs);
        when(mockPs.executeUpdate()).thenReturn(1);

        Podcast p = new Podcast();
        p.setPodcastId(601);
        p.setArtistId(5);
        assertTrue(dao.updatePodcast(p));
    }

    @Test
    void testUpdateEpisode() throws Exception {
        when(mockCon.prepareStatement(anyString())).thenReturn(mockPs);
        when(mockPs.executeUpdate()).thenReturn(1);

        PodcastEpisode ep = new PodcastEpisode();
        ep.setEpisodeId(701);
        assertTrue(dao.updateEpisode(ep));
    }

    @Test
    void testDeleteEpisode() throws Exception {
        when(mockCon.prepareStatement(anyString())).thenReturn(mockPs);
        when(mockPs.executeUpdate()).thenReturn(1);

        assertTrue(dao.deleteEpisode(701));
    }

    @Test
    void testGetEpisodeById() throws Exception {
        when(mockCon.prepareStatement(anyString())).thenReturn(mockPs);
        when(mockPs.executeQuery()).thenReturn(mockRs);
        when(mockRs.next()).thenReturn(true);

        assertNotNull(dao.getEpisodeById(701));
    }
}

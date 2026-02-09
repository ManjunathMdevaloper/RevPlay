package com.revplay.test;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.revplay.dao.PodcastDAO;
import com.revplay.model.Podcast;
import com.revplay.model.PodcastEpisode;
import com.revplay.service.PodcastServiceImpl;

import java.lang.reflect.Field;
import java.util.Arrays;
import java.util.List;

@ExtendWith(MockitoExtension.class)
class PodcastServiceTest {

    @Mock
    private PodcastDAO dao;

    @InjectMocks
    private PodcastServiceImpl service;

    @BeforeEach
    void setUp() throws Exception {
        Field field = PodcastServiceImpl.class.getDeclaredField("dao");
        field.setAccessible(true);
        field.set(service, dao);
    }

    @Test
    void testSearchPodcasts() {
        List<Podcast> list = Arrays.asList(new Podcast());
        when(dao.searchPodcasts("crime")).thenReturn(list);
        assertEquals(1, service.searchPodcasts("crime").size());
    }

    @Test
    void testGetAllPodcasts() {
        List<Podcast> list = Arrays.asList(new Podcast());
        when(dao.getAllPodcasts()).thenReturn(list);
        assertEquals(1, service.getAllPodcasts().size());
    }

    @Test
    void testGetEpisodesByPodcast() {
        List<PodcastEpisode> list = Arrays.asList(new PodcastEpisode());
        when(dao.getEpisodesByPodcast(1)).thenReturn(list);
        assertEquals(1, service.getEpisodesByPodcast(1).size());
    }

    @Test
    void testPlayEpisode_Success() {
        PodcastEpisode ep = new PodcastEpisode();
        when(dao.getEpisodeById(1)).thenReturn(ep);

        assertEquals(ep, service.playEpisode(10, 1));
        verify(dao).incrementEpisodePlayCount(1);
    }

    @Test
    void testPlayEpisode_NotFound() {
        when(dao.getEpisodeById(99)).thenReturn(null);
        assertNull(service.playEpisode(10, 99));
        verify(dao, never()).incrementEpisodePlayCount(anyInt());
    }

    @Test
    void testCreatePodcast() {
        Podcast p = new Podcast();
        service.createPodcast(p);
        verify(dao).createPodcast(p);
    }

    @Test
    void testAddEpisode() {
        PodcastEpisode ep = new PodcastEpisode();
        service.addEpisode(ep);
        verify(dao).addEpisode(ep);
    }

    @Test
    void testGetPodcastsByArtist() {
        List<Podcast> list = Arrays.asList(new Podcast());
        when(dao.getPodcastsByArtist(1)).thenReturn(list);
        assertEquals(1, service.getPodcastsByArtist(1).size());
    }

    @Test
    void testUpdatePodcast() {
        Podcast p = new Podcast();
        when(dao.updatePodcast(p)).thenReturn(true);
        assertTrue(service.updatePodcast(p));
    }

    @Test
    void testDeletePodcast() {
        when(dao.deletePodcast(1, 10)).thenReturn(true);
        assertTrue(service.deletePodcast(1, 10));
    }

    @Test
    void testUpdateEpisode() {
        PodcastEpisode ep = new PodcastEpisode();
        when(dao.updateEpisode(ep)).thenReturn(true);
        assertTrue(service.updateEpisode(ep));
    }

    @Test
    void testDeleteEpisode() {
        when(dao.deleteEpisode(1)).thenReturn(true);
        assertTrue(service.deleteEpisode(1));
    }
}

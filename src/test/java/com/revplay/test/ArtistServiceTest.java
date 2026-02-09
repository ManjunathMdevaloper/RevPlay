package com.revplay.test;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.revplay.dao.ArtistDAO;
import com.revplay.dao.ArtistSongDAO;
import com.revplay.model.Artist;
import com.revplay.service.ArtistServiceImpl;

import java.lang.reflect.Field;
import java.util.Arrays;
import java.util.List;

@ExtendWith(MockitoExtension.class)
class ArtistServiceTest {

    @Mock
    private ArtistDAO dao;

    @Mock
    private ArtistSongDAO artistSongDao;

    @InjectMocks
    private ArtistServiceImpl service;

    @BeforeEach
    void setUp() throws Exception {
        Field field = ArtistServiceImpl.class.getDeclaredField("dao");
        field.setAccessible(true);
        field.set(service, dao);

        Field songField = ArtistServiceImpl.class.getDeclaredField("artisSongtDao");
        songField.setAccessible(true);
        songField.set(service, artistSongDao);
    }

    @Test
    void testGetArtistByUserId() {
        Artist artist = new Artist();
        when(dao.getArtistByUserId(1)).thenReturn(artist);
        assertEquals(artist, service.getArtistByUserId(1));
    }

    @Test
    void testSaveOrUpdateArtist() {
        Artist artist = new Artist();
        service.saveOrUpdateArtist(artist);
        verify(dao).saveOrUpdateArtist(artist);
    }

    @Test
    void testGetSongStatistics() {
        List<String> stats = Arrays.asList("Stat1", "Stat2");
        when(artistSongDao.getSongStatistics(1)).thenReturn(stats);
        assertEquals(2, service.getSongStatistics(1).size());
    }

    @Test
    void testGetAllArtists() {
        List<Artist> list = Arrays.asList(new Artist());
        when(dao.getAllArtists()).thenReturn(list);
        assertEquals(1, service.getAllArtists().size());
    }
}

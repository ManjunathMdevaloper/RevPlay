package com.revplay.test;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.revplay.dao.ArtistSongDAO;
import com.revplay.model.Song;
import com.revplay.service.ArtistSongServiceImpl;

import java.lang.reflect.Field;
import java.util.Arrays;
import java.util.List;

@ExtendWith(MockitoExtension.class)
class ArtistSongServiceTest {

    @Mock
    private ArtistSongDAO dao;

    @InjectMocks
    private ArtistSongServiceImpl service;

    @BeforeEach
    void setUp() throws Exception {
        Field field = ArtistSongServiceImpl.class.getDeclaredField("dao");
        field.setAccessible(true);
        field.set(service, dao);
    }

    @Test
    void testGetMySongs() {
        List<Song> songs = Arrays.asList(new Song());
        when(dao.getSongsByArtist(1)).thenReturn(songs);
        assertEquals(1, service.getMySongs(1).size());
    }

    @Test
    void testUploadSong() {
        Song song = new Song();
        service.uploadSong(song);
        verify(dao).uploadSong(song);
    }

    @Test
    void testUpdateSong() {
        Song song = new Song();
        service.updateSong(song);
        verify(dao).updateSong(song);
    }

    @Test
    void testDeleteSong() {
        when(dao.deleteSong(1, 10)).thenReturn(true);
        assertTrue(service.deleteSong(1, 10));
    }

    @Test
    void testGetSongsByArtist() {
        List<Song> songs = Arrays.asList(new Song());
        when(dao.getSongsByArtist(1)).thenReturn(songs);
        assertEquals(1, service.getSongsByArtist(1).size());
    }

    @Test
    void testGetUsersWhoFavoritedMySongs() {
        List<String> users = Arrays.asList("User1");
        when(dao.getUsersWhoFavoritedMySongs(1)).thenReturn(users);
        assertEquals(1, service.getUsersWhoFavoritedMySongs(1).size());
    }

    @Test
    void testIsSongOwnedByArtist() {
        when(dao.isSongOwnedByArtist(1, 10)).thenReturn(true);
        assertTrue(service.isSongOwnedByArtist(1, 10));
    }
}

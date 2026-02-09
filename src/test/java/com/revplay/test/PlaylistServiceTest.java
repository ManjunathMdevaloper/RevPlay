package com.revplay.test;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.revplay.dao.PlaylistDAO;
import com.revplay.model.Playlist;
import com.revplay.model.Song;
import com.revplay.service.PlaylistServiceImpl;

import java.lang.reflect.Field;
import java.util.Arrays;
import java.util.List;

@ExtendWith(MockitoExtension.class)
class PlaylistServiceTest {

    @Mock
    private PlaylistDAO dao;

    @InjectMocks
    private PlaylistServiceImpl service;

    @BeforeEach
    void setUp() throws Exception {
        Field field = PlaylistServiceImpl.class.getDeclaredField("playlistDAO");
        field.setAccessible(true);
        field.set(service, dao);
    }

    @Test
    void testCreatePlaylist() {
        Playlist p = new Playlist();
        service.createPlaylist(p);
        verify(dao).createPlaylist(p);
    }

    @Test
    void testGetMyPlaylists() {
        List<Playlist> list = Arrays.asList(new Playlist());
        when(dao.getPlaylistsByUser(1)).thenReturn(list);
        assertEquals(1, service.getMyPlaylists(1).size());
    }

    @Test
    void testAddSong() {
        when(dao.addSongToPlaylist(1, 10)).thenReturn(true);
        assertTrue(service.addSong(1, 10));
    }

    @Test
    void testRemoveSong() {
        when(dao.removeSongFromPlaylist(1, 10)).thenReturn(true);
        assertTrue(service.removeSong(1, 10));
    }

    @Test
    void testUpdatePlaylist() {
        Playlist p = new Playlist();
        service.updatePlaylist(p);
        verify(dao).updatePlaylist(p);
    }

    @Test
    void testDeletePlaylist() {
        when(dao.deletePlaylist(1, 10)).thenReturn(true);
        assertTrue(service.deletePlaylist(1, 10));
    }

    @Test
    void testGetPublicPlaylists() {
        List<Playlist> list = Arrays.asList(new Playlist());
        when(dao.getPublicPlaylists(1)).thenReturn(list);
        assertEquals(1, service.getPublicPlaylists(1).size());
    }

    @Test
    void testGetMyPlaylistsWithSongCount() {
        List<Playlist> list = Arrays.asList(new Playlist());
        when(dao.getMyPlaylistsWithSongCount(1)).thenReturn(list);
        assertEquals(1, service.getMyPlaylistsWithSongCount(1).size());
    }

    @Test
    void testGetSongsInPlaylist() {
        List<Song> list = Arrays.asList(new Song());
        when(dao.getSongsInPlaylist(1)).thenReturn(list);
        assertEquals(1, service.getSongsInPlaylist(1).size());
    }
}

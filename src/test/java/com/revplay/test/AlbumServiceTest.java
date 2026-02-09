package com.revplay.test;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.revplay.dao.AlbumDAO;
import com.revplay.model.Album;
import com.revplay.service.AlbumServiceImpl;

import java.lang.reflect.Field;
import java.util.Arrays;
import java.util.List;

@ExtendWith(MockitoExtension.class)
class AlbumServiceTest {

    @Mock
    private AlbumDAO dao;

    @InjectMocks
    private AlbumServiceImpl service;

    @BeforeEach
    void setUp() throws Exception {
        Field field = AlbumServiceImpl.class.getDeclaredField("dao");
        field.setAccessible(true);
        field.set(service, dao);
    }

    @Test
    void testGetAllAlbums() {
        List<Album> list = Arrays.asList(new Album());
        when(dao.getAllAlbums()).thenReturn(list);
        assertEquals(1, service.getAllAlbums().size());
    }

    @Test
    void testCreateAlbum() {
        Album album = new Album();
        service.createAlbum(album);
        verify(dao).createAlbum(album);
    }

    @Test
    void testGetMyAlbums() {
        List<Album> list = Arrays.asList(new Album());
        when(dao.getAlbumsByArtist(1)).thenReturn(list);
        assertEquals(1, service.getMyAlbums(1).size());
    }

    @Test
    void testUpdateAlbum() {
        Album album = new Album();
        service.updateAlbum(album);
        verify(dao).updateAlbum(album);
    }

    @Test
    void testDeleteAlbum() {
        service.deleteAlbum(1, 10);
        verify(dao).deleteAlbum(1, 10);
    }

    @Test
    void testDeleteSongsByAlbum() {
        service.deleteSongsByAlbum(1);
        verify(dao).deleteSongsByAlbum(1);
    }

    @Test
    void testDeleteFavoritesByAlbum() {
        service.deleteFavoritesByAlbum(1);
        verify(dao).deleteFavoritesByAlbum(1);
    }

    @Test
    void testDeletePlaylistSongsByAlbum() {
        service.deletePlaylistSongsByAlbum(1);
        verify(dao).deletePlaylistSongsByAlbum(1);
    }

    @Test
    void testDeleteListeningHistoryByAlbum() {
        service.deleteListeningHistoryByAlbum(1);
        verify(dao).deleteListeningHistoryByAlbum(1);
    }

    @Test
    void testHasSongs() {
        when(dao.hasSongs(1)).thenReturn(true);
        assertTrue(service.hasSongs(1));
    }
}

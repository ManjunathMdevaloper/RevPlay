package com.revplay.test;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.revplay.dao.ListeningHistoryDAO;
import com.revplay.dao.SongDAO;
import com.revplay.model.Song;
import com.revplay.service.SongServiceImpl;

import java.lang.reflect.Field;
import java.util.Arrays;
import java.util.List;

@ExtendWith(MockitoExtension.class)
class SongServiceTest {

    @Mock
    private SongDAO songDAO;

    @Mock
    private ListeningHistoryDAO historyDAO;

    @InjectMocks
    private SongServiceImpl songService;

    @BeforeEach
    void setUp() throws Exception {
        Field daoField = SongServiceImpl.class.getDeclaredField("songDAO");
        daoField.setAccessible(true);
        daoField.set(songService, songDAO);

        Field histField = SongServiceImpl.class.getDeclaredField("historyDAO");
        histField.setAccessible(true);
        histField.set(songService, historyDAO);
    }

    @Test
    void testGetAllSongs() {
        List<Song> songs = Arrays.asList(new Song(), new Song());
        when(songDAO.getAllSongs()).thenReturn(songs);
        assertEquals(2, songService.getAllSongs().size());
    }

    @Test
    void testPlaySong_Success() {
        Song mockSong = new Song();
        mockSong.setTitle("Test Song");

        when(songDAO.getSongById(1)).thenReturn(mockSong);

        Song result = songService.playSong(10, 1);

        assertNotNull(result);
        assertEquals("Test Song", result.getTitle());
        verify(songDAO).incrementPlayCount(1);
        verify(historyDAO).addHistory(10, 1);
    }

    @Test
    void testPlaySong_NotFound() {
        when(songDAO.getSongById(999)).thenReturn(null);
        assertNull(songService.playSong(10, 999));
    }

    @Test
    void testGetSongById() {
        Song song = new Song();
        when(songDAO.getSongById(1)).thenReturn(song);
        assertEquals(song, songService.getSongById(1));
    }

    @Test
    void testSearchSongs() {
        List<Song> songs = Arrays.asList(new Song());
        when(songDAO.searchSongs("pop")).thenReturn(songs);
        assertEquals(1, songService.searchSongs("pop").size());
    }

    @Test
    void testGetSongsByGenre() {
        List<Song> songs = Arrays.asList(new Song());
        when(songDAO.getSongsByGenre("Rock")).thenReturn(songs);
        assertEquals(1, songService.getSongsByGenre("Rock").size());
    }

    @Test
    void testUploadSong() {
        Song song = new Song();
        songService.uploadSong(song);
        verify(songDAO).uploadSong(song);
    }

    @Test
    void testGetSongsByArtist() {
        List<Song> songs = Arrays.asList(new Song());
        when(songDAO.getSongsByArtist(5)).thenReturn(songs);
        assertEquals(1, songService.getSongsByArtist(5).size());
    }

    @Test
    void testGetSongsByAlbum() {
        List<Song> songs = Arrays.asList(new Song());
        when(songDAO.getSongsByAlbum(2)).thenReturn(songs);
        assertEquals(1, songService.getSongsByAlbum(2).size());
    }

    @Test
    void testGetAllGenres() {
        List<String> genres = Arrays.asList("Pop", "Jazz");
        when(songDAO.getAllGenres()).thenReturn(genres);
        assertEquals(2, songService.getAllGenres().size());
    }
}

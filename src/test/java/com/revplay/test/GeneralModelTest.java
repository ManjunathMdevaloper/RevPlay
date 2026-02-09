package com.revplay.test;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.Test;
import com.revplay.model.*;
import java.sql.Date;

public class GeneralModelTest {

    @Test
    public void testUserModel() {
        Date now = new Date(System.currentTimeMillis());
        User user = new User(1, "Test", "test@test.com", "pass", "USER", "Q", "A", "Hint", now);
        new User();

        assertEquals(1, user.getUserId());
        assertEquals("Test", user.getName());
        assertEquals("test@test.com", user.getEmail());
        assertEquals("pass", user.getPassword());
        assertEquals("USER", user.getRole());
        assertEquals("Q", user.getSecurityQuestion());
        assertEquals("A", user.getSecurityAnswer());
        assertEquals("Hint", user.getPasswordHint());
        assertEquals(now, user.getCreatedAt());

        user.setUserId(2);
        user.setName("N");
        user.setEmail("E");
        user.setPassword("P");
        user.setRole("R");
        user.setSecurityQuestion("SQ");
        user.setSecurityAnswer("SA");
        user.setPasswordHint("PH");
        user.setCreatedAt(now);

        assertEquals(2, user.getUserId());
        assertEquals("N", user.getName());
        assertEquals("E", user.getEmail());
        assertEquals("P", user.getPassword());
        assertEquals("R", user.getRole());
        assertEquals("SQ", user.getSecurityQuestion());
        assertEquals("SA", user.getSecurityAnswer());
        assertEquals("PH", user.getPasswordHint());
    }

    @Test
    public void testAlbumModel() {
        Date now = new Date(System.currentTimeMillis());
        Album album = new Album(1, "Title", now, 10);
        new Album();

        assertEquals(1, album.getAlbumId());
        assertEquals("Title", album.getTitle());
        assertEquals(now, album.getReleaseDate());
        assertEquals(10, album.getArtistId());

        album.setAlbumId(2);
        album.setTitle("New");
        album.setReleaseDate(now);
        album.setArtistId(11);

        assertEquals(2, album.getAlbumId());
        assertEquals("New", album.getTitle());
        assertEquals(now, album.getReleaseDate());
        assertEquals(11, album.getArtistId());
    }

    @Test
    public void testSongModel() {
        Date now = new Date(System.currentTimeMillis());
        Song song = new Song(1, "Title", "Genre", 300, now, 100, 5, 2);
        new Song();

        assertEquals(1, song.getSongId());
        assertEquals("Title", song.getTitle());
        assertEquals("Genre", song.getGenre());
        assertEquals(300, song.getDuration());
        assertEquals(now, song.getReleaseDate());
        assertEquals(100, song.getPlayCount());
        assertEquals(5, song.getArtistId());
        assertEquals(2, song.getAlbumId());

        song.setSongId(2);
        song.setTitle("T");
        song.setGenre("G");
        song.setDuration(301);
        song.setReleaseDate(now);
        song.setPlayCount(101);
        song.setArtistId(6);
        song.setAlbumId(3);

        assertEquals(2, song.getSongId());
        assertEquals("T", song.getTitle());
        assertEquals("G", song.getGenre());
        assertEquals(301, song.getDuration());
        assertEquals(101, song.getPlayCount());
        assertEquals(6, song.getArtistId());
        assertEquals(3, song.getAlbumId());
    }

    @Test
    public void testArtistModel() {
        Artist artist = new Artist(1, 10, "Bio", "Genre", "Links");
        new Artist();

        assertEquals(1, artist.getArtistId());
        assertEquals(10, artist.getUserId());
        assertEquals("Bio", artist.getBio());
        assertEquals("Genre", artist.getGenre());
        assertEquals("Links", artist.getSocialLinks());

        artist.setArtistId(2);
        artist.setUserId(11);
        artist.setBio("New Bio");
        artist.setGenre("Rock");
        artist.setSocialLinks("L");
        artist.setArtistName("Name");

        assertEquals(2, artist.getArtistId());
        assertEquals(11, artist.getUserId());
        assertEquals("New Bio", artist.getBio());
        assertEquals("Rock", artist.getGenre());
        assertEquals("L", artist.getSocialLinks());
        assertEquals("Name", artist.getArtistName());
    }

    @Test
    public void testPodcastModel() {
        Date now = new Date(System.currentTimeMillis());
        Podcast podcast = new Podcast(1, 5, "Title", "Host", "Cat", "Desc", now);
        new Podcast();

        assertEquals(1, podcast.getPodcastId());
        assertEquals(5, podcast.getArtistId());
        assertEquals("Title", podcast.getTitle());
        assertEquals("Host", podcast.getHostName());
        assertEquals("Cat", podcast.getCategory());
        assertEquals("Desc", podcast.getDescription());
        assertEquals(now, podcast.getCreatedAt());

        podcast.setPodcastId(2);
        podcast.setArtistId(6);
        podcast.setTitle("New Podcast");
        podcast.setHostName("H");
        podcast.setCategory("C");
        podcast.setDescription("D");
        podcast.setCreatedAt(now);

        assertEquals(2, podcast.getPodcastId());
        assertEquals(6, podcast.getArtistId());
        assertEquals("New Podcast", podcast.getTitle());
        assertEquals("H", podcast.getHostName());
        assertEquals("C", podcast.getCategory());
        assertEquals("D", podcast.getDescription());
    }

    @Test
    public void testPodcastEpisodeModel() {
        Date now = new Date(System.currentTimeMillis());
        PodcastEpisode ep = new PodcastEpisode(1, 10, "Title", 3600, now, 500);
        new PodcastEpisode();

        assertEquals(1, ep.getEpisodeId());
        assertEquals(10, ep.getPodcastId());
        assertEquals("Title", ep.getTitle());
        assertEquals(3600, ep.getDuration());
        assertEquals(now, ep.getReleaseDate());
        assertEquals(500, ep.getPlayCount());

        ep.setEpisodeId(2);
        ep.setPodcastId(11);
        ep.setTitle("T");
        ep.setDuration(3601);
        ep.setReleaseDate(now);
        ep.setPlayCount(501);

        assertEquals(2, ep.getEpisodeId());
        assertEquals(11, ep.getPodcastId());
        assertEquals("T", ep.getTitle());
        assertEquals(3601, ep.getDuration());
        assertEquals(501, ep.getPlayCount());
    }

    @Test
    public void testPlaylistModel() {
        Playlist p = new Playlist(1, "Name", "Desc", "Y", 10);
        new Playlist();

        assertEquals(1, p.getPlaylistId());
        assertEquals("Name", p.getName());
        assertEquals("Desc", p.getDescription());
        assertEquals("Y", p.getIsPublic());
        assertEquals(10, p.getUserId());

        p.setPlaylistId(2);
        p.setName("New Name");
        p.setDescription("D");
        p.setIsPublic("N");
        p.setUserId(11);
        p.setOwnerName("Owner");
        p.setSongCount(5);

        assertEquals(2, p.getPlaylistId());
        assertEquals("New Name", p.getName());
        assertEquals("D", p.getDescription());
        assertEquals("N", p.getIsPublic());
        assertEquals(11, p.getUserId());
        assertEquals("Owner", p.getOwnerName());
        assertEquals(5, p.getSongCount());
    }

    @Test
    public void testFavoriteModel() {
        Date now = new Date(System.currentTimeMillis());
        Favorite f = new Favorite(1, 10, now);
        new Favorite();

        assertEquals(1, f.getUserId());
        assertEquals(10, f.getSongId());
        assertEquals(now, f.getAddedAt());

        f.setUserId(2);
        f.setSongId(11);
        f.setAddedAt(now);

        assertEquals(2, f.getUserId());
        assertEquals(11, f.getSongId());
    }

    @Test
    public void testListeningHistoryModel() {
        Date now = new Date(System.currentTimeMillis());
        ListeningHistory h = new ListeningHistory(1, 10, 20, now);
        new ListeningHistory();

        assertEquals(1, h.getHistoryId());
        assertEquals(10, h.getUserId());
        assertEquals(20, h.getSongId());
        assertEquals(now, h.getPlayedAt());

        h.setHistoryId(2);
        h.setUserId(11);
        h.setSongId(21);
        h.setPlayedAt(now);

        assertEquals(2, h.getHistoryId());
        assertEquals(11, h.getUserId());
        assertEquals(21, h.getSongId());
    }
}

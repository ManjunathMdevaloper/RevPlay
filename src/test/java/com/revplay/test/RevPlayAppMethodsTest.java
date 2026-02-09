package com.revplay.test;

import static org.mockito.Mockito.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.junit.jupiter.MockitoSettings;
import org.mockito.quality.Strictness;

import com.revplay.main.RevPlayAppMethonds;
import com.revplay.service.*;
import com.revplay.dao.*;
import com.revplay.model.*;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.lang.reflect.Field;
import java.util.Scanner;
import java.util.Arrays;
import java.util.ArrayList;

@ExtendWith(MockitoExtension.class)
@MockitoSettings(strictness = Strictness.LENIENT)
class RevPlayAppMethodsTest {

    @Mock
    private UserService userService;
    @Mock
    private SongService songService;
    @Mock
    private FavoriteDAO favoriteDAO;
    @Mock
    private PlaylistService playlistService;
    @Mock
    private ListeningHistoryDAO historyDAO;
    @Mock
    private PodcastService podcastService;
    @Mock
    private ArtistService artistService;
    @Mock
    private AlbumService albumService;
    @Mock
    private ArtistSongService artistSongService;

    private void setStaticField(String name, Object value) throws Exception {
        Field field = RevPlayAppMethonds.class.getDeclaredField(name);
        field.setAccessible(true);
        field.set(null, value);
    }

    @BeforeEach
    void setUp() throws Exception {
        setStaticField("userService", userService);
        setStaticField("songService", songService);
        setStaticField("favoriteDAO", favoriteDAO);
        setStaticField("playlistService", playlistService);
        setStaticField("historyDAO", historyDAO);
        setStaticField("podcastService", podcastService);
        setStaticField("artistService", artistService);
        setStaticField("albumService", albumService);
        setStaticField("artistSongService", artistSongService);
    }

    private void runWithInput(String input) throws Exception {
        InputStream in = new ByteArrayInputStream(input.getBytes());
        setStaticField("sc", new Scanner(in));
    }

    @Test
    void testHandleLogin_AllFlows() throws Exception {
        // User Login
        runWithInput("user@test.com\npass\n22\n");
        User user = new User(1, "U", "user@test.com", "pass", "USER", "Q", "A", "H", null);
        when(userService.login("user@test.com", "pass")).thenReturn(user);
        RevPlayAppMethonds.handleLogin();

        // Artist Login
        runWithInput("artist@test.com\npass\n21\n");
        User artistUser = new User(2, "A", "artist@test.com", "pass", "ARTIST", "Q", "A", "H", null);
        when(userService.login("artist@test.com", "pass")).thenReturn(artistUser);
        RevPlayAppMethonds.handleLogin();

        // Fail Login
        runWithInput("wrong\npass\n");
        when(userService.login("wrong", "pass")).thenReturn(null);
        RevPlayAppMethonds.handleLogin();
    }

    @Test
    void testHandleRegister_Full() throws Exception {
        runWithInput("N\nE\nP\nQ\nA\nH\n");
        when(userService.registerUser(any())).thenReturn(true);
        RevPlayAppMethonds.handleRegister();

        runWithInput("N\nE\nP\nQ\nA\nH\n");
        when(userService.registerUser(any())).thenReturn(false);
        RevPlayAppMethonds.handleRegister();
    }

    @Test
    void testHandleForgotPassword_Full() throws Exception {
        runWithInput("e\nA\nnew\n");
        when(userService.getSecurityQuestion("e")).thenReturn("Q");
        when(userService.getPasswordHint("e")).thenReturn("H");
        when(userService.validateSecurityAnswer("e", "A")).thenReturn(true);
        when(userService.updatePassword("e", "new")).thenReturn(true);
        RevPlayAppMethonds.handleForgotPassword();

        runWithInput("wrong\n");
        when(userService.getSecurityQuestion("wrong")).thenReturn(null);
        RevPlayAppMethonds.handleForgotPassword();
    }

    @Test
    void testUserDashboard_MegaFlow() throws Exception {
        // 1(View), 2(View Pods), 5(Search), pop, 6(Search Pod), crime, 7(Global), all,
        // 17(Genre), Rock, 18(Artist), 101, 19(Album), 201, 20(History), 21(Recent),
        // 10(Create), N, D, Y, 11(View), 16(Public), 22(Logout)
        runWithInput("1\n2\n5\npop\n6\ncrime\n7\nall\n17\nRock\n18\n101\n19\n201\n20\n21\n10\nN\nD\nY\n11\n16\n22\n");

        User user = new User();
        user.setUserId(1);
        user.setRole("USER");

        when(songService.getAllSongs()).thenReturn(Arrays.asList(new Song()));
        when(podcastService.getAllPodcasts()).thenReturn(Arrays.asList(new Podcast()));
        when(songService.searchSongs(anyString())).thenReturn(new ArrayList<>());
        when(podcastService.searchPodcasts(anyString())).thenReturn(new ArrayList<>());
        when(songService.getAllGenres()).thenReturn(Arrays.asList("Rock"));
        when(songService.getSongsByGenre("Rock")).thenReturn(new ArrayList<>());
        when(artistService.getAllArtists()).thenReturn(new ArrayList<>());
        when(songService.getSongsByArtist(anyInt())).thenReturn(new ArrayList<>());
        when(albumService.getAllAlbums()).thenReturn(new ArrayList<>());
        when(historyDAO.getListeningHistory(anyInt())).thenReturn(new ArrayList<>());
        when(historyDAO.getRecentPlays(anyInt(), anyInt())).thenReturn(new ArrayList<>());
        when(playlistService.getMyPlaylistsWithSongCount(1)).thenReturn(new ArrayList<>());
        when(playlistService.getPublicPlaylists(1)).thenReturn(new ArrayList<>());

        RevPlayAppMethonds.userDashboard(user);
    }

    @Test
    void testUserDashboard_ControlsAndPlaylist() throws Exception {
        // 3(Play), 1, 1(Pause), 2(Resume), 3(Repeat), 4(Skip), 5(Stop)
        // 4(Play Pod), 601, 701, 1(Pause), 2(Resume), 3(Stop)
        // 8(Add Fav), 301. 9(View Fav)
        // 12(Add to Playlist), 501, 301. 13(Remove), 501, 301. 14(Update), 501, N, D,
        // Y. 15(Delete), 501. 22(Logout)
        runWithInput(
                "3\n1\n1\n2\n3\n4\n5\n4\n601\n701\n1\n2\n3\n8\n301\n9\n12\n501\n301\n13\n501\n301\n14\n501\nN\nD\nY\n15\n501\n22\n");

        User user = new User();
        user.setUserId(1);
        user.setRole("USER");

        Song song = new Song();
        song.setTitle("S");
        when(songService.playSong(anyInt(), anyInt())).thenReturn(song);
        Podcast pod = new Podcast();
        pod.setPodcastId(601);
        when(podcastService.getAllPodcasts()).thenReturn(Arrays.asList(pod));
        PodcastEpisode ep = new PodcastEpisode();
        ep.setEpisodeId(701);
        when(podcastService.getEpisodesByPodcast(601)).thenReturn(Arrays.asList(ep));
        when(podcastService.playEpisode(anyInt(), anyInt())).thenReturn(ep);
        when(songService.getSongById(301)).thenReturn(new Song());
        when(favoriteDAO.getFavoriteSongs(1)).thenReturn(new ArrayList<>());

        Playlist pl = new Playlist();
        pl.setPlaylistId(501);
        when(playlistService.getMyPlaylists(1)).thenReturn(Arrays.asList(pl));
        when(songService.getAllSongs()).thenReturn(Arrays.asList(new Song()));
        when(playlistService.getSongsInPlaylist(501)).thenReturn(Arrays.asList(new Song()));
        when(playlistService.addSong(501, 301)).thenReturn(true);
        when(playlistService.removeSong(501, 301)).thenReturn(true);
        when(playlistService.deletePlaylist(501, 1)).thenReturn(true);

        RevPlayAppMethonds.userDashboard(user);
    }

    @Test
    void testArtistDashboard_MegaFlow() throws Exception {
        // 1(View), 2(Update), B, G, L.
        // 3(Create Alb), T, 2024-01-01. 4(View). 5(Update Alb), 201, T, 2024-01-02.
        // 6(Delete Alb), 201.
        // 7(Upload Song), 201, T, G, 300. 8(View Songs). 9(Update Song), 301, T, G,
        // 300, 201. 10(Delete), 301.
        // 11(Create Pod), T, H, C. 12(View Pods). 13(Update Pod), 601, T, H, C.
        // 14(Delete Pod), 601.
        // 15(Add Ep), 601, T, D, 2024-01-01. 16(View Eps). 17(Update Ep), 701, T, D.
        // 18(Delete Ep), 701.
        // 19(Stats). 20(Favs). 21(Logout)
        String inputs = "1\n2\nB\nG\nL\n" +
                "3\nT\n2024-01-01\n4\n5\n201\nT\n2024-01-02\n6\n201\n" +
                "7\n201\nT\nG\n300\n8\n9\n301\nT\nG\n300\n201\n10\n301\n" +
                "11\nT\nH\nC\n12\n13\n601\nT\nH\nC\n14\n601\nY\n" +
                "15\n601\nT\n300\n2024-01-01\n16\n" +
                "17\n601\n701\nT\n300\n2024-01-02\n" +
                "18\n601\n701\nY\n" +
                "19\n20\n21\n";
        runWithInput(inputs);

        User user = new User();
        user.setUserId(101);
        user.setRole("ARTIST");

        Artist artist = new Artist();
        artist.setArtistId(5);
        when(artistService.getArtistByUserId(101)).thenReturn(artist);
        when(albumService.getMyAlbums(5)).thenReturn(Arrays.asList(new Album(201, "A", null, 5)));
        when(artistSongService.getSongsByArtist(5))
                .thenReturn(Arrays.asList(new Song(301, "S", "G", 300, null, 0, 5, 201)));
        when(artistSongService.isSongOwnedByArtist(anyInt(), anyInt())).thenReturn(true);
        Podcast pod = new Podcast();
        pod.setPodcastId(601);
        when(podcastService.getPodcastsByArtist(5)).thenReturn(Arrays.asList(pod));
        PodcastEpisode ep = new PodcastEpisode();
        ep.setEpisodeId(701);
        when(podcastService.getEpisodesByPodcast(601)).thenReturn(Arrays.asList(ep));
        when(artistService.getSongStatistics(5)).thenReturn(new ArrayList<>());
        when(artistSongService.getUsersWhoFavoritedMySongs(5)).thenReturn(new ArrayList<>());

        RevPlayAppMethonds.artistDashboard(user);
    }

    @Test
    void testNegativeAndEdgeCases() throws Exception {
        // 1. Artist not found error
        runWithInput("21\n");
        User artistUser = new User();
        artistUser.setUserId(999);
        artistUser.setRole("ARTIST");
        when(artistService.getArtistByUserId(999)).thenReturn(null);
        RevPlayAppMethonds.artistDashboard(artistUser);

        // 2. Empty lists in Artist Dashboard
        Artist artist = new Artist();
        artist.setArtistId(77);
        when(artistService.getArtistByUserId(999)).thenReturn(artist);
        when(albumService.getMyAlbums(77)).thenReturn(new ArrayList<>());
        when(artistSongService.getSongsByArtist(77)).thenReturn(new ArrayList<>());
        when(podcastService.getPodcastsByArtist(77)).thenReturn(new ArrayList<>());

        // Inputs: 4(View Alb), 8(View Songs), 12(View Pods), 16(View Eps), 21(Logout)
        runWithInput("4\n8\n12\n16\n21\n");
        RevPlayAppMethonds.artistDashboard(artistUser);

        // 3. User Dashboard search not found
        runWithInput("5\nnonexistent\n22\n");
        User user = new User();
        user.setUserId(1);
        user.setRole("USER");
        when(songService.searchSongs("nonexistent")).thenReturn(new ArrayList<>());
        RevPlayAppMethonds.userDashboard(user);

        // 4. Update non-existent IDs
        runWithInput("14\n999\n21\n"); // Update Album 999
        when(albumService.getMyAlbums(77)).thenReturn(new ArrayList<>());
        RevPlayAppMethonds.artistDashboard(artistUser);
    }
}

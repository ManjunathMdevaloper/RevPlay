
package com.revplay.main;

import java.util.List;
import java.util.Scanner;

import com.revplay.dao.FavoriteDAO;
import com.revplay.dao.FavoriteDAOImpl;
import com.revplay.dao.ListeningHistoryDAO;
import com.revplay.dao.ListeningHistoryDAOImpl;
import com.revplay.model.Album;
import com.revplay.model.Artist;
import com.revplay.model.Playlist;
import com.revplay.model.Podcast;
import com.revplay.model.PodcastEpisode;
import com.revplay.model.Song;
import com.revplay.model.User;
import com.revplay.service.AlbumService;
import com.revplay.service.AlbumServiceImpl;
import com.revplay.service.ArtistService;
import com.revplay.service.ArtistServiceImpl;
import com.revplay.service.ArtistSongService;
import com.revplay.service.ArtistSongServiceImpl;
import com.revplay.service.PlaylistService;
import com.revplay.service.PlaylistServiceImpl;
import com.revplay.service.SongService;
import com.revplay.service.SongServiceImpl;
import com.revplay.service.UserService;
import com.revplay.service.UserServiceImpl;
import com.revplay.service.PodcastService;
import com.revplay.service.PodcastServiceImpl;

public class RevPlayAppMethonds {
	private static Scanner sc = new Scanner(System.in);
	private static UserService userService = new UserServiceImpl();
	private static SongService songService = new SongServiceImpl();
	private static FavoriteDAO favoriteDAO = new FavoriteDAOImpl();
	private static PlaylistService playlistService = new PlaylistServiceImpl();
	private static ListeningHistoryDAO historyDAO = new ListeningHistoryDAOImpl();
	private static PodcastService podcastService = new PodcastServiceImpl();
	private static ArtistService artistService = new ArtistServiceImpl();
	private static AlbumService albumService = new AlbumServiceImpl();
	private static ArtistSongService artistSongService = new ArtistSongServiceImpl();

	// ----------------Register and Login---------------------
	public static void handleArtistRegister() {
		User user = new User();
		System.out.print("Enter Name: ");
		user.setName(sc.nextLine());
		System.out.print("Enter Email: ");
		user.setEmail(sc.nextLine());
		System.out.print("Enter Password: ");
		user.setPassword(sc.nextLine());
		System.out.print("Enter Security Question: ");
		user.setSecurityQuestion(sc.nextLine());
		System.out.print("Enter Security Answer: ");
		user.setSecurityAnswer(sc.nextLine());
		System.out.print("Enter Password Hint: ");
		user.setPasswordHint(sc.nextLine());

		// register in USERS table as ARTIST
		boolean registered = userService.registerArtist(user);
		if (!registered) {
			System.out.println("❌ Registration failed. Email may already be associated with an account.");
			return;
		}

		// now insert artist profile
		Artist artist = new Artist();
		System.out.print("Enter Bio: ");
		artist.setBio(sc.nextLine());
		System.out.print("Enter Genre: ");
		artist.setGenre(sc.nextLine());
		System.out.print("Enter Social Links: ");
		artist.setSocialLinks(sc.nextLine());

		// get newly created user id
		int artistUserId = userService.getUserIdByEmail(user.getEmail());
		artist.setUserId(artistUserId);

		artistService.saveOrUpdateArtist(artist);

		System.out.println("✅ Artist registration successful! Please login.");
	}

	public static void handleRegister() {
		User user = new User();
		System.out.print("Enter Name: ");
		user.setName(sc.nextLine());
		System.out.print("Enter Email: ");
		user.setEmail(sc.nextLine());
		System.out.print("Enter Password: ");
		user.setPassword(sc.nextLine());
		System.out.print("Enter Security Question: ");
		user.setSecurityQuestion(sc.nextLine());
		System.out.print("Enter Security Answer: ");
		user.setSecurityAnswer(sc.nextLine());
		System.out.print("Enter Password Hint: ");
		user.setPasswordHint(sc.nextLine());

		user.setRole("USER"); // Default role

		boolean success = userService.registerUser(user);

		if (!success) {
			System.out.println("❌ You are already registered. Please login.");
		} else {
			System.out.println("✅ Registration successful! Please login.");
		}
	}

	public static void handleForgotPassword() {
		System.out.print("Enter registered email: ");
		String email = sc.nextLine();
		String question = userService.getSecurityQuestion(email);

		if (question == null) {
			System.out.println("❌ Email not found");
			return;
		}

		// 🔹 Show security question
		System.out.println("Security Question: " + question);

		// 🔹 Show password hint BEFORE answer
		String hint = userService.getPasswordHint(email);
		System.out.println("Password Hint: " + hint);

		System.out.print("Enter your answer: ");
		String answer = sc.nextLine();

		boolean valid = userService.validateSecurityAnswer(email, answer);

		if (valid) {
			System.out.print("Enter your new password: ");
			String newPassword = sc.nextLine();
			boolean updated = userService.updatePassword(email, newPassword);
			if (updated) {
				System.out.println("✅ Password updated successfully! Please login with your new password.");
			} else {
				System.out.println("❌ Failed to update password. Please try again.");
			}
		} else {
			System.out.println("❌ Incorrect answer");
		}
	}

	public static void handleLogin() {
		System.out.print("Enter Email: ");
		String email = sc.nextLine();
		System.out.print("Enter Password: ");
		String password = sc.nextLine();

		User user = userService.login(email, password);

		if (user == null) {
			System.out.println("Invalid credentials ❌");
			return;
		}

		System.out.println("\nLogin Successful!");
		System.out.println("Welcome " + user.getName());
		System.out.println("Role: " + user.getRole());

		if ("USER".equalsIgnoreCase(user.getRole())) {
			userDashboard(user);
		} else if ("ARTIST".equalsIgnoreCase(user.getRole())) {
			artistDashboard(user);
		}
	}

	// ---------------------User Dash Borad------------------------
	public static void userDashboard(User user) {
		int option = 0;
		while (option != 22) {
			System.out.println("\n--- USER DASHBOARD ---");
			System.out.println("1. View All Songs");
			System.out.println("2. View All Podcasts");
			System.out.println("----------------------");
			System.out.println("3. Play Song (with Controls)");
			System.out.println("4. Play Podcast (with Controls)");
			System.out.println("----------------------");
			System.out.println("5. Search Songs");
			System.out.println("6. Search Podcasts");
			System.out.println("----------------------");
			System.out.println("7. Global Search (Songs & PodCasts)");
			System.out.println("8. Add Song to Favorites");
			System.out.println("9. View Favorite Songs");
			System.out.println("----------------------");
			System.out.println("10. Create Playlist");
			System.out.println("11. View My Playlists");
			System.out.println("12. Add Song to Playlist");
			System.out.println("13. Remove Song from Playlist");
			System.out.println("14. Update Playlist");
			System.out.println("15. Delete Playlist");
			System.out.println("16. View Public Playlists");
			System.out.println("----------------------");
			System.out.println("17. Browse Songs by Genre");
			System.out.println("18. Browse Songs by Artist");
			System.out.println("19. Browse Songs by Album");
			System.out.println("----------------------");
			System.out.println("20. View Listening History");
			System.out.println("21. View Recently Played Songs");
			System.out.println("22. Logout");

			System.out.print("Choose option: ");
			option = sc.nextInt();
			sc.nextLine(); // consume newline

			switch (option) {
				case 1:
					viewAllSongs();
					break;
				case 2:
					viewAllPodcasts();
					break;
				case 3:
					playSongWithControls(user);
					break;
				case 4:
					playPodcastWithControls(user);
					break;
				case 5:
					searchSongs();
					break;
				case 6:
					handleSearchPodcasts();
					break;
				case 7:
					handleGlobalSearch();
					break;
				case 8:
					addToFavorites(user);
					break;
				case 9:
					viewFavoriteSongs(user);
					break;
				case 10:
					createPlaylist(user);
					break;
				case 11:
					viewMyPlaylists(user);
					break;
				case 12:
					addSongToPlaylist(user);
					break;
				case 13:
					removeSongFromPlaylist(user);
					break;
				case 14:
					updatePlaylist(user);
					break;
				case 15:
					deletePlaylist(user);
					break;
				case 16:
					viewPublicPlaylists(user);
					break;
				case 17:
					browseSongsByGenre();
					break;
				case 18:
					browseSongsByArtist();
					break;
				case 19:
					browseSongsByAlbum();
					break;
				case 20:
					viewListeningHistory(user);
					break;
				case 21:
					viewRecentPlays(user);
					break;
				case 22:
					System.out.println("Logged out 👋");
					option = 22;
					break;
				default:
					System.out.println("Invalid option");
			}
		}
	}

	private static void browseSongsByAlbum() {

		System.out.println("\n--- Available Albums ---");

		List<Album> albums = albumService.getAllAlbums();

		if (albums.isEmpty()) {
			System.out.println("No albums available");
			return;
		}

		// show albums
		albums.forEach(a -> System.out.println(
				"Album ID: " + a.getAlbumId() +
						" | Title: " + a.getTitle() +
						" | Release: " + a.getReleaseDate()));

		System.out.print("\nEnter Album ID to view songs: ");
		int albumId = sc.nextInt();
		sc.nextLine();

		System.out.println("\n--- Songs in Selected Album ---");

		List<Song> songs = songService.getSongsByAlbum(albumId);

		if (songs.isEmpty()) {
			System.out.println("No songs found in this album");
			return;
		}

		songs.forEach(s -> System.out.println(
				s.getSongId() + " | " +
						s.getTitle() + " | " +
						s.getGenre() + " | Plays: " +
						s.getPlayCount()));
	}

	private static void browseSongsByArtist() {

		System.out.println("\n--- Available Artists ---");

		List<Artist> artists = artistService.getAllArtists();

		if (artists.isEmpty()) {
			System.out.println("No artists available");
			return;
		}

		// show artists
		artists.forEach(a -> System.out.println(
				"Artist ID: " + a.getArtistId() +
						" | Name: " + a.getArtistName() +
						" | Genre: " + a.getGenre() +
						" | Bio: " + a.getBio()));

		System.out.print("\nEnter Artist ID to view songs: ");
		int artistId = sc.nextInt();
		sc.nextLine();

		System.out.println("\n--- Songs by Selected Artist ---");

		List<Song> songs = songService.getSongsByArtist(artistId);

		if (songs.isEmpty()) {
			System.out.println("No songs found for this artist");
			return;
		}

		songs.forEach(s -> System.out.println(
				s.getSongId() + " | " +
						s.getTitle() + " | " +
						s.getGenre() + " | Plays: " +
						s.getPlayCount()));
	}

	private static void playSongWithControls(User user) {
		System.out.println("\n--- Available Songs ---");
		viewAllSongs();

		System.out.print("Enter Song ID to play: ");
		int songId = sc.nextInt();
		sc.nextLine();

		Song song = songService.playSong(user.getUserId(), songId);

		if (song == null) {
			System.out.println("❌ Song not found");
			return;
		}

		System.out.println("▶ Playing: " + song.getTitle());

		boolean playing = true;
		int currentSongId = songId;

		while (playing) {
			System.out.println("\n--- Player Controls ---");
			System.out.println("1. Pause");
			System.out.println("2. Resume");
			System.out.println("3. Repeat");
			System.out.println("4. Skip");
			System.out.println("5. Stop");
			System.out.print("Choose option: ");
			int choice = sc.nextInt();
			sc.nextLine();

			switch (choice) {

				case 1:
					System.out.println("⏸ Paused");
					break;

				case 2:
					System.out.println("▶ Resumed: " + song.getTitle());
					break;

				case 3:
					System.out.println("🔁 Repeating: " + song.getTitle());
					// optional: don’t increment play count again
					break;

				case 4:
					currentSongId++;
					Song nextSong = songService.playSong(user.getUserId(), currentSongId);

					if (nextSong != null) {
						song = nextSong;
						System.out.println("⏭ Skipped to: " + song.getTitle());
					} else {
						System.out.println("❌ No next song available");
						currentSongId--; // rollback
					}
					break;

				case 5:
					System.out.println("⏹ Stopped");
					playing = false;
					break;

				default:
					System.out.println("Invalid option");
			}
		}
	}

	private static void playPodcastWithControls(User user) {
		System.out.println("\n--- Available Podcasts ---");
		List<Podcast> podcasts = podcastService.getAllPodcasts();
		if (podcasts.isEmpty()) {
			System.out.println("No podcasts found in database");
			return;
		}
		podcasts.forEach(p -> System.out.println(p.getPodcastId() + " | " + p.getTitle() + " | Host: " + p.getHostName()
				+ " | Category: " + p.getCategory()));

		System.out.print("\nEnter Podcast ID to view episodes: ");
		int podcastId = sc.nextInt();
		sc.nextLine();

		System.out.println("\n--- Available Episodes ---");
		List<PodcastEpisode> episodes = podcastService.getEpisodesByPodcast(podcastId);
		if (episodes.isEmpty()) {
			System.out.println("❌ No episodes found for this podcast (ID: " + podcastId + ")");
			return;
		}
		episodes.forEach(
				e -> System.out.println(e.getEpisodeId() + " | " + e.getTitle() + " | Release: " + e.getReleaseDate()));

		System.out.print("\nEnter Episode ID to play: ");
		int episodeId = sc.nextInt();
		sc.nextLine();
		PodcastEpisode episode = podcastService.playEpisode(user.getUserId(), episodeId);

		if (episode == null) {
			System.out.println("❌ Episode not found (ID: " + episodeId + ")");
			return;
		}

		System.out.println("▶ Playing Podcast: " + episode.getTitle());

		boolean playing = true;
		while (playing) {
			System.out.println("\n--- Podcast Controls ---");
			System.out.println("1. Pause");
			System.out.println("2. Resume");
			System.out.println("3. Stop");
			System.out.print("Choose option: ");
			int choice = sc.nextInt();
			sc.nextLine();

			switch (choice) {
				case 1:
					System.out.println("⏸ Paused");
					break;
				case 2:
					System.out.println("▶ Resumed: " + episode.getTitle());
					break;
				case 3:
					System.out.println("⏹ Stopped");
					playing = false;
					break;
				default:
					System.out.println("Invalid option");
			}
		}
	}

	private static void addToFavorites(User user) {
		System.out.print("Enter Song ID to favorite: ");
		int songId = sc.nextInt();
		sc.nextLine();

		// Check if song exists first to provide better feedback
		if (songService.getSongById(songId) == null) {
			System.out.println("❌ Song with ID " + songId + " not found.");
			return;
		}

		favoriteDAO.addToFavorites(user.getUserId(), songId);
	}

	private static void createPlaylist(User user) {

		System.out.print("Playlist Name: ");
		String pname = sc.nextLine();
		System.out.print("Description: ");
		String desc = sc.nextLine();
		System.out.print("Public? (Y/N): ");
		String pub = sc.nextLine();

		Playlist playlist = new Playlist();
		playlist.setName(pname);
		playlist.setDescription(desc);
		playlist.setIsPublic(pub);
		playlist.setUserId(user.getUserId());

		playlistService.createPlaylist(playlist);
		System.out.println("✅ Playlist created");
	}

	private static void viewMyPlaylists(User user) {
		List<Playlist> playlists = playlistService.getMyPlaylistsWithSongCount(user.getUserId());
		if (playlists.isEmpty()) {
			System.out.println("❌ No playlist found. Create your playlist!");
		} else {
			playlists.forEach(p -> System.out.println(p.getPlaylistId()
					+ " | " + p.getName() + " | Songs: " + p.getSongCount() + " | Public: " + p.getIsPublic()));
		}
	}

	private static void addSongToPlaylist(User user) {

		// 1️⃣ Show playlists
		System.out.println("--- My Playlists ---");
		List<Playlist> myPlaylists = playlistService.getMyPlaylists(user.getUserId());
		if (myPlaylists.isEmpty()) {
			System.out.println("❌ You have no playlists. Create one first.");
			return;
		}
		myPlaylists.forEach(p -> System.out.println(p.getPlaylistId() + " | " + p.getName()));

		System.out.print("Enter Playlist ID: ");
		int playlistId = sc.nextInt();
		sc.nextLine();

		// Validate ownership
		boolean owns = false;
		for (Playlist p : myPlaylists) {
			if (p.getPlaylistId() == playlistId) {
				owns = true;
				break;
			}
		}

		if (!owns) {
			System.out.println("❌ Invalid Playlist ID (not owned by you)");
			return;
		}

		// 2️⃣ Show all songs
		System.out.println("--- All Songs ---");
		songService.getAllSongs().forEach(s -> System.out.println(s.getSongId() + " | " + s.getTitle()));

		System.out.print("Enter Song ID: ");
		int songId = sc.nextInt();
		sc.nextLine();

		boolean success = playlistService.addSong(playlistId, songId);
		if (success) {
			System.out.println("🎵 Song added to playlist successfully");
		} else {
			System.out.println("❌ Failed to add song to playlist (Check IDs or existence)");
		}
	}

	private static void removeSongFromPlaylist(User user) {

		// 1️⃣ Show playlists
		System.out.println("--- My Playlists ---");
		List<Playlist> myPlaylists = playlistService.getMyPlaylists(user.getUserId());
		if (myPlaylists.isEmpty()) {
			System.out.println("❌ You have no playlists.");
			return;
		}
		myPlaylists.forEach(p -> System.out.println(p.getPlaylistId() + " | " + p.getName()));

		System.out.print("Enter Playlist ID: ");
		int playlistId = sc.nextInt();
		sc.nextLine();

		// Validate ownership
		boolean owns = false;
		for (Playlist p : myPlaylists) {
			if (p.getPlaylistId() == playlistId) {
				owns = true;
				break;
			}
		}

		if (!owns) {
			System.out.println("❌ Invalid Playlist ID (not owned by you)");
			return;
		}

		// 2️⃣ Show songs in playlist
		List<Song> songs = playlistService.getSongsInPlaylist(playlistId);
		if (songs.isEmpty()) {
			System.out.println("--- No songs in this playlist ---");
			return;
		}

		System.out.println("--- Songs in Playlist ---");
		songs.forEach(s -> System.out.println(s.getSongId() + " | " + s.getTitle()));

		System.out.print("Enter Song ID to remove: ");
		int songId = sc.nextInt();
		sc.nextLine();

		boolean removed = playlistService.removeSong(playlistId, songId);
		if (removed) {
			System.out.println("🗑 Song removed from playlist successfully");
		} else {
			System.out.println("❌ Failed to remove song (Song not in playlist)");
		}
	}

	private static void viewFavoriteSongs(User user) {
		List<Song> favorites = favoriteDAO.getFavoriteSongs(user.getUserId());
		if (favorites.isEmpty()) {
			System.out.println("❌ No favorite songs found. Start adding some!");
		} else {
			favorites.forEach(s -> System.out.println(
					s.getSongId() + " | " + s.getTitle() + " | " + s.getGenre() + " | Plays: " + s.getPlayCount()));
		}
	}

	private static void viewListeningHistory(User user) {
		List<Song> history = historyDAO.getListeningHistory(user.getUserId());
		if (history.isEmpty()) {
			System.out.println("❌ Your listening history is empty.");
		} else {
			history.forEach(s -> System.out.println(s.getSongId() + " | " + s.getTitle() + " | " + s.getGenre()));
		}
	}

	private static void searchSongs() {
		System.out.print("Enter keyword to search: ");
		String keyword = sc.nextLine();

		List<Song> results = songService.searchSongs(keyword);
		if (results.isEmpty()) {
			System.out.println("No matching songs found.");
			return;
		}
		results.forEach(s -> System.out.println(s.getSongId() + " | " + s.getTitle() + " | " + s.getGenre()));
	}

	private static void browseSongsByGenre() {
		System.out.println("\n--- Available Genres ---");
		List<String> genres = songService.getAllGenres();
		if (genres.isEmpty()) {
			System.out.println("No genres available");
			return;
		}
		genres.forEach(System.out::println);

		System.out.print("\nEnter genre to browse songs: ");
		String genre = sc.nextLine();

		songService.getSongsByGenre(genre)
				.forEach(s -> System.out.println(s.getSongId() + " | " + s.getTitle() + " | " + s.getGenre()));
	}

	private static void updatePlaylist(User user) {
		System.out.println("\n--- Your Playlists ---");
		List<Playlist> myPlaylists = playlistService.getMyPlaylists(user.getUserId());
		if (myPlaylists.isEmpty()) {
			System.out.println("❌ You have no playlists to update.");
			return;
		}
		myPlaylists.forEach(p -> System.out.println("ID: " + p.getPlaylistId() + " | Name: " + p.getName()));

		System.out.print("\nEnter Playlist ID to update: ");
		int pid = sc.nextInt();
		sc.nextLine();

		// Validate ownership
		boolean owns = false;
		for (Playlist p : myPlaylists) {
			if (p.getPlaylistId() == pid) {
				owns = true;
				break;
			}
		}

		if (!owns) {
			System.out.println("❌ Invalid Playlist ID (not owned by you)");
			return;
		}

		System.out.print("New Playlist Name: ");
		String name = sc.nextLine();
		System.out.print("New Description: ");
		String desc = sc.nextLine();
		System.out.print("Public? (Y/N): ");
		String pub = sc.nextLine();

		Playlist playlist = new Playlist();
		playlist.setPlaylistId(pid);
		playlist.setName(name);
		playlist.setDescription(desc);
		playlist.setIsPublic(pub);
		playlist.setUserId(user.getUserId());

		playlistService.updatePlaylist(playlist);

		System.out.println("✅ Playlist updated");
	}

	private static void deletePlaylist(User user) {
		System.out.println("\n--- Your Playlists ---");
		viewMyPlaylists(user);

		System.out.print("\nEnter Playlist ID to delete: ");
		int pid = sc.nextInt();
		sc.nextLine();

		boolean success = playlistService.deletePlaylist(pid, user.getUserId());

		if (success) {
			System.out.println("🗑 Playlist deleted successfully");
		} else {
			System.out.println("❌ Playlist not found or access denied");
		}
	}

	private static void viewPublicPlaylists(User user) {
		List<Playlist> playlists = playlistService.getPublicPlaylists(user.getUserId());
		if (playlists.isEmpty()) {
			System.out.println("❌ No public playlists found.");
		} else {
			playlists.forEach(p -> System.out.println(
					p.getPlaylistId() + " | " + p.getName() + " | By: " + p.getOwnerName() + " | "
							+ p.getDescription()));
		}
	}

	public static void viewRecentPlays(User user) {
		System.out.println("--- Recently Played (Last 5) ---");
		List<Song> recent = historyDAO.getRecentPlays(user.getUserId(), 5);
		if (recent.isEmpty()) {
			System.out.println("❌ No recently played songs.");
		} else {
			recent.forEach(s -> System.out.println(s.getSongId() + " | " + s.getTitle() + " | " + s.getGenre()));
		}
	}

	// -------------------Artist Dash board----------------------
	public static void artistDashboard(User user) {
		int option = 0;
		while (option != 21) {
			System.out.println("\n--- ARTIST DASHBOARD ---");
			System.out.println("----- Profile Management -----");
			System.out.println("1. View Profile");
			System.out.println("2. Update Profile");
			System.out.println("----- Album Management -----");
			System.out.println("3. Create Album");
			System.out.println("4. View My Albums");
			System.out.println("5. Update Album");
			System.out.println("6. Delete Album");
			System.out.println("----- Song Management -----");
			System.out.println("7. Upload Song");
			System.out.println("8. View My Songs");
			System.out.println("9. Update Song");
			System.out.println("10. Delete Song");
			System.out.println("----- Podcast Management -----");
			System.out.println("11. Create Podcast");
			System.out.println("12. View My Podcasts");
			System.out.println("13. Update Podcast");
			System.out.println("14. Delete Podcast");
			System.out.println("----- Episode Management -----");
			System.out.println("15. Add Podcast Episode");
			System.out.println("16. View My Podcast Episodes");
			System.out.println("17. Update Podcast Episode");
			System.out.println("18. Delete Podcast Episode");
			System.out.println("----- Analytics -----");
			System.out.println("19. View Content Statistics (Songs & Podcasts)");
			System.out.println("20. View Users Who Favorited My Songs");
			System.out.println("21. Logout");

			System.out.print("\nChoose Option: ");
			option = sc.nextInt();
			sc.nextLine();

			switch (option) {
				case 1:
					viewArtistProfile(user);
					break;
				case 2:
					createOrUpdateArtistProfile(user);
					break;
				case 3:
					createAlbum(user);
					break;
				case 4:
					viewMyAlbums(user);
					break;
				case 5:
					updateAlbum(user);
					break;
				case 6:
					deleteAlbum(user);
					break;
				case 7:
					uploadSongWithAlbum(user);
					break;
				case 8:
					viewMySongs(user);
					break;
				case 9:
					updateSong(user);
					break;
				case 10:
					deleteSong(user);
					break;
				case 11:
					handleCreatePodcast(user);
					break;
				case 12:
					viewMyPodcasts(user);
					break;
				case 13:
					handleUpdatePodcast(user);
					break;
				case 14:
					handleDeletePodcast(user);
					break;
				case 15:
					handleAddPodcastEpisode(user);
					break;
				case 16:
					viewMyPodcastEpisodes(user);
					break;
				case 17:
					handleUpdatePodcastEpisode(user);
					break;
				case 18:
					handleDeletePodcastEpisode(user);
					break;
				case 19:
					viewSongStatistics(user);
					break;
				case 20:
					viewUsersWhoFavoritedMySongs(user);
					break;
				case 21:
					System.out.println("Logging out from Artist Dashboard...");
					return;
				default:
					System.out.println("Invalid option");
			}
		}
	}

	private static void deleteAlbum(User user) {

		Artist artist = artistService.getArtistByUserId(user.getUserId());
		if (artist == null) {
			System.out.println("❌ Artist profile not found");
			return;
		}

		List<Album> albums = albumService.getMyAlbums(artist.getArtistId());
		if (albums.isEmpty()) {
			System.out.println("No albums are there");
			return;
		}

		albums.forEach(a -> System.out.println(a.getAlbumId() + " | " + a.getTitle()));

		System.out.print("Enter Album ID to delete: ");
		int albumId = sc.nextInt();
		sc.nextLine();

		// 🔥 FULL CASCADE DELETE (CORRECT ORDER)
		albumService.deleteFavoritesByAlbum(albumId);
		albumService.deletePlaylistSongsByAlbum(albumId);
		albumService.deleteListeningHistoryByAlbum(albumId);
		albumService.deleteSongsByAlbum(albumId);
		albumService.deleteAlbum(albumId, artist.getArtistId());

		System.out.println("🗑 album and all songs of this album were deleted");
	}

	private static void updateAlbum(User user) {

		Artist artist = artistService.getArtistByUserId(user.getUserId());
		if (artist == null) {
			System.out.println("❌ Artist profile not found");
			return;
		}

		// show albums
		List<Album> albums = albumService.getMyAlbums(artist.getArtistId());
		if (albums.isEmpty()) {
			System.out.println("No albums are there");
			return;
		}

		albums.forEach(a -> System.out.println(a.getAlbumId() + " | " + a.getTitle()));

		System.out.print("Enter Album ID to update: ");
		int albumId = sc.nextInt();
		sc.nextLine();

		Album album = new Album();
		album.setAlbumId(albumId);
		album.setArtistId(artist.getArtistId());

		System.out.print("New Title: ");
		album.setTitle(sc.nextLine());
		System.out.print("New Release Date (yyyy-mm-dd): ");
		String releaseDateStr = sc.nextLine();
		album.setReleaseDate(java.sql.Date.valueOf(releaseDateStr));

		albumService.updateAlbum(album);
		System.out.println("✅ Album updated");
	}

	private static void viewArtistProfile(User user) {

		ArtistService artistService = new ArtistServiceImpl();

		Artist artist = artistService.getArtistByUserId(user.getUserId());

		if (artist == null) {
			System.out.println("❌ Artist profile not found");
			System.out.println("➡ Please create/update profile first");
			return;
		}

		System.out.println("\n--- ARTIST PROFILE ---");
		System.out.println("Bio          : " + artist.getBio());
		System.out.println("Genre        : " + artist.getGenre());
		System.out.println("Social Links : " + artist.getSocialLinks());
	}

	public static void viewSongStatistics(User user) {
		Artist artist = artistService.getArtistByUserId(user.getUserId());

		if (artist == null) {
			System.out.println("❌ Artist profile not found");
			return;
		}

		System.out.println("\n--- 🎵 SONG STATISTICS ---");
		List<String> songStats = artistService.getSongStatistics(artist.getArtistId());
		if (songStats.isEmpty()) {
			System.out.println("No songs uploaded yet");
		} else {
			songStats.forEach(System.out::println);
		}

		System.out.println("\n--- 🎙 PODCAST STATISTICS ---");
		List<Podcast> myPodcasts = podcastService.getPodcastsByArtist(artist.getArtistId());
		if (myPodcasts.isEmpty()) {
			System.out.println("No podcasts created yet");
		} else {
			boolean hasEpisodes = false;
			for (Podcast p : myPodcasts) {
				List<PodcastEpisode> episodes = podcastService.getEpisodesByPodcast(p.getPodcastId());
				if (!episodes.isEmpty()) {
					hasEpisodes = true;
					System.out.println("Podcast: " + p.getTitle());
					episodes.forEach(e -> System.out.println("   - " + e.getTitle() + " | Plays: " + e.getPlayCount()));
				}
			}
			if (!hasEpisodes) {
				System.out.println("No episodes added yet");
			}
		}
	}

	public static void viewUsersWhoFavoritedMySongs(User user) {

		Artist artist = artistService.getArtistByUserId(user.getUserId());

		if (artist == null) {
			System.out.println("❌ Artist profile not found");
			return;
		}

		System.out.println("--- Users Who Favorited My Songs ---");

		List<String> list = artistSongService.getUsersWhoFavoritedMySongs(artist.getArtistId());

		if (list.isEmpty()) {
			System.out.println("No favorites yet");
			return;
		}

		list.forEach(System.out::println);
	}

	public static void deleteSong(User user) {

		Artist artist = artistService.getArtistByUserId(user.getUserId());

		if (artist == null) {
			System.out.println("❌ Artist profile not found");
			return;
		}

		System.out.println("\n--- My Songs ---");
		artistSongService.getSongsByArtist(artist.getArtistId())
				.forEach(s -> System.out.println(s.getSongId() + " | " + s.getTitle()));

		System.out.print("Enter Song ID to delete: ");
		int songId = sc.nextInt();
		sc.nextLine();

		// 🔒 Ownership check
		if (!artistSongService.isSongOwnedByArtist(songId, artist.getArtistId())) {
			System.out.println("❌ Delete failed (not your song)");
			return;
		}

		boolean deleted = artistSongService.deleteSong(songId, artist.getArtistId());

		if (deleted) {
			System.out.println("🗑 Song deleted successfully");
		} else {
			System.out.println("❌ Song deletion failed");
		}
	}

	public static void updateSong(User user) {

		Artist artist = artistService.getArtistByUserId(user.getUserId());
		if (artist == null) {
			System.out.println("❌ Artist profile not found");
			return;
		}

		// 1️⃣ Show artist songs
		System.out.println("\n--- My Songs ---");
		List<Song> songs = artistSongService.getSongsByArtist(artist.getArtistId());

		if (songs.isEmpty()) {
			System.out.println("No songs uploaded yet");
			return;
		}

		songs.forEach(s -> System.out.println(s.getSongId() + " | " + s.getTitle()));

		// 2️⃣ Choose song
		System.out.print("Enter Song ID to update: ");
		int songId = sc.nextInt();
		sc.nextLine();

		// 🔒 Ownership check (🔥 THIS WAS MISSING)
		if (!artistSongService.isSongOwnedByArtist(songId, artist.getArtistId())) {
			System.out.println("❌ Update failed (not your song)");
			return; // ⛔ STOP execution
		}

		// 3️⃣ Take new details
		Song song = new Song();
		song.setSongId(songId);
		song.setArtistId(artist.getArtistId());

		System.out.print("New Title: ");
		song.setTitle(sc.nextLine());
		System.out.print("New Genre: ");
		song.setGenre(sc.nextLine());
		System.out.print("New Duration: ");
		song.setDuration(sc.nextInt());
		sc.nextLine();

		// 4️⃣ Show albums
		System.out.println("\n--- My Albums ---");
		albumService.getMyAlbums(artist.getArtistId())
				.forEach(a -> System.out.println(a.getAlbumId() + " | " + a.getTitle()));

		System.out.print("Choose Album ID: ");
		song.setAlbumId(sc.nextInt());
		sc.nextLine();

		// 5️⃣ Update song
		artistSongService.updateSong(song);

		System.out.println("✅ Song updated successfully");
	}

	public static void viewMySongs(User user) {

		// 1️⃣ Convert user_id → artist_id
		Artist artist = artistService.getArtistByUserId(user.getUserId());

		if (artist == null) {
			System.out.println("❌ Artist profile not found");
			return;
		}

		System.out.println("\n--- MY SONGS ---");

		List<Song> songs = artistSongService.getSongsByArtist(artist.getArtistId());

		if (songs.isEmpty()) {
			System.out.println("No songs uploaded yet");
			return;
		}

		for (Song s : songs) {
			System.out.println(s.getSongId() + " | " + s.getTitle() + " | " + s.getGenre() + " | Album: "
					+ s.getAlbumId() + " | Plays: " + s.getPlayCount());
		}
	}

	public static void uploadSongWithAlbum(User user) {

		Song song = new Song();

		// get artist
		Artist artist = artistService.getArtistByUserId(user.getUserId());

		if (artist == null) {
			System.out.println("❌ Artist profile not found");
			return;
		}

		// show albums
		System.out.println("--- My Albums ---");
		albumService.getMyAlbums(artist.getArtistId())
				.forEach(a -> System.out.println(a.getAlbumId() + " | " + a.getTitle()));

		System.out.print("Choose Album ID: ");
		int albumId = sc.nextInt();
		sc.nextLine();

		System.out.print("Song Title: ");
		song.setTitle(sc.nextLine());
		System.out.print("Genre: ");
		song.setGenre(sc.nextLine());
		System.out.print("Duration (seconds): ");
		song.setDuration(sc.nextInt());
		sc.nextLine();

		song.setArtistId(artist.getArtistId());
		song.setAlbumId(albumId);

		songService.uploadSong(song);

		System.out.println("🎶 Song uploaded and linked to album successfully");
	}

	private static void viewMyAlbums(User user) {

		// 🔥 Convert user_id → artist_id
		Artist artist = artistService.getArtistByUserId(user.getUserId());

		if (artist == null) {
			System.out.println("❌ Artist profile not found");
			return;
		}

		List<Album> albums = albumService.getMyAlbums(artist.getArtistId());
		if (albums.isEmpty()) {
			System.out.println("No albums are there");
		} else {
			albums.forEach(a -> System.out.println(a.getAlbumId() + " | " + a.getTitle() + " | " + a.getReleaseDate()));
		}
	}

	private static void createAlbum(User user) {

		// 🔥 Get artist record using user_id
		Artist artist = artistService.getArtistByUserId(user.getUserId());

		if (artist == null) {
			System.out.println("❌ Artist profile not found. Please create profile first.");
			return;
		}

		Album album = new Album();

		System.out.print("Album Title: ");
		album.setTitle(sc.nextLine());
		System.out.print("Release Date (yyyy-mm-dd): ");
		String relDate = sc.nextLine();
		album.setReleaseDate(java.sql.Date.valueOf(relDate));

		// ✅ CORRECT artist_id
		album.setArtistId(artist.getArtistId());

		albumService.createAlbum(album);

		System.out.println("✅ Album created successfully");
	}

	private static void createOrUpdateArtistProfile(User user) {

		ArtistService artistService = new ArtistServiceImpl();
		Artist artist = new Artist();

		artist.setUserId(user.getUserId());

		System.out.print("Enter Bio: ");
		artist.setBio(sc.nextLine());
		System.out.print("Enter Genre: ");
		artist.setGenre(sc.nextLine());
		System.out.print("Enter Social Links: ");
		artist.setSocialLinks(sc.nextLine());

		artistService.saveOrUpdateArtist(artist);

		System.out.println("✅ Artist profile saved successfully");
	}

	private static void viewAllSongs() {
		List<Song> songs = songService.getAllSongs();
		if (songs.isEmpty()) {
			System.out.println("No songs available in the database.");
			return;
		}
		songs.forEach(s -> System.out.println(
				s.getSongId() + " | " + s.getTitle() + " | " + s.getGenre() + " | Plays: " + s.getPlayCount()));
	}

	private static void viewAllPodcasts() {
		System.out.println("\n--- ALL PODCASTS ---");
		List<Podcast> podcasts = podcastService.getAllPodcasts();
		if (podcasts.isEmpty()) {
			System.out.println("No podcasts found");
			return;
		}
		podcasts.forEach(p -> System.out.println(p.getPodcastId() + " | " + p.getTitle() + " | Host: " + p.getHostName()
				+ " | Category: " + p.getCategory()));
	}

	private static void handleSearchPodcasts() {
		System.out.print("Enter keyword for podcast search: ");
		String keyword = sc.nextLine();
		List<Podcast> results = podcastService.searchPodcasts(keyword);
		if (results.isEmpty()) {
			System.out.println("No matching podcasts found");
			return;
		}
		results.forEach(
				p -> System.out.println(p.getPodcastId() + " | " + p.getTitle() + " | Category: " + p.getCategory()));
	}

	private static void handleGlobalSearch() {
		System.out.print("\nGlobal search (Songs & PodCasts): ");
		String keyword = sc.nextLine();

		System.out.println("\n--- 🎵 Matching Songs ---");
		List<Song> songs = songService.searchSongs(keyword);
		if (songs.isEmpty()) {
			System.out.println("No matching songs found");
		} else {
			songs.forEach(
					s -> System.out.println(s.getSongId() + " | " + s.getTitle() + " | Genre: " + s.getGenre()));
		}

		System.out.println("\n--- 🎙 Matching PodCasts ---");
		List<Podcast> podcasts = podcastService.searchPodcasts(keyword);
		if (podcasts.isEmpty()) {
			System.out.println("No matching podcasts found");
		} else {
			podcasts.forEach(
					p -> System.out
							.println(p.getPodcastId() + " | " + p.getTitle() + " | Category: " + p.getCategory()));
		}
	}

	private static void handleCreatePodcast(User user) {
		Artist artist = artistService.getArtistByUserId(user.getUserId());
		if (artist == null) {
			System.out.println("❌ Artist profile not found. Register as artist first.");
			return;
		}

		System.out.println("\n--- Create New Podcast ---");
		Podcast p = new Podcast();
		p.setArtistId(artist.getArtistId());
		p.setHostName(artist.getArtistName());

		System.out.print("Enter Podcast Title: ");
		p.setTitle(sc.nextLine());
		System.out.print("Enter Category: ");
		p.setCategory(sc.nextLine());
		System.out.print("Enter Description: ");
		p.setDescription(sc.nextLine());

		podcastService.createPodcast(p);
		System.out.println("✅ Podcast created successfully!");
	}

	private static void viewMyPodcasts(User user) {
		Artist artist = artistService.getArtistByUserId(user.getUserId());
		if (artist == null)
			return;

		System.out.println("\n--- My Podcasts ---");
		List<Podcast> list = podcastService.getPodcastsByArtist(artist.getArtistId());
		if (list.isEmpty()) {
			System.out.println("You haven't created any podcasts yet.");
			return;
		}
		list.forEach(
				p -> System.out.println(p.getPodcastId() + " | " + p.getTitle() + " | Category: " + p.getCategory()));
	}

	private static void handleAddPodcastEpisode(User user) {
		Artist artist = artistService.getArtistByUserId(user.getUserId());
		if (artist == null)
			return;

		List<Podcast> myPodcasts = podcastService.getPodcastsByArtist(artist.getArtistId());
		if (myPodcasts.isEmpty()) {
			System.out.println("❌ You need to create a podcast first.");
			return;
		}

		System.out.println("\n--- Add Episode to Podcast ---");
		myPodcasts.forEach(p -> System.out.println(p.getPodcastId() + " | " + p.getTitle()));

		System.out.print("Enter Podcast ID: ");
		int podcastId = sc.nextInt();
		sc.nextLine();

		// Validate ownership
		boolean owns = myPodcasts.stream().anyMatch(p -> p.getPodcastId() == podcastId);
		if (!owns) {
			System.out.println("❌ Invalid Podcast ID or not owned by you.");
			return;
		}

		PodcastEpisode ep = new PodcastEpisode();
		ep.setPodcastId(podcastId);
		System.out.print("Episode Title: ");
		ep.setTitle(sc.nextLine());
		System.out.print("Duration (seconds): ");
		ep.setDuration(sc.nextInt());
		sc.nextLine();
		System.out.print("Release Date (yyyy-mm-dd): ");
		ep.setReleaseDate(java.sql.Date.valueOf(sc.nextLine()));

		podcastService.addEpisode(ep);
		System.out.println("✅ Episode added successfully!");
	}

	private static void viewMyPodcastEpisodes(User user) {
		Artist artist = artistService.getArtistByUserId(user.getUserId());
		if (artist == null)
			return;

		List<Podcast> myPodcasts = podcastService.getPodcastsByArtist(artist.getArtistId());
		if (myPodcasts.isEmpty()) {
			System.out.println("❌ You have no podcasts.");
			return;
		}

		System.out.println("\n--- My Podcasts & Their Episodes ---");
		for (Podcast p : myPodcasts) {
			System.out.println("\n🎙 Podcast: " + p.getTitle() + " [" + p.getCategory() + "]");
			List<PodcastEpisode> episodes = podcastService.getEpisodesByPodcast(p.getPodcastId());
			if (episodes.isEmpty()) {
				System.out.println("   (No episodes added yet)");
			} else {
				episodes.forEach(e -> System.out.println("   - " + e.getEpisodeId() + " | " + e.getTitle()
						+ " | Duration: " + e.getDuration() + "s | Plays: " + e.getPlayCount()));
			}
		}
	}

	private static void handleUpdatePodcast(User user) {
		Artist artist = artistService.getArtistByUserId(user.getUserId());
		if (artist == null)
			return;

		List<Podcast> myPodcasts = podcastService.getPodcastsByArtist(artist.getArtistId());
		if (myPodcasts.isEmpty()) {
			System.out.println("❌ You have no podcasts to update.");
			return;
		}

		System.out.println("\n--- Update Podcast ---");
		myPodcasts.forEach(p -> System.out.println(p.getPodcastId() + " | " + p.getTitle()));

		System.out.print("Enter Podcast ID to update: ");
		int pid = sc.nextInt();
		sc.nextLine();

		Podcast selected = null;
		for (Podcast p : myPodcasts) {
			if (p.getPodcastId() == pid) {
				selected = p;
				break;
			}
		}

		if (selected == null) {
			System.out.println("❌ Invalid Podcast ID or not owned by you.");
			return;
		}

		System.out.print("New Title [" + selected.getTitle() + "]: ");
		String title = sc.nextLine();
		if (!title.isEmpty())
			selected.setTitle(title);

		System.out.print("New Category [" + selected.getCategory() + "]: ");
		String cat = sc.nextLine();
		if (!cat.isEmpty())
			selected.setCategory(cat);

		System.out.print("New Description [" + selected.getDescription() + "]: ");
		String desc = sc.nextLine();
		if (!desc.isEmpty())
			selected.setDescription(desc);

		boolean updated = podcastService.updatePodcast(selected);
		if (updated) {
			System.out.println("✅ Podcast updated successfully");
		} else {
			System.out.println("❌ Failed to update podcast");
		}
	}

	private static void handleDeletePodcast(User user) {
		Artist artist = artistService.getArtistByUserId(user.getUserId());
		if (artist == null)
			return;

		List<Podcast> myPodcasts = podcastService.getPodcastsByArtist(artist.getArtistId());
		if (myPodcasts.isEmpty()) {
			System.out.println("❌ You have no podcasts to delete.");
			return;
		}

		System.out.println("\n--- Delete Podcast ---");
		myPodcasts.forEach(p -> System.out.println(p.getPodcastId() + " | " + p.getTitle()));

		System.out.print("Enter Podcast ID to delete: ");
		int pid = sc.nextInt();
		sc.nextLine();

		boolean owns = myPodcasts.stream().anyMatch(p -> p.getPodcastId() == pid);
		if (!owns) {
			System.out.println("❌ Invalid Podcast ID or not owned by you.");
			return;
		}

		System.out.print("Are you sure you want to delete this podcast and all its episodes? (Y/N): ");
		if (sc.nextLine().equalsIgnoreCase("Y")) {
			boolean deleted = podcastService.deletePodcast(pid, artist.getArtistId());
			if (deleted) {
				System.out.println("🗑 Podcast and episodes deleted successfully");
			} else {
				System.out.println("❌ Failed to delete podcast");
			}
		}
	}

	private static void handleUpdatePodcastEpisode(User user) {
		Artist artist = artistService.getArtistByUserId(user.getUserId());
		if (artist == null)
			return;

		List<Podcast> myPodcasts = podcastService.getPodcastsByArtist(artist.getArtistId());
		if (myPodcasts.isEmpty()) {
			System.out.println("❌ You have no podcasts.");
			return;
		}

		System.out.println("\n--- My Podcasts ---");
		myPodcasts.forEach(p -> System.out.println(p.getPodcastId() + " | " + p.getTitle()));

		System.out.print("Select Podcast ID to see episodes: ");
		int pid = sc.nextInt();
		sc.nextLine();

		boolean owns = myPodcasts.stream().anyMatch(p -> p.getPodcastId() == pid);
		if (!owns) {
			System.out.println("❌ Invalid Podcast ID.");
			return;
		}

		List<PodcastEpisode> episodes = podcastService.getEpisodesByPodcast(pid);
		if (episodes.isEmpty()) {
			System.out.println("No episodes in this podcast.");
			return;
		}

		System.out.println("\n--- Episodes ---");
		episodes.forEach(e -> System.out.println(e.getEpisodeId() + " | " + e.getTitle()));

		System.out.print("Enter Episode ID to update: ");
		int eid = sc.nextInt();
		sc.nextLine();

		PodcastEpisode selected = episodes.stream().filter(e -> e.getEpisodeId() == eid).findFirst().orElse(null);
		if (selected == null) {
			System.out.println("❌ Invalid Episode ID.");
			return;
		}

		System.out.print("New Title [" + selected.getTitle() + "]: ");
		String title = sc.nextLine();
		if (!title.isEmpty())
			selected.setTitle(title);

		System.out.print("New Duration (sec) [" + selected.getDuration() + "]: ");
		String durStr = sc.nextLine();
		if (!durStr.isEmpty())
			selected.setDuration(Integer.parseInt(durStr));

		System.out.print("New Release Date (yyyy-mm-dd) [" + selected.getReleaseDate() + "]: ");
		String dateStr = sc.nextLine();
		if (!dateStr.isEmpty())
			selected.setReleaseDate(java.sql.Date.valueOf(dateStr));

		boolean updated = podcastService.updateEpisode(selected);
		if (updated) {
			System.out.println("✅ Episode updated successfully");
		} else {
			System.out.println("❌ Failed to update episode");
		}
	}

	private static void handleDeletePodcastEpisode(User user) {
		Artist artist = artistService.getArtistByUserId(user.getUserId());
		if (artist == null)
			return;

		List<Podcast> myPodcasts = podcastService.getPodcastsByArtist(artist.getArtistId());
		if (myPodcasts.isEmpty()) {
			System.out.println("❌ You have no podcasts.");
			return;
		}

		System.out.println("\n--- My Podcasts ---");
		myPodcasts.forEach(p -> System.out.println(p.getPodcastId() + " | " + p.getTitle()));

		System.out.print("Select Podcast ID to see episodes: ");
		int pid = sc.nextInt();
		sc.nextLine();

		boolean owns = myPodcasts.stream().anyMatch(p -> p.getPodcastId() == pid);
		if (!owns) {
			System.out.println("❌ Invalid Podcast ID.");
			return;
		}

		List<PodcastEpisode> episodes = podcastService.getEpisodesByPodcast(pid);
		if (episodes.isEmpty()) {
			System.out.println("No episodes found.");
			return;
		}

		System.out.println("\n--- Episodes ---");
		episodes.forEach(e -> System.out.println(e.getEpisodeId() + " | " + e.getTitle()));

		System.out.print("Enter Episode ID to delete: ");
		int eid = sc.nextInt();
		sc.nextLine();

		boolean belongs = episodes.stream().anyMatch(e -> e.getEpisodeId() == eid);
		if (!belongs) {
			System.out.println("❌ Episode does not belong to your podcast.");
			return;
		}

		System.out.print("Are you sure? (Y/N): ");
		if (sc.nextLine().equalsIgnoreCase("Y")) {
			boolean deleted = podcastService.deleteEpisode(eid);
			if (deleted) {
				System.out.println("🗑 Episode deleted successfully");
			} else {
				System.out.println("❌ Failed to delete episode");
			}
		}
	}
}

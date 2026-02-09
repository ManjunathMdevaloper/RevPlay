package com.revplay.service;

import java.util.List;

import com.revplay.model.Song;
import com.revplay.model.User;

public interface UserService {

	User login(String email, String password);

	List<Song> searchSongs(String keyword);

	List<Song> getSongsByGenre(String genre);

	boolean registerUser(User user);

	String getSecurityQuestion(String email);

	boolean validateSecurityAnswer(String email, String answer);

	String getPasswordHint(String email);

	String getPassword(String email);

	boolean registerArtist(User user);

	int getUserIdByEmail(String email);

	boolean updatePassword(String email, String newPassword);
}

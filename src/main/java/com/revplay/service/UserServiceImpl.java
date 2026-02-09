package com.revplay.service;

import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.core.Logger;

import com.revplay.dao.UserDAO;
import com.revplay.dao.UserDAOImpl;
import com.revplay.model.Song;
import com.revplay.model.User;

public class UserServiceImpl implements UserService {

	private UserDAO userDAO = new UserDAOImpl();
	private SongService songService = new SongServiceImpl();
	private static final Logger logger = (Logger) LogManager.getLogger(UserServiceImpl.class);

	@Override
	public User login(String email, String password) {

		logger.info("Login attempt for email: " + email);

		// 1️⃣ Input validation
		if (email == null || password == null) {
			logger.warn("Login failed due to null email/password");
			return null;
		}

		// 2️⃣ Call DAO
		User user = userDAO.login(email, password);

		// 3️⃣ Log result
		if (user != null) {
			logger.info("Login success: " + email + ", role=" + user.getRole());
		} else {
			logger.warn("Login failed for email: " + email);
		}

		// 4️⃣ Return result
		return user;
	}

	@Override
	public List<Song> searchSongs(String keyword) {
		return songService.searchSongs(keyword);
	}

	@Override
	public List<Song> getSongsByGenre(String genre) {
		return songService.getSongsByGenre(genre);
	}

	@Override
	public boolean registerUser(User user) {
		// check if email already exists
		if (userDAO.isEmailExists(user.getEmail())) {
			logger.warn("Registration attempt with existing email: " + user.getEmail());
			return false;
		}
		boolean success = userDAO.registerUser(user);
		if (success) {
			logger.info("User registered successfully: " + user.getEmail());
		}
		return success;
	}

	@Override
	public String getSecurityQuestion(String email) {
		return userDAO.getSecurityQuestion(email);
	}

	@Override
	public boolean validateSecurityAnswer(String email, String answer) {
		return userDAO.validateSecurityAnswer(email, answer);
	}

	@Override
	public String getPasswordHint(String email) {
		return userDAO.getPasswordHint(email);
	}

	@Override
	public String getPassword(String email) {
		return userDAO.getPassword(email);
	}

	@Override
	public boolean registerArtist(User user) {
		if (userDAO.isEmailExists(user.getEmail())) {
			logger.warn("Artist registration failed: email already exists - " + user.getEmail());
			return false;
		}
		boolean success = userDAO.registerArtist(user);
		if (success) {
			logger.info("Artist registered successfully: " + user.getEmail());
		}
		return success;
	}

	@Override
	public int getUserIdByEmail(String email) {
		return userDAO.getUserIdByEmail(email);
	}

	@Override
	public boolean updatePassword(String email, String newPassword) {
		return userDAO.updatePassword(email, newPassword);
	}

}

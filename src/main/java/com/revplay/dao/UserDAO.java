package com.revplay.dao;

import com.revplay.model.User;

public interface UserDAO {

	User login(String email, String password);

	User getUserById(int userId);

	boolean registerUser(User user);

	String getSecurityQuestion(String email);

	boolean validateSecurityAnswer(String email, String answer);

	String getPasswordHint(String email);

	String getPassword(String email);

	boolean registerArtist(User user);

	int getUserIdByEmail(String email);

	boolean isEmailExists(String email);

	boolean updatePassword(String email, String newPassword);
}

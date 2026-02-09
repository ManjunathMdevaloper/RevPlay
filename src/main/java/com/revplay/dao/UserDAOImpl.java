package com.revplay.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

import com.revplay.model.User;
import com.revplay.util.DBConnection;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class UserDAOImpl implements UserDAO {

	private static final String LOGIN_SQL = "SELECT * FROM users WHERE email = ? AND password = ?";
	private static final Logger logger = LogManager.getLogger(UserDAOImpl.class);

	@Override
	public boolean isEmailExists(String email) {

		String sql = "SELECT COUNT(*) FROM users WHERE email = ?";

		try (
				Connection con = DBConnection.getConnection();
				PreparedStatement ps = con.prepareStatement(sql)) {
			ps.setString(1, email);
			ResultSet rs = ps.executeQuery();

			if (rs.next()) {
				return rs.getInt(1) > 0;
			}
		} catch (Exception e) {
			logger.error("DB error while checking email existence: " + email, e);
		}

		return false;
	}

	@Override
	public User login(String email, String password) {

		User user = null;

		try (Connection con = DBConnection.getConnection(); PreparedStatement ps = con.prepareStatement(LOGIN_SQL);) {

			ps.setString(1, email);
			ps.setString(2, password);

			ResultSet rs = ps.executeQuery();

			if (rs.next()) {
				user = new User();
				user.setUserId(rs.getInt("user_id"));
				user.setName(rs.getString("name"));
				user.setEmail(rs.getString("email"));
				user.setPassword(rs.getString("password"));
				user.setRole(rs.getString("role"));
				user.setSecurityQuestion(rs.getString("security_question"));
				user.setSecurityAnswer(rs.getString("security_answer"));
				user.setPasswordHint(rs.getString("password_hint"));
				user.setCreatedAt(rs.getDate("created_at"));
			}

		} catch (Exception e) {
			logger.error("DB error during login for email: " + email, e);
		}

		return user;
	}

	@Override
	public User getUserById(int userId) {
		return null; // will implement later
	}

	@Override
	public boolean registerArtist(User user) {
		String sql = "INSERT INTO users (user_id, name, email, password, role, security_question, security_answer, password_hint, created_at) VALUES (users_seq.NEXTVAL, ?, ?, ?, 'ARTIST', ?, ?, ?, SYSDATE)";
		try (Connection con = DBConnection.getConnection(); PreparedStatement ps = con.prepareStatement(sql)) {
			ps.setString(1, user.getName());
			ps.setString(2, user.getEmail());
			ps.setString(3, user.getPassword());
			ps.setString(4, user.getSecurityQuestion());
			ps.setString(5, user.getSecurityAnswer());
			ps.setString(6, user.getPasswordHint());
			return ps.executeUpdate() > 0;
		} catch (Exception e) {
			logger.error("DB error in UserDAOImpl.registerArtist()", e);
		}
		return false;
	}

	@Override
	public int getUserIdByEmail(String email) {

		String sql = "SELECT user_id FROM users WHERE email = ?";
		try (
				Connection con = DBConnection.getConnection();
				PreparedStatement ps = con.prepareStatement(sql)) {
			ps.setString(1, email);
			ResultSet rs = ps.executeQuery();
			if (rs.next()) {
				return rs.getInt(1);
			}
		} catch (Exception e) {
			logger.error("DB error in UserDAOImpl.getUserIdByEmail()", e);
		}

		return 0;
	}

	@Override
	public boolean registerUser(User user) {
		String sql = "INSERT INTO users\r\n" + "    (user_id, name, email, password, role,\r\n"
				+ "     security_question, security_answer, password_hint, created_at)\r\n" + "    VALUES\r\n"
				+ "    (users_seq.NEXTVAL, ?, ?, ?, ?, ?, ?, ?, SYSDATE)";
		try (Connection con = DBConnection.getConnection(); PreparedStatement ps = con.prepareStatement(sql);) {
			ps.setString(1, user.getName());
			ps.setString(2, user.getEmail());
			ps.setString(3, user.getPassword());
			ps.setString(4, user.getRole());
			ps.setString(5, user.getSecurityQuestion());
			ps.setString(6, user.getSecurityAnswer());
			ps.setString(7, user.getPasswordHint());
			return ps.executeUpdate() > 0;
		} catch (Exception e) {
			logger.error("DB error in UserDAOImpl.registerUser()", e);
		}
		return false;
	}

	@Override
	public String getSecurityQuestion(String email) {

		String sql = "SELECT security_question FROM users WHERE email = ?";
		try (Connection con = DBConnection.getConnection(); PreparedStatement ps = con.prepareStatement(sql)) {
			ps.setString(1, email);
			ResultSet rs = ps.executeQuery();
			if (rs.next()) {
				return rs.getString(1);
			}
		} catch (Exception e) {
			logger.error("DB error in UserDAOImpl.getSecurityQuestion()", e);
		}

		return null;
	}

	@Override
	public boolean validateSecurityAnswer(String email, String answer) {

		String sql = "SELECT 1 FROM users WHERE email = ? AND security_answer = ?";
		try (Connection con = DBConnection.getConnection(); PreparedStatement ps = con.prepareStatement(sql)) {
			ps.setString(1, email);
			ps.setString(2, answer);
			ResultSet rs = ps.executeQuery();
			return rs.next();
		} catch (Exception e) {
			logger.error("DB error in UserDAOImpl.validateSecurityAnswer()", e);
		}

		return false;
	}

	@Override
	public String getPasswordHint(String email) {

		String sql = "SELECT password_hint FROM users WHERE email = ?";
		try (Connection con = DBConnection.getConnection(); PreparedStatement ps = con.prepareStatement(sql)) {
			ps.setString(1, email);
			ResultSet rs = ps.executeQuery();
			if (rs.next()) {
				return rs.getString(1);
			}
		} catch (Exception e) {
			logger.error("DB error in UserDAOImpl.getPasswordHint()", e);
		}

		return null;
	}

	@Override
	public String getPassword(String email) {

		String sql = "SELECT password FROM users WHERE email = ?";
		try (Connection con = DBConnection.getConnection(); PreparedStatement ps = con.prepareStatement(sql)) {
			ps.setString(1, email);
			ResultSet rs = ps.executeQuery();
			if (rs.next()) {
				return rs.getString(1);
			}
		} catch (Exception e) {
			logger.error("DB error in UserDAOImpl.getPassword()", e);
		}
		return null;
	}

	@Override
	public boolean updatePassword(String email, String newPassword) {
		String sql = "UPDATE users SET password = ? WHERE email = ?";
		try (Connection con = DBConnection.getConnection(); PreparedStatement ps = con.prepareStatement(sql)) {
			ps.setString(1, newPassword);
			ps.setString(2, email);
			return ps.executeUpdate() > 0;
		} catch (Exception e) {
			logger.error("DB error in UserDAOImpl.updatePassword()", e);
		}
		return false;
	}
}

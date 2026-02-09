package com.revplay.main;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

import com.revplay.util.DBConnection;

public class DBTest {

	public static void main(String[] args) {

		String sql = "SELECT user_id, name, role FROM users";

		try (Connection con = DBConnection.getConnection();
				PreparedStatement ps = con.prepareStatement(sql);
				ResultSet rs = ps.executeQuery();) {

			System.out.println("Connected to Oracle DB successfully!");
			System.out.println("---- USERS ----");

			while (rs.next()) {
				System.out.println(rs.getInt("user_id") + " | " + rs.getString("name") + " | " + rs.getString("role"));
			}

		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}

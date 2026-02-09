package com.revplay.util;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DBConnection {

	private static final String URL =
	        "jdbc:oracle:thin:@Manjunath:1522:XE";
    private static final String USERNAME = "SYSTEM";
    private static final String PASSWORD = "Manu@9965";

    public static Connection getConnection() throws SQLException {
        return DriverManager.getConnection(URL, USERNAME, PASSWORD);
    }
}

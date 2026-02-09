package com.revplay.test;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.MockedStatic;

import com.revplay.dao.UserDAOImpl;
import com.revplay.model.User;
import com.revplay.util.DBConnection;

class UserDAOTest {

    private MockedStatic<DBConnection> mockedDbConnection;
    private Connection mockCon;
    private PreparedStatement mockPs;
    private ResultSet mockRs;
    private UserDAOImpl dao;

    @BeforeEach
    void setUp() throws Exception {
        mockedDbConnection = mockStatic(DBConnection.class);
        mockCon = mock(Connection.class);
        mockPs = mock(PreparedStatement.class);
        mockRs = mock(ResultSet.class);
        dao = new UserDAOImpl();

        mockedDbConnection.when(DBConnection::getConnection).thenReturn(mockCon);
    }

    @AfterEach
    void tearDown() {
        mockedDbConnection.close();
    }

    @Test
    void testIsEmailExists_True() throws Exception {
        when(mockCon.prepareStatement(anyString())).thenReturn(mockPs);
        when(mockPs.executeQuery()).thenReturn(mockRs);
        when(mockRs.next()).thenReturn(true);
        when(mockRs.getInt(1)).thenReturn(1);

        assertTrue(dao.isEmailExists("test@test.com"));
    }

    @Test
    void testLogin_Success() throws Exception {
        when(mockCon.prepareStatement(anyString())).thenReturn(mockPs);
        when(mockPs.executeQuery()).thenReturn(mockRs);
        when(mockRs.next()).thenReturn(true);
        when(mockRs.getInt("user_id")).thenReturn(1);
        when(mockRs.getString("name")).thenReturn("Tester");
        when(mockRs.getString("role")).thenReturn("USER");

        User user = dao.login("test@test.com", "pass");
        assertNotNull(user);
        assertEquals("Tester", user.getName());
    }

    @Test
    void testRegisterUser_Success() throws Exception {
        when(mockCon.prepareStatement(anyString())).thenReturn(mockPs);
        when(mockPs.executeUpdate()).thenReturn(1);

        User user = new User();
        user.setName("User");
        assertTrue(dao.registerUser(user));
    }

    @Test
    void testRegisterArtist_Success() throws Exception {
        when(mockCon.prepareStatement(anyString())).thenReturn(mockPs);
        when(mockPs.executeUpdate()).thenReturn(1);

        User user = new User();
        user.setName("Artist");
        assertTrue(dao.registerArtist(user));
    }

    @Test
    void testGetUserIdByEmail() throws Exception {
        when(mockCon.prepareStatement(anyString())).thenReturn(mockPs);
        when(mockPs.executeQuery()).thenReturn(mockRs);
        when(mockRs.next()).thenReturn(true);
        when(mockRs.getInt(1)).thenReturn(123);

        assertEquals(123, dao.getUserIdByEmail("test@test.com"));
    }

    @Test
    void testGetSecurityQuestion() throws Exception {
        when(mockCon.prepareStatement(anyString())).thenReturn(mockPs);
        when(mockPs.executeQuery()).thenReturn(mockRs);
        when(mockRs.next()).thenReturn(true);
        when(mockRs.getString(1)).thenReturn("What color?");

        assertEquals("What color?", dao.getSecurityQuestion("test@test.com"));
    }

    @Test
    void testValidateSecurityAnswer() throws Exception {
        when(mockCon.prepareStatement(anyString())).thenReturn(mockPs);
        when(mockPs.executeQuery()).thenReturn(mockRs);
        when(mockRs.next()).thenReturn(true);

        assertTrue(dao.validateSecurityAnswer("test@test.com", "Blue"));
    }

    @Test
    void testGetPasswordHint() throws Exception {
        when(mockCon.prepareStatement(anyString())).thenReturn(mockPs);
        when(mockPs.executeQuery()).thenReturn(mockRs);
        when(mockRs.next()).thenReturn(true);
        when(mockRs.getString(1)).thenReturn("Hint");

        assertEquals("Hint", dao.getPasswordHint("test@test.com"));
    }

    @Test
    void testGetPassword() throws Exception {
        when(mockCon.prepareStatement(anyString())).thenReturn(mockPs);
        when(mockPs.executeQuery()).thenReturn(mockRs);
        when(mockRs.next()).thenReturn(true);
        when(mockRs.getString(1)).thenReturn("pass123");

        assertEquals("pass123", dao.getPassword("test@test.com"));
    }

    @Test
    void testUpdatePassword() throws Exception {
        when(mockCon.prepareStatement(anyString())).thenReturn(mockPs);
        when(mockPs.executeUpdate()).thenReturn(1);

        assertTrue(dao.updatePassword("test@test.com", "newPass"));
    }

    @Test
    void testLogin_Fail() throws Exception {
        when(mockCon.prepareStatement(anyString())).thenReturn(mockPs);
        when(mockPs.executeQuery()).thenReturn(mockRs);
        when(mockRs.next()).thenReturn(false);

        assertNull(dao.login("wrong@test.com", "pass"));
    }

    @Test
    void testGetSecurityQuestion_Null() throws Exception {
        when(mockCon.prepareStatement(anyString())).thenReturn(mockPs);
        when(mockPs.executeQuery()).thenReturn(mockRs);
        when(mockRs.next()).thenReturn(false);

        assertNull(dao.getSecurityQuestion("wrong@test.com"));
    }

    @Test
    void testIsEmailExists_False() throws Exception {
        when(mockCon.prepareStatement(anyString())).thenReturn(mockPs);
        when(mockPs.executeQuery()).thenReturn(mockRs);
        when(mockRs.next()).thenReturn(false);

        assertFalse(dao.isEmailExists("new@test.com"));
    }

    @Test
    void testRegisterUser_Fail() throws Exception {
        when(mockCon.prepareStatement(anyString())).thenReturn(mockPs);
        when(mockPs.executeUpdate()).thenReturn(0);

        assertFalse(dao.registerUser(new User()));
    }

    @Test
    void testRegisterArtist_Fail() throws Exception {
        when(mockCon.prepareStatement(anyString())).thenReturn(mockPs);
        when(mockPs.executeUpdate()).thenReturn(0);

        assertFalse(dao.registerArtist(new User()));
    }
}

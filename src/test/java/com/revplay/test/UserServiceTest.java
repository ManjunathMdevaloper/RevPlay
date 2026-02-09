package com.revplay.test;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.revplay.dao.UserDAO;
import com.revplay.model.User;
import com.revplay.service.UserServiceImpl;

import java.lang.reflect.Field;

@ExtendWith(MockitoExtension.class)
class UserServiceTest {

    @Mock
    private UserDAO userDAO;

    @InjectMocks
    private UserServiceImpl userService;

    @BeforeEach
    void setUp() throws Exception {
        // Manually inject the mock because UserServiceImpl initializes it inline
        Field field = UserServiceImpl.class.getDeclaredField("userDAO");
        field.setAccessible(true);
        field.set(userService, userDAO);
    }

    @Test
    void testValidLogin() {
        User mockUser = new User();
        mockUser.setEmail("test@test.com");
        mockUser.setRole("USER");

        when(userDAO.login("test@test.com", "pass")).thenReturn(mockUser);

        User result = userService.login("test@test.com", "pass");

        assertNotNull(result);
        assertEquals("USER", result.getRole());
        verify(userDAO).login("test@test.com", "pass");
    }

    @Test
    void testInvalidLogin() {
        when(userDAO.login("wrong@test.com", "wrong")).thenReturn(null);

        User result = userService.login("wrong@test.com", "wrong");

        assertNull(result);
    }

    @Test
    void testNullLogin() {
        assertNull(userService.login(null, "pass"));
        assertNull(userService.login("email", null));
    }

    @Test
    void testRegisterUser() {
        User user = new User();
        user.setEmail("new@test.com");

        when(userDAO.isEmailExists("new@test.com")).thenReturn(false);
        when(userDAO.registerUser(user)).thenReturn(true);

        assertTrue(userService.registerUser(user));
    }

    @Test
    void testRegisterUserExistingEmail() {
        User user = new User();
        user.setEmail("existing@test.com");

        when(userDAO.isEmailExists("existing@test.com")).thenReturn(true);

        assertFalse(userService.registerUser(user));
    }

    @Test
    void testRegisterArtist() {
        User user = new User();
        user.setEmail("artist@test.com");

        when(userDAO.isEmailExists("artist@test.com")).thenReturn(false);
        when(userDAO.registerArtist(user)).thenReturn(true);

        assertTrue(userService.registerArtist(user));
    }

    @Test
    void testGetSecurityQuestion() {
        when(userDAO.getSecurityQuestion("test@test.com")).thenReturn("How?");
        assertEquals("How?", userService.getSecurityQuestion("test@test.com"));
    }

    @Test
    void testValidateSecurityAnswer() {
        when(userDAO.validateSecurityAnswer("test@test.com", "Answer")).thenReturn(true);
        assertTrue(userService.validateSecurityAnswer("test@test.com", "Answer"));
    }

    @Test
    void testGetPasswordHint() {
        when(userDAO.getPasswordHint("test@test.com")).thenReturn("Hint");
        assertEquals("Hint", userService.getPasswordHint("test@test.com"));
    }

    @Test
    void testGetPassword() {
        when(userDAO.getPassword("test@test.com")).thenReturn("pass");
        assertEquals("pass", userService.getPassword("test@test.com"));
    }

    @Test
    void testGetUserIdByEmail() {
        when(userDAO.getUserIdByEmail("test@test.com")).thenReturn(1);
        assertEquals(1, userService.getUserIdByEmail("test@test.com"));
    }

    @Test
    void testUpdatePassword() {
        when(userDAO.updatePassword("test@test.com", "newPass")).thenReturn(true);
        assertTrue(userService.updatePassword("test@test.com", "newPass"));
    }
}

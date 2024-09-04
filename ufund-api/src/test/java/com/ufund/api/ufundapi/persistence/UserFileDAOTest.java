package com.ufund.api.ufundapi.persistence;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

import java.io.File;
import java.io.IOException;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.ufund.api.ufundapi.model.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

/**
 * Test class for the Need class.
 * 
 * @Author: Jaden Jovanelly
 */
@Tag("Persistence-tier")
public class UserFileDAOTest {
    UserFileDAO userFileDAO;
    User[] testUsers;
    ObjectMapper mockObjectMapper;

    @BeforeEach
    public void setupUserFileDAO() throws IOException {
        mockObjectMapper = mock(ObjectMapper.class);
        testUsers = new User[3];
        testUsers[0] = new User("user1");
        testUsers[1] = new User("user2");
        testUsers[2] = new User("user3");

        when(mockObjectMapper.readValue(any(File.class), eq(User[].class))).thenReturn(testUsers);

        userFileDAO = new UserFileDAO("doesnt_matter.txt", mockObjectMapper);
    }

    @Test
    public void testGetUsers() throws IOException {
        User[] users = userFileDAO.getUsers();
        assertArrayEquals(users, testUsers);
    }

    @Test
    public void testGetUser() throws IOException {
        User user = userFileDAO.getUser("user1");
        assertEquals(user, testUsers[0]);
    }

    @Test
    public void testGetUserNotFound() throws IOException {
        User user = userFileDAO.getUser("nonexistent_user");
        assertNull(user);
    }

    @Test
    public void testDeleteUser() throws IOException {
        boolean result = assertDoesNotThrow(() -> userFileDAO.deleteUser("user1"), "Unexpected exception thrown");
        assertTrue(result);
        assertEquals(userFileDAO.getUsersArray().size(), testUsers.length - 1);
    }

    @Test
    public void testDeleteUserNotFound() throws IOException {
        boolean result = assertDoesNotThrow(() -> userFileDAO.deleteUser("nonexistent_user"), "Unexpected exception thrown");
        assertFalse(result);
        assertEquals(userFileDAO.getUsersArray().size(), testUsers.length);
    }

    // @Test
    // public void testCreateUser() throws IOException {
    //     User newUser = new User("user4");
    //     doReturn(null).when(mockObjectMapper).writeValue(any(File.class), eq(testUsers));
    //     User createdUser = userFileDAO.createUser(newUser);
    //     assertNotNull(createdUser);
    //     assertEquals(newUser, createdUser);
    // }

    // @Test
    // public void testUpdateUser() throws IOException {
    //     User updatedUser = new User("user1");
    //     doReturn(null).when(mockObjectMapper).writeValue(any(File.class), eq(testUsers));
    //     User result = userFileDAO.updateUser(updatedUser);
    //     assertNotNull(result);
    //     assertEquals(updatedUser, result);
    // }

    @Test
    public void testUpdateUserNotFound() throws IOException {
        User updatedUser = new User("nonexistent_user");
        User result = userFileDAO.updateUser(updatedUser);
        assertNull(result);
    }
}
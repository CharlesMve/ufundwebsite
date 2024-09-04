package com.ufund.api.ufundapi.controller;

import com.ufund.api.ufundapi.model.Need;
import com.ufund.api.ufundapi.model.User;
import com.ufund.api.ufundapi.persistence.UserDAO;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.io.IOException;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.*;

/**
 * Test class for the UserController class.
 * 
 * @Author: Jaden Jovanelly
 */

 //@Tag("Controller-tier")
public class UserControllerTest {
    private UserController userController;

    @Mock
    private UserDAO userDAO;

    private User testUser;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.initMocks(this);
        userController = new UserController(userDAO);

        testUser = new User("testUser");
    }

    @Test
    public void testCreateUser() throws IOException {
        when(userDAO.getUser("testUser")).thenReturn(null);
        when(userDAO.createUser(testUser)).thenReturn(testUser);

        ResponseEntity<User> response = userController.createUser(testUser);
        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        assertEquals(testUser, response.getBody());
    }

    @Test
    public void testCreateUserConflict() throws IOException {
        when(userDAO.getUser("testUser")).thenReturn(testUser);

        ResponseEntity<User> response = userController.createUser(testUser);
        assertEquals(HttpStatus.CONFLICT, response.getStatusCode());
    }

    @Test
    public void testCreateUserInternalError() throws IOException {
        when(userDAO.getUser("testUser")).thenThrow(new IOException());

        ResponseEntity<User> response = userController.createUser(testUser);
        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
    }

    @Test
    public void testDeleteUser() throws IOException {
        when(userDAO.deleteUser("testUser")).thenReturn(true);

        ResponseEntity<Need> response = userController.deleteUser("testUser");
        assertEquals(HttpStatus.OK, response.getStatusCode());
    }

    @Test
    public void testDeleteUserNotFound() throws IOException {
        when(userDAO.deleteUser("testUser")).thenReturn(false);

        ResponseEntity<Need> response = userController.deleteUser("testUser");
        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
    }

    @Test
    public void testDeleteUserInternalError() throws IOException {
        when(userDAO.deleteUser("testUser")).thenThrow(new IOException());

        ResponseEntity<Need> response = userController.deleteUser("testUser");
        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
    }

    @Test
    public void testGetUserFromName() throws IOException {
        when(userDAO.getUser("testUser")).thenReturn(testUser);

        ResponseEntity<User> response = userController.getUserFromName("testUser");
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(testUser, response.getBody());
    }

    @Test
    public void testGetUserFromNameNotFound() throws IOException {
        when(userDAO.getUser("testUser")).thenReturn(null);

        ResponseEntity<User> response = userController.getUserFromName("testUser");
        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
    }

    // @Test
    // public void testGetUserFromNameInternalError() throws IOException {
    //     when(userDAO.getUser("testUser")).thenThrow(new IOException());

    //     ResponseEntity<User> response = userController.getUserFromName("testUser");
    //     assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
    // }

    @Test
    public void testCheckUserExists() throws IOException {
        when(userDAO.getUser("testUser")).thenReturn(testUser);

        ResponseEntity<Boolean> response = userController.checkUserExists("testUser");
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertTrue(response.getBody());
    }

    @Test
    public void testCheckUserExistsNotFound() throws IOException {
        when(userDAO.getUser("testUser")).thenReturn(null);

        ResponseEntity<Boolean> response = userController.checkUserExists("testUser");
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertFalse(response.getBody());
    }

    // @Test
    // public void testCheckUserExistsInternalError() throws IOException {
    //     when(userDAO.getUser("testUser")).thenThrow(new IOException());

    //     ResponseEntity<Boolean> response = userController.checkUserExists("testUser");
    //     assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
    // }

    @Test
    public void testGetUsers() throws IOException {
        User[] users = {testUser};
        when(userDAO.getUsers()).thenReturn(users);

        ResponseEntity<User[]> response = userController.getUsers();
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(users, response.getBody());
    }

    @Test
    public void testUpdateUser() throws IOException {
        when(userDAO.getUser("testUser")).thenReturn(testUser);
        when(userDAO.updateUser(testUser)).thenReturn(testUser);

        ResponseEntity<User> response = userController.updateUser(testUser);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(testUser, response.getBody());
    }

    @Test
    public void testUpdateUserNotFound() throws IOException {
        when(userDAO.getUser("testUser")).thenReturn(null);

        ResponseEntity<User> response = userController.updateUser(testUser);
        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
    }

//     @Test
//     public void testUpdateUserInternalError() throws IOException {
//         when(userDAO.getUser("testUser")).thenThrow(new IOException());

//         ResponseEntity<User> response = userController.updateUser(testUser);
//         assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
//     }
// }
}
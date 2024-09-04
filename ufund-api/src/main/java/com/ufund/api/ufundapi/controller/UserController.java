package com.ufund.api.ufundapi.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.ufund.api.ufundapi.model.Need;
import com.ufund.api.ufundapi.model.User;
import com.ufund.api.ufundapi.persistence.UserDAO;

import java.io.IOException;

/**
 * Handles the REST API requests for the Hero resource
 * <p>
 * {@literal @}RestController Spring annotation identifies this class as a REST
 * API
 * method handler to the Spring framework
 * 
 * @author Hakan Gul
 */

@RestController
@RequestMapping("users")
public class UserController {
    private UserDAO userDao;

    /**
     * Creates a REST API controller to reponds to requests
     * 
     * @param userDao The {@link UserDAO Need Data Access Object} to perform CRUD
     *                operations
     *                <br>
     *                This dependency is injected by the Spring Framework
     */
    public UserController(UserDAO userDAO) {
        this.userDao = userDAO;
    }

    /**
     * Creates a {@linkplain Need need} with the provided need object
     * 
     * @param need - The {@link Need need} to create
     * 
     * @return ResponseEntity with created {@link Need need} object and HTTP status
     *         of CREATED<br>
     *         ResponseEntity with HTTP status of CONFLICT if {@link Need need}
     *         object already exists<br>
     *         ResponseEntity with HTTP status of INTERNAL_SERVER_ERROR otherwise
     */
    @PostMapping("/register")
    public ResponseEntity<User> createUser(@RequestBody User user) {
        try {
            boolean doesUserExist = userDao.getUser(user.getUsername()) != null;
            if (doesUserExist) {
                return new ResponseEntity<>(HttpStatus.CONFLICT);
            }
            user.setMoney(1000);
            User createdUser = userDao.createUser(user);
            if (createdUser != null) {
                return new ResponseEntity<User>(createdUser, HttpStatus.CREATED);
            } else {
                return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
            }
        } catch (IOException e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    /**
     * Deletes a {@linkplain Need need} with the given id
     * 
     * @param id The id of the {@link Need need} to deleted
     * 
     * @return ResponseEntity HTTP status of OK if deleted<br>
     *         ResponseEntity with HTTP status of NOT_FOUND if not found<br>
     *         ResponseEntity with HTTP status of INTERNAL_SERVER_ERROR otherwise
     */
    @DeleteMapping("/{userName}")
    public ResponseEntity<Need> deleteUser(@PathVariable String userName) {
        try {
            boolean didDeleteSuccessfully = userDao.deleteUser(userName);
            if (didDeleteSuccessfully) {
                return new ResponseEntity<>(HttpStatus.OK);
            } else {
                return new ResponseEntity<>(HttpStatus.NOT_FOUND);
            }
        } catch (IOException e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    /**
     * Responds to the GET request for the {@linkplain Need need} that matches the
     * given id
     * 
     * @param id The id parameter which is the ID for the {@link Need need}
     * 
     * @return ResponseEntity with the matching {@link Need need} object and
     *         HTTP status of OK<br>
     *         ResponseEntity with HTTP status of INTERNAL_SERVER_ERROR otherwise
     *         <p>
     *         Example: Find the need that has the id 3
     *         GET http://localhost:8080/cupboard/needs/3
     */
    @GetMapping("/{userName}")
    public ResponseEntity<User> getUserFromName(@PathVariable String userName) {
        try {
            User user = userDao.getUser(userName);

            if (user != null) {
                return new ResponseEntity<User>(user, HttpStatus.OK);
            } else {
                return new ResponseEntity<>(HttpStatus.NOT_FOUND);
            }
        } catch (IOException e) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @GetMapping("/check/{userName}")
    public ResponseEntity<Boolean> checkUserExists(@PathVariable String userName) {
        try {
            User user = userDao.getUser(userName);

            return new ResponseEntity<Boolean>(user != null, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @GetMapping("")
    public ResponseEntity<User[]> getUsers() {
        try {
            User[] users = userDao.getUsers();
            // User[] users = new User[1];
            // users[0] = new User("Test");

            return new ResponseEntity<User[]>(users, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    /**
     * Updates the {@linkplain Need need} with the provided {@linkplain Need need}
     * object, if it exists
     * 
     * @param user The {@link Need need} to update
     * 
     * @return ResponseEntity with updated {@link Need need} object and HTTP status
     *         of OK if updated<br>
     *         ResponseEntity with HTTP status of NOT_FOUND if not found<br>
     *         ResponseEntity with HTTP status of INTERNAL_SERVER_ERROR otherwise
     */
    @PutMapping("/{userName}")
    public ResponseEntity<User> updateUser(@RequestBody User user) {
        try {
            User userToUpdate = userDao.getUser(user.getUsername());

            if (userToUpdate != null) {
                userToUpdate.setUserName(user.getUsername());
                userToUpdate.setBasket(user.getBasket());
                userToUpdate.setMoney(user.getMoney()); // Update money
                User updatedUser = userDao.updateUser(userToUpdate);
                return new ResponseEntity<User>(updatedUser, HttpStatus.OK);
            } else {
                return new ResponseEntity<>(HttpStatus.NOT_FOUND);
            }
        } catch (IOException e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

}
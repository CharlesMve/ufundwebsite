package com.ufund.api.ufundapi.persistence;

import java.io.IOException;

import com.ufund.api.ufundapi.model.Need;
import com.ufund.api.ufundapi.model.User;

/**
 * Defines the interface for User object persistence
 * 
 * @author SWEN Faculty
 */
public interface UserDAO {
    /**
     * Retrieves a {@linkplain Need Need} with the given id
     * 
     * @param id The id of the {@link Need Need} to get
     * 
     * @return a {@link Need Need} object with the matching id
     *         <br>
     *         null if no {@link Need Need} with a matching id is found
     * 
     * @throws IOException if an issue with underlying storage
     */
    User getUser(String userName) throws IOException;

    User[] getUsers();

    /**
     * Creates and saves a {@linkplain Need Need}
     * 
     * @param Need {@linkplain Need Need} object to be created and saved
     *             <br>
     *             The id of the Need object is ignored and a new uniqe id is
     *             assigned
     *
     * @return new {@link Need Need} if successful, false otherwise
     * 
     * @throws IOException if an issue with underlying storage
     */
    User createUser(User user) throws IOException;

    /**
     * Updates and saves a {@linkplain Need Need}
     * 
     * @param {@link Need Need} object to be updated and saved
     * 
     * @return updated {@link Need Need} if successful, null if
     *         {@link Need Need} could not be found
     * 
     * @throws IOException if underlying storage cannot be accessed
     */
    User updateUser(User user) throws IOException;

    /**
     * Deletes a {@linkplain Need Need} with the given id
     * 
     * @param id The id of the {@link Need Need}
     * 
     * @return true if the {@link Need Need} was deleted
     *         <br>
     *         false if Need with the given id does not exist
     * 
     * @throws IOException if underlying storage cannot be accessed
     */
    boolean deleteUser(String userName) throws IOException;
}
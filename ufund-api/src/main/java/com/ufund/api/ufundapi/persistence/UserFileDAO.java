package com.ufund.api.ufundapi.persistence;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.ufund.api.ufundapi.model.Need;
import com.ufund.api.ufundapi.model.User;

@Component
public class UserFileDAO implements UserDAO {
    // private static final Logger LOG =
    // Logger.getLogger(NeedFileDAO.class.getName());
    List<User> Users; // Provides a local cache of the Need objects
                      // so that we don't need to read from the file
                      // each time
    private ObjectMapper objectMapper; // Provides conversion between Need
                                       // objects and JSON text format written
                                       // to the file
    private String filename; // Filename to read from and write to

    /**
     * Creates a Need File Data Access Object
     * 
     * @param filename     Filename to read from and write to
     * @param objectMapper Provides JSON Object to/from Java Object serialization
     *                     and deserialization
     * 
     * @throws IOException when file cannot be accessed or read from
     */
    public UserFileDAO(@Value("${Users.file}") String filename, ObjectMapper objectMapper) throws IOException {
        this.filename = filename;
        this.objectMapper = objectMapper;
        load(); // load the Needs from the file
    }

    /**
     * Saves the {@linkplain Need Needs} from the map into the file as an array of
     * JSON objects
     * 
     * @return true if the {@link Need Needs} were written successfully
     * 
     * @throws IOException when file cannot be accessed or written to
     */
    private boolean save() throws IOException {
        User[] UserArray = (User[]) getUsersArray().toArray();

        // Serializes the Java Objects to JSON objects into the file
        // writeValue will thrown an IOException if there is an issue
        // with the file or reading from the file
        objectMapper.writeValue(new File(filename), UserArray);
        return true;
    }

    /**
     * Loads {@linkplain Need Needs} from the JSON file into the map
     * <br>
     * Also sets next id to one more than the greatest id found in the file
     * 
     * @return true if the file was read successfully
     * 
     * @throws IOException when file cannot be accessed or read from
     */
    private boolean load() throws IOException {
        // Deserializes the JSON objects from the file into an array of Needs
        // readValue will throw an IOException if there's an issue with the file
        // or reading from the file
        User[] UserArray = objectMapper.readValue(new File(filename), User[].class);

        Users = new ArrayList<>(Arrays.asList(UserArray));
        return true;
    }

    @Override
    public User getUser(String userName) throws IOException {
        for (int i = 0; i < this.Users.size(); i++) {
            if (this.Users.get(i).getUsername().equals(userName)) {
                return this.Users.get(i);
            }
        }
        return null;
    }

    public User[] getUsers() {
        return getUsersArray().toArray(new User[0]);
    }

    @Override
    public User createUser(User user) throws IOException {
        this.Users.add(user);
        return user;
    }

    @Override
    public User updateUser(User user) throws IOException {
        for (int i = 0; i < this.Users.size(); i++) {
            if (this.Users.get(i).getUsername().equals(user.getUsername())) {
                this.Users.get(i).setUserName(user.getUsername());
                this.Users.get(i).setBasket(user.getBasket());
                return this.Users.get(i);
            }
        }
        return null;
    }

    @Override
    public boolean deleteUser(String userName) throws IOException {
        for (int i = 0; i < this.Users.size(); i++) {
            if (this.Users.get(i).getUsername().equals(userName)) {
                this.Users.remove(i);
                return true;
            }
        }
        return false;
    }

    public List<User> getUsersArray() {
        return this.Users;
    }
}

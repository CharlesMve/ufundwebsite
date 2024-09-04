package com.ufund.api.ufundapi.persistence;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Map;
import java.util.TreeMap;
//import java.util.logging.Logger;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.ufund.api.ufundapi.model.Need;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

/**
 * Implements the functionality for JSON file-based peristance for Needs
 * 
 * {@literal @}Component Spring annotation instantiates a single instance of
 * this
 * class and injects the instance into other classes as needed
 * 
 * @author SWEN Faculty
 */
@Component
public class NeedFileDAO implements NeedDAO {
    // private static final Logger LOG =
    // Logger.getLogger(NeedFileDAO.class.getName());
    Map<Integer, Need> Needs; // Provides a local cache of the Need objects
                              // so that we don't need to read from the file
                              // each time
    private ObjectMapper objectMapper; // Provides conversion between Need
                                       // objects and JSON text format written
                                       // to the file
    private static int nextId; // The next Id to assign to a new Need
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
    public NeedFileDAO(@Value("${Needs.file}") String filename, ObjectMapper objectMapper) throws IOException {
        this.filename = filename;
        this.objectMapper = objectMapper;
        load(); // load the Needs from the file
    }

    /**
     * Generates the next id for a new {@linkplain Need Need}
     * 
     * @return The next id
     */
    private synchronized static int nextId() {
        int id = nextId;
        ++nextId;
        return id;
    }

    /**
     * Generates an array of {@linkplain Need Needs} from the tree map
     * 
     * @return The array of {@link Need Needs}, may be empty
     */
    private Need[] getNeedsArray() {
        return getNeedsArray(null);
    }

    /**
     * Generates an array of {@linkplain Need Needs} from the tree map for any
     * {@linkplain Need Needs} that contains the text specified by containsText
     * <br>
     * If containsText is null, the array contains all of the {@linkplain Need
     * Needs}
     * in the tree map
     * 
     * @return The array of {@link Need Needs}, may be empty
     */
    private Need[] getNeedsArray(String containsText) { // if containsText == null, no filter
        ArrayList<Need> NeedArrayList = new ArrayList<>();

        for (Need Need : Needs.values()) {
            if (containsText == null || Need.getName().contains(containsText)) {
                NeedArrayList.add(Need);
            }
        }

        Need[] NeedArray = new Need[NeedArrayList.size()];
        NeedArrayList.toArray(NeedArray);
        return NeedArray;
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
        Need[] NeedArray = getNeedsArray();

        // Serializes the Java Objects to JSON objects into the file
        // writeValue will thrown an IOException if there is an issue
        // with the file or reading from the file
        objectMapper.writeValue(new File(filename), NeedArray);
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
        Needs = new TreeMap<>();
        nextId = 0;

        // Deserializes the JSON objects from the file into an array of Needs
        // readValue will throw an IOException if there's an issue with the file
        // or reading from the file
        Need[] NeedArray = objectMapper.readValue(new File(filename), Need[].class);

        // Add each Need to the tree map and keep track of the greatest id
        for (Need Need : NeedArray) {
            Needs.put(Need.getId(), Need);
            if (Need.getId() > nextId)
                nextId = Need.getId();
        }
        // Make the next id one greater than the maximum from the file
        ++nextId;
        return true;
    }

    /**
     ** {@inheritDoc}
     */
    @Override
    public Need getNeed(int id) {
        synchronized (Needs) {
            if (Needs.containsKey(id))
                return Needs.get(id);
            else
                return null;
        }
    }

    /**
     ** {@inheritDoc}
     */
    @Override
    public Need createNeed(Need Need) throws IOException {
        synchronized (Needs) {
            // We create a new Need object because the id field is immutable
            // and we need to assign the next unique id
            Need newNeed = new Need(nextId(), Need.getName(), Need.getCost(), Need.getQuantity());
            Needs.put(newNeed.getId(), newNeed);
            save(); // may throw an IOException
            return newNeed;
        }
    }

    /**
     ** {@inheritDoc}
     */
    @Override
    public Need updateNeed(Need Need) throws IOException {
        synchronized (Needs) {
            if (Needs.containsKey(Need.getId()) == false)
                return null; // Need does not exist

            Needs.put(Need.getId(), Need);
            save(); // may throw an IOException
            return Need;
        }
    }

    /**
     ** {@inheritDoc}
     */
    @Override
    public boolean deleteNeed(int id) throws IOException {
        synchronized (Needs) {
            if (Needs.containsKey(id)) {
                Needs.remove(id);
                return save();
            } else
                return false;
        }
    }

    @Override
    public Need[] getNeeds() throws IOException {
        return getNeedsArray();
    }

    @Override
    public Need[] findNeeds(String containsText) throws IOException {
        /*
         * Need[] needs = getNeedsArray(containsText)
         * Need[] newNeeds = new Need[] {};
         * for (int i = 0; i < newNeeds.length; i++) {
         * if (this.needs[i].getName().contains(containsText)) {
         * newNeeds.add(needs[i]);
         * }
         * }
         */
        return getNeedsArray(containsText);
    }
}
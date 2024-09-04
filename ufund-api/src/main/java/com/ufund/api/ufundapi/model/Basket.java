package com.ufund.api.ufundapi.model;

import java.util.ArrayList;
import java.util.List;

/**
 * Represents funding basket 
 * 
 * @author Jaden Jovanelly
 */
public class Basket {
    private List<Need> needs;

    /**
     * Creates a new Basket with an empty list of needs.
     */
    public Basket() {
        this.needs = new ArrayList<>();
    }

    /**
     * Gets the list of needs in the basket.
     *
     * @return List of needs in the basket
     */
    public List<Need> getNeeds() {
        return needs;
    }

    /**
     * Adds a need to the basket.
     *
     * @param need The need to add to the basket
     */
    public void addNeed(Need need) {
        needs.add(need);
    }

    /**
     * Removes a need from the basket.
     *
     * @param need The need to remove from the basket
     */
    public void removeNeed(Need need) {
        needs.remove(need);
    }
}


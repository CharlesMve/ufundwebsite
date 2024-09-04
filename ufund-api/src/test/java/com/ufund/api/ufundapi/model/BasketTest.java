package com.ufund.api.ufundapi.model;

import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.assertFalse;

import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

import java.util.List;


/**
 * Test class for the Basket class.
 * 
 * @Author: Jaden Jovanelly
 */

 @Tag("Model-tier")
public class BasketTest {

    @Test
    public void testBasketConstructor() {
        // Create a new Basket
        Basket basket = new Basket();

        // Verify that the constructor initializes an empty list of needs
        List<Need> needs = basket.getNeeds();
        assertTrue(needs.isEmpty());
    }

    @Test
    public void testAddNeed() {
        // Create a new Basket
        Basket basket = new Basket();

        // Create a new Need
        Need need = new Need(1, "Test Need", 100, 5);

        // Add the Need to the Basket
        basket.addNeed(need);

        // Verify that the Need is in the Basket
        List<Need> needs = basket.getNeeds();
        assertTrue(needs.contains(need));
    }

    @Test
    public void testRemoveNeed() {
        // Create a new Basket
        Basket basket = new Basket();

        // Create a new Need
        Need need = new Need(1, "Test Need", 100, 5);

        // Add the Need to the Basket
        basket.addNeed(need);

        // Remove the Need from the Basket
        basket.removeNeed(need);

        // Verify that the Need is no longer in the Basket
        List<Need> needs = basket.getNeeds();
        assertFalse(needs.contains(need));
    }
}

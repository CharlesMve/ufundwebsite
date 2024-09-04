package com.ufund.api.ufundapi.model;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

/**
 * Test class for the Need class.
 * 
 * @Author: Jaden Jovanelly
 */

 @Tag("Model-tier")
public class NeedTest {

    @Test
    public void testNeedConstructor() {
        // Create a new Need
        Need need = new Need(1, "Test Need", 100, 5);

        // Verify that the constructor initializes the fields correctly
        assertEquals(1, need.getId());
        assertEquals("Test Need", need.getName());
        assertEquals(100, need.getCost());
        assertEquals(5, need.getQuantity());
    }

    @Test
    public void testSetName() {
        // Create a new Need
        Need need = new Need(1, "Test Need", 100, 5);

        // Set a new name
        need.setName("New Name");

        // Verify that the name is updated
        assertEquals("New Name", need.getName());
    }

    @Test
    public void testSetCost() {
        // Create a new Need
        Need need = new Need(1, "Test Need", 100, 5);

        // Set a new cost
        need.setCost(200);

        // Verify that the cost is updated
        assertEquals(200, need.getCost());
    }

    @Test
    public void testSetQuantity() {
        // Create a new Need
        Need need = new Need(1, "Test Need", 100, 5);

        // Set a new quantity
        need.setQuantity(10);

        // Verify that the quantity is updated
        assertEquals(10, need.getQuantity());
    }

    @Test
    public void testToString() {
        // Create a new Need
        Need need = new Need(1, "Test Need", 100, 5);

        // Verify that the toString method returns the expected string
        String expectedString = "Need [id=1, name=Test Need cost=100 quantity=5]";
        assertEquals(expectedString, need.toString());
    }
}

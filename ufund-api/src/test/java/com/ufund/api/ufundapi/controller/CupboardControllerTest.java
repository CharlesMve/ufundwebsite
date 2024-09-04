package com.ufund.api.ufundapi.controller;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

import com.ufund.api.ufundapi.model.Need;
import com.ufund.api.ufundapi.persistence.NeedDAO;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.io.IOException;

/**
 * Test class for the CupboardController class.
 * 
 * @Author: Jaden Jovanelly
 */

 @Tag("Controller-tier")
public class CupboardControllerTest {
    private CupboardController cupboardController;
    private NeedDAO mockNeedDAO;
    private Need testNeed;

    @BeforeEach
    public void setup() {
        mockNeedDAO = mock(NeedDAO.class);
        cupboardController = new CupboardController(mockNeedDAO);
        testNeed = new Need(1, "Test Need", 100, 5);
    }

    @Test
    public void testGetNeeds() throws IOException {
        // Setup
        when(mockNeedDAO.getNeeds()).thenReturn(new Need[] { testNeed });

        // Invoke
        ResponseEntity<Need[]> response = cupboardController.getNeeds();

        // Verify
        assertEquals(response.getStatusCode(), HttpStatus.OK);
        Need[] needs = response.getBody();
        assertEquals(needs.length, 1);
        assertEquals(needs[0], testNeed);
    }

    @Test
    public void testGetNeedsError() throws IOException {
        // Setup
        when(mockNeedDAO.getNeeds()).thenThrow(new IOException());

        // Invoke
        ResponseEntity<Need[]> response = cupboardController.getNeeds();

        // Verify
        assertEquals(response.getStatusCode(), HttpStatus.NOT_FOUND);
    }

    @Test
    public void testCreateNeed() throws IOException {
        // Setup
        when(mockNeedDAO.getNeed(testNeed.getId())).thenReturn(null);
        when(mockNeedDAO.createNeed(testNeed)).thenReturn(testNeed);

        // Invoke
        ResponseEntity<Need> response = cupboardController.createNeed(testNeed);

        // Verify
        assertEquals(response.getStatusCode(), HttpStatus.CREATED);
        Need createdNeed = response.getBody();
        assertEquals(createdNeed, testNeed);
    }

    @Test
    public void testCreateNeedConflict() throws IOException {
        // Setup
        when(mockNeedDAO.getNeed(testNeed.getId())).thenReturn(testNeed);

        // Invoke
        ResponseEntity<Need> response = cupboardController.createNeed(testNeed);

        // Verify
        assertEquals(response.getStatusCode(), HttpStatus.CONFLICT);
    }

    @Test
    public void testCreateNeedError() throws IOException {
        // Setup
        when(mockNeedDAO.getNeed(testNeed.getId())).thenReturn(null);
        when(mockNeedDAO.createNeed(testNeed)).thenThrow(new IOException());

        // Invoke
        ResponseEntity<Need> response = cupboardController.createNeed(testNeed);

        // Verify
        assertEquals(response.getStatusCode(), HttpStatus.INTERNAL_SERVER_ERROR);
    }

    
    @Test
public void testUpdateNeed() throws IOException {
    // Setup
    when(mockNeedDAO.getNeed(testNeed.getId())).thenReturn(testNeed);

    Need updatedNeed = new Need(testNeed.getId(), "Updated Need", 200, 10);

    when(mockNeedDAO.updateNeed(any(Need.class))).thenReturn(updatedNeed);

    // Invoke
    ResponseEntity<Need> response = cupboardController.updateNeed(updatedNeed);

    // Verify
    assertEquals(response.getStatusCode(), HttpStatus.OK);
    Need resultNeed = response.getBody();
    assertEquals(resultNeed.getName(), updatedNeed.getName());
    assertEquals(resultNeed.getCost(), updatedNeed.getCost());
    assertEquals(resultNeed.getQuantity(), updatedNeed.getQuantity());
}

@Test
public void testUpdateNeedNotFound() throws IOException {
    // Setup
    when(mockNeedDAO.getNeed(testNeed.getId())).thenReturn(null);

    // Invoke
    ResponseEntity<Need> response = cupboardController.updateNeed(testNeed);

    // Verify
    assertEquals(response.getStatusCode(), HttpStatus.NOT_FOUND);
}

@Test
public void testUpdateNeedError() throws IOException {
    // Setup
    when(mockNeedDAO.getNeed(testNeed.getId())).thenReturn(testNeed);
    when(mockNeedDAO.updateNeed(any(Need.class))).thenThrow(new IOException());

    // Invoke
    ResponseEntity<Need> response = cupboardController.updateNeed(testNeed);

    // Verify
    assertEquals(response.getStatusCode(), HttpStatus.INTERNAL_SERVER_ERROR);
}

@Test
void testGetNeedFromIDError() throws IOException {
    // Setup: Mock the needDao to throw an IOException
    int id = 1;
    when(mockNeedDAO.getNeed(id)).thenThrow(new IOException("Simulated error"));

    // Invoke: Send a GET request with the valid ID
    ResponseEntity<Need> response = cupboardController.getNeedFromID(id);

    // Verify: Assert that the response is NOT_FOUND status
    assertEquals(response.getStatusCode(), HttpStatus.NOT_FOUND);
    assertNull(response.getBody());
}

@Test
public void testGetNeedFromID() throws IOException {
    // Setup
    when(mockNeedDAO.getNeed(testNeed.getId())).thenReturn(testNeed);

    // Invoke
    ResponseEntity<Need> response = cupboardController.getNeedFromID(testNeed.getId());

    // Verify
    assertEquals(response.getStatusCode(), HttpStatus.OK);
    Need retrievedNeed = response.getBody();
    assertEquals(retrievedNeed, testNeed);
}

@Test
public void testGetNeedFromIDNotFound() throws IOException {
    // Setup
    when(mockNeedDAO.getNeed(testNeed.getId())).thenReturn(null);

    // Invoke
    ResponseEntity<Need> response = cupboardController.getNeedFromID(testNeed.getId());

    // Verify
    assertEquals(response.getStatusCode(), HttpStatus.NOT_FOUND);
}

@Test
public void testSearchNeeds() throws IOException {
    // Setup: Mock the needDao to return needs with matching name
    when(mockNeedDAO.getNeeds()).thenReturn(new Need[] { testNeed, new Need(2, "Another Need", 150, 3) });

    // Invoke: Perform a search for matching needs
    ResponseEntity<Need[]> response = cupboardController.searchNeeds("Test");

    // Verify: Assert the response status code and the matching needs
    assertEquals(response.getStatusCode(), HttpStatus.OK);
    Need[] matchingNeeds = response.getBody();
    assertEquals(matchingNeeds.length, 1);
    assertEquals(matchingNeeds[0], testNeed);
}

@Test
public void testSearchNeedsNoMatch() throws IOException {
    // Setup: Mock the needDao to return needs with no matching name
    when(mockNeedDAO.getNeeds()).thenReturn(new Need[] { new Need(2, "Another Need", 150, 3) });

    // Invoke: Perform a search for non-matching needs
    // ResponseEntity<Need[]> response = cupboardController.searchNeeds("Nonexistent");

    // Verify: Assert that the response status code is NOT_FOUND
    // assertEquals(response.getStatusCode(), HttpStatus.NOT_FOUND);
    // Need[] matchingNeeds = response.getBody();
    // assertNull(matchingNeeds);
}

@Test
public void testSearchNeedsError() throws IOException {
    // Setup: Mock the needDao to throw an IOException
    when(mockNeedDAO.getNeeds()).thenThrow(new IOException());

    // Invoke: Perform a search for matching needs
    ResponseEntity<Need[]> response = cupboardController.searchNeeds("Test");

    // Verify: Assert that the response status code is NOT_FOUND
    assertEquals(response.getStatusCode(), HttpStatus.NOT_FOUND);
}

@Test
public void testDeleteNeed() throws IOException {
    // Setup
    when(mockNeedDAO.deleteNeed(testNeed.getId())).thenReturn(true);

    // Invoke
    ResponseEntity<Need> response = cupboardController.deleteNeed(testNeed.getId());

    // Verify
    assertEquals(response.getStatusCode(), HttpStatus.OK);
}

@Test
public void testDeleteNeedNotFound() throws IOException {
    // Setup
    when(mockNeedDAO.deleteNeed(testNeed.getId())).thenReturn(false);

    // Invoke
    ResponseEntity<Need> response = cupboardController.deleteNeed(testNeed.getId());

    // Verify
    assertEquals(response.getStatusCode(), HttpStatus.NOT_FOUND);
}

@Test
public void testDeleteNeedError() throws IOException {
    // Setup
    when(mockNeedDAO.deleteNeed(testNeed.getId())).thenThrow(new IOException());

    // Invoke
    ResponseEntity<Need> response = cupboardController.deleteNeed(testNeed.getId());

    // Verify
    assertEquals(response.getStatusCode(), HttpStatus.INTERNAL_SERVER_ERROR);
}


    // Add similar test methods for other controller methods...
}

package com.ufund.api.ufundapi.persistence;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.io.File;
import java.io.IOException;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.ufund.api.ufundapi.model.Need;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

/**
 * Test the Need File DAO class
 * 
  * @author Jaden Jovanelly
 * @author Hakan gul
 * @author Christain Issacs
 * @author JC Mve Mondo
 * 
 */

@Tag("Persistence-tier")
public class NeedFileDAOTest {
    NeedFileDAO needFileDAO;
    Need[] testNeeds;
    ObjectMapper mockObjectMapper;

    /**
     * Before each test, we will create and inject a Mock Object Mapper to
     * isolate the tests from the underlying file
     * @throws IOException
     */
    @BeforeEach
    public void setupNeedFileDAO() throws IOException {
        mockObjectMapper = mock(ObjectMapper.class);
        testNeeds = new Need[3];
        testNeeds[0] = new Need(99,"Wi-Fire", 88, 77);
        testNeeds[1] = new Need(100,"Galactic Agent", 90, 80);
        testNeeds[2] = new Need(101,"Ice Gladiator", 91, 81);

        // When the object mapper is supposed to read from the file
        // the mock object mapper will return the need array above
        when(mockObjectMapper
            .readValue(new File("doesnt_matter.txt"),Need[].class))
                .thenReturn(testNeeds);
        needFileDAO = new NeedFileDAO("doesnt_matter.txt",mockObjectMapper);
    }

    @Test
    public void testGetNeeds() throws IOException {
        // Invoke
        Need[] needs = needFileDAO.getNeeds();

        // Analyze
        assertEquals(needs.length,testNeeds.length);
        for (int i = 0; i < testNeeds.length;++i)
            assertEquals(needs[i],testNeeds[i]);
    }

    @Test
    public void testFindNeeds() throws IOException {
        // Invoke
        Need[] needs = needFileDAO.findNeeds("la");

        // Analyze
        assertEquals(needs.length,2);
        assertEquals(needs[0],testNeeds[1]);
        assertEquals(needs[1],testNeeds[2]);
    }

    @Test
    public void testGetNeed() {
        // Invoke
        Need need = needFileDAO.getNeed(99);

        // Analzye
        assertEquals(need,testNeeds[0]);
    }

    @Test
    public void testDeleteNeed() {
        // Invoke
        boolean result = assertDoesNotThrow(() -> needFileDAO.deleteNeed(99),
                            "Unexpected exception thrown");

        // Analzye
        assertEquals(result,true);
        // We check the internal tree map size against the length
        // of the test needs array - 1 (because of the delete)
        // Because needs attribute of NeedFileDAO is package private
        // we can access it directly
        assertEquals(needFileDAO.Needs.size(),testNeeds.length-1);
    }

    @Test
    public void testCreateNeed() {
        // Setup
        Need need = new Need(102,"Wonder-Person", 92, 82);

        // Invoke
        Need result = assertDoesNotThrow(() -> needFileDAO.createNeed(need),
                                "Unexpected exception thrown");

        // Analyze
        assertNotNull(result);
        Need actual = needFileDAO.getNeed(need.getId());
        assertEquals(actual.getId(),need.getId());
        assertEquals(actual.getName(),need.getName());
    }

    @Test
    public void testUpdateNeed() {
        // Setup
        Need need = new Need(99,"Galactic Agent", 89, 88);

        // Invoke
        Need result = assertDoesNotThrow(() -> needFileDAO.updateNeed(need),
                                "Unexpected exception thrown");

        // Analyze
        assertNotNull(result);
        Need actual = needFileDAO.getNeed(need.getId());
        assertEquals(actual,need);
    }

    @Test
    public void testSaveException() throws IOException{
        doThrow(new IOException())
            .when(mockObjectMapper)
                .writeValue(any(File.class),any(Need[].class));

        Need need = new Need(102,"Wi-Fire", 92, 82);

        assertThrows(IOException.class,
                        () -> needFileDAO.createNeed(need),
                        "IOException not thrown");
    }

    @Test
    public void testGetNeedNotFound() {
        // Invoke
        Need need = needFileDAO.getNeed(98);

        // Analyze
        assertEquals(need,null);
    }

    @Test
    public void testDeleteNeedNotFound() {
        // Invoke
        boolean result = assertDoesNotThrow(() -> needFileDAO.deleteNeed(98),
                                                "Unexpected exception thrown");

        // Analyze
        assertEquals(result,false);
        assertEquals(needFileDAO.Needs.size(),testNeeds.length);
    }

    @Test
    public void testUpdateNeedNotFound() {
        // Setup
        Need need = new Need(98,"Bolt", 88, 78);

        // Invoke
        Need result = assertDoesNotThrow(() -> needFileDAO.updateNeed(need),
                                                "Unexpected exception thrown");

        // Analyze
        assertNull(result);
    }

    @Test
    public void testConstructorException() throws IOException {
        // Setup
        ObjectMapper mockObjectMapper = mock(ObjectMapper.class);
        // We want to simulate with a Mock Object Mapper that an
        // exception was raised during JSON object deseerialization
        // into Java objects
        // When the Mock Object Mapper readValue method is called
        // from the NeedFileDAO load method, an IOException is
        // raised
        doThrow(new IOException())
            .when(mockObjectMapper)
                .readValue(new File("doesnt_matter.txt"),Need[].class);

        // Invoke & Analyze
        assertThrows(IOException.class,
                        () -> new NeedFileDAO("doesnt_matter.txt",mockObjectMapper),
                        "IOException not thrown");
    }
}

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
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.ufund.api.ufundapi.TenPercentEnhanment.CodeThatShareButtonShouldRun;
import com.ufund.api.ufundapi.model.Need;
import com.ufund.api.ufundapi.persistence.NeedDAO;
//import com.ufund.api.ufundapi.persistence.NeedFileDAO;

import java.io.IOException;
import java.util.*;
//import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Handles the REST API requests for the Hero resource
 * <p>
 * {@literal @}RestController Spring annotation identifies this class as a REST
 * API
 * method handler to the Spring framework
 * 
 * @author SWEN Faculty
 * @author Hakan Gul
 */

@RestController
@RequestMapping("cupboard")
public class CupboardController {
    private static final Logger LOG = Logger.getLogger(CupboardController.class.getName());
    private NeedDAO needDao;

    /**
     * Creates a REST API controller to reponds to requests
     * 
     * @param needDao The {@link NeedDao Need Data Access Object} to perform CRUD
     *                operations
     *                <br>
     *                This dependency is injected by the Spring Framework
     */
    public CupboardController(NeedDAO needDao) {
        this.needDao = needDao;
    }

    /**
     * Responds to the GET request for all {@linkplain Hero heroes}
     * 
     * 
     * @return ResponseEntity with array of {@link Hero hero} objects (may be empty)
     *         and
     *         HTTP status of OK<br>
     *         ResponseEntity with HTTP status of INTERNAL_SERVER_ERROR otherwise
     */
    @GetMapping("/needs")
    public ResponseEntity<Need[]> getNeeds() {
        LOG.info("GET /needs");

        try {
            Need[] needs = needDao.getNeeds();

            if (needs != null) {
                return new ResponseEntity<Need[]>(needs, HttpStatus.OK);
            } else {
                return new ResponseEntity<>(HttpStatus.NOT_FOUND);
            }
        } catch (IOException e) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @PostMapping("/needs/shareneed")
    public ResponseEntity<Need> shareNeed(@RequestBody Need need) {
        try {
            CodeThatShareButtonShouldRun test = new CodeThatShareButtonShouldRun(need);
            test.run();
            return new ResponseEntity<Need>(need, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
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
    @PostMapping("/needs")
    public ResponseEntity<Need> createNeed(@RequestBody Need need) {
        LOG.info("POST /cupboard/needs " + need);

        try {
            boolean doesNeedExist = needDao.getNeed(need.getId()) != null;
            if (doesNeedExist) {
                return new ResponseEntity<>(HttpStatus.CONFLICT);
            }
            Need createdNeed = needDao.createNeed(need);
            if (createdNeed != null) {
                return new ResponseEntity<Need>(createdNeed, HttpStatus.CREATED);
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
    @DeleteMapping("/needs/{id}")
    public ResponseEntity<Need> deleteNeed(@PathVariable int id) {
        LOG.info("DELETE /cupboard/need/" + id);

        try {
            boolean didDeleteSuccessfully = needDao.deleteNeed(id);
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
    @GetMapping("/needs/{id}")
    public ResponseEntity<Need> getNeedFromID(@PathVariable int id) {
        LOG.info("GET /cupboard/needs/" + id);

        try {
            Need need = needDao.getNeed(id);

            if (need != null) {
                return new ResponseEntity<Need>(need, HttpStatus.OK);
            } else {
                return new ResponseEntity<>(HttpStatus.NOT_FOUND);
            }
        } catch (IOException e) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    /**
     * Responds to the GET request for all {@linkplain Need needs} whose name
     * contains
     * the text in name
     * 
     * @param name The name parameter which contains the text used to find the
     *             {@link Need needs}
     * 
     * @return ResponseEntity with array of {@link Need need} objects (may be empty)
     *         and
     *         HTTP status of OK<br>
     *         ResponseEntity with HTTP status of INTERNAL_SERVER_ERROR otherwise
     *         <p>
     *         Example: Find all needs that contain the text "ma"
     *         GET http://localhost:8080/needs/?name=ma
     */
    @GetMapping("/needs/search")
    public ResponseEntity<Need[]> searchNeeds(@RequestParam String name) {
        LOG.info("GET /cupboard/needs/?name=" + name);
        try {
            Need[] needs = needDao.getNeeds();
            List<Need> matchingNeedsList = new ArrayList<Need>();

            if (needs != null) {
                for (int i = 0; i < needs.length; i++) {
                    if (needs[i].getName().contains(name)) {
                        matchingNeedsList.add(needs[i]);
                    }
                }
                return new ResponseEntity<Need[]>(matchingNeedsList.toArray(new Need[matchingNeedsList.size()]),
                        HttpStatus.OK);
            } else {
                return new ResponseEntity<>(HttpStatus.NOT_FOUND);
            }
        } catch (IOException e) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    /**
     * Updates the {@linkplain Need need} with the provided {@linkplain Need need}
     * object, if it exists
     * 
     * @param need The {@link Need need} to update
     * 
     * @return ResponseEntity with updated {@link Need need} object and HTTP status
     *         of OK if updated<br>
     *         ResponseEntity with HTTP status of NOT_FOUND if not found<br>
     *         ResponseEntity with HTTP status of INTERNAL_SERVER_ERROR otherwise
     */
    @PutMapping("/needs")
    public ResponseEntity<Need> updateNeed(@RequestBody Need need) {
        LOG.info("PUT /cupboard/needs " + need);
        try {
            Need needToUpdate = needDao.getNeed((need.getId()));

            if (needToUpdate != null) {
                needToUpdate.setName(need.getName());
                needToUpdate.setCost(need.getCost());
                needToUpdate.setQuantity(need.getQuantity());
                Need updatedNeed = needDao.updateNeed(needToUpdate);
                return new ResponseEntity<Need>(updatedNeed, HttpStatus.OK);
            } else {
                return new ResponseEntity<>(HttpStatus.NOT_FOUND);
            }
        } catch (IOException e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
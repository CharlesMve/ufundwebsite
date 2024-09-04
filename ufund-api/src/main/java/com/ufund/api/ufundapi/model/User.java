package com.ufund.api.ufundapi.model;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Represents a generic user
 * 
 * @author Jaden Jovanelly
 */
public class User {
    @JsonProperty("username")
    private String username;
    @JsonProperty("basket")
    private Basket basket;
    @JsonProperty("money")
    private int money;

    /**
     * Creates a new user with the specified username.
     *
     * @param username The username of the user
     */
    public User(@JsonProperty("username") String username) {
        this.username = username;
        this.money = 1000;
    }

    /**
     * Gets the username of the user.
     *
     * @return The username of the user
     */
    public String getUsername() {
        return username;
    }

    public void setUserName(String userName) {
        this.username = userName;
    }

    public Basket getBasket() {
        return this.basket;
    }

    public void setBasket(Basket basket) {
        this.basket = basket;
    }

    public int getMoney() {
        return this.money;
    }

    public void setMoney(int money) {
        this.money = money;
    }
}

package com.caxerx.bean;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

public class User implements Serializable {
    private int id;
    private String username;
    private String password;
    private String firstName;
    private String lastName;
    private String email;
    private Date dateOfBirth;
    private int type;
    private List<Integer> permission;

    private Role role;


    private transient List<Restaurant> favouriteRestaurant;
    private transient List<Menu> favouriteMenu;
    private transient List<Restaurant> likedRestaurant;
    private transient List<Comment> comments;


    public User() {
    }

    public User(int id, String username, String password, String firstName, String lastName, String email, Date dateOfBirth, int type) {
        this.id = id;
        this.username = username;
        this.password = password;
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.dateOfBirth = dateOfBirth;
        this.type = type;
    }

    public List<Integer> getPermission() {
        return permission;
    }

    public void setPermission(List<Integer> permission) {
        this.permission = permission;
    }

    public Role getRole() {
        return role;
    }

    public void setRole(Role role) {
        this.role = role;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public List<Restaurant> getFavouriteRestaurant() {
        return favouriteRestaurant;
    }

    public void setFavouriteRestaurant(List<Restaurant> favouriteRestaurant) {
        this.favouriteRestaurant = favouriteRestaurant;
    }

    public List<Menu> getFavouriteMenu() {
        return favouriteMenu;
    }

    public void setFavouriteMenu(List<Menu> favouriteMenu) {
        this.favouriteMenu = favouriteMenu;
    }

    public List<Restaurant> getLikedRestaurant() {
        return likedRestaurant;
    }

    public void setLikedRestaurant(List<Restaurant> likedRestaurant) {
        this.likedRestaurant = likedRestaurant;
    }

    public List<Comment> getComments() {
        return comments;
    }

    public void setComments(List<Comment> comments) {
        this.comments = comments;
    }

    public Date getDateOfBirth() {
        return dateOfBirth;
    }

    public void setDateOfBirth(Date dateOfBirth) {
        this.dateOfBirth = dateOfBirth;
    }
}

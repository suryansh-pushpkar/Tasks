package com.friendbook.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public class ProfileUpdateDTO {

    @NotBlank
    @Size(max = 100)
    private String fullName;

    @NotBlank
    @Email
    @Size(max = 150)
    private String email;

    @Size(max = 255)
    private String password;

    @Size(max = 255)
    private String favSongs;

    @Size(max = 255)
    private String favBooks;

    @Size(max = 255)
    private String favPlaces;

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getFavSongs() {
        return favSongs;
    }

    public void setFavSongs(String favSongs) {
        this.favSongs = favSongs;
    }

    public String getFavBooks() {
        return favBooks;
    }

    public void setFavBooks(String favBooks) {
        this.favBooks = favBooks;
    }

    public String getFavPlaces() {
        return favPlaces;
    }

    public void setFavPlaces(String favPlaces) {
        this.favPlaces = favPlaces;
    }
}

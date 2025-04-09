package br.com.rogerio.Musicplaylist.entity;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public class Album {
    // Class properties
    private List<Artist> artists;
    private String name;
    private String release_date;
    private int total_tracks;

    // Constructors
    public Album(String name) {
        this.name = name;
    }
    public Album(){
    }

    // Getters
    public List<Artist> getArtists() {
        return artists;
    }
    public String getName() {
        return name;
    }
    public String getRelease_date() {
        return release_date;
    }
    public int getTotal_tracks() {
        return total_tracks;
    }
}

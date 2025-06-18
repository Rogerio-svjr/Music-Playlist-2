package br.com.rogerio.Musicplaylist.entity;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public class Artist {
    private String name;
    
    public Artist(String name) {
        this.name = name;
    }
    public Artist(){
    }
    
    public String getName() {
        return name;
    }
}

package br.com.rogerio.Musicplaylist.models;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public class Artist {
    private String name;

    public String getName() {
        return name;
    }
}

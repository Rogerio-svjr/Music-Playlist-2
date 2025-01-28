package br.com.rogerio.Musicplaylist.models;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public class Tracks {
    private List<TrackItem> items;

    public List<TrackItem> getItems() {
        return items;
    }
}

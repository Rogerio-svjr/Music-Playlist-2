package br.com.rogerio.Musicplaylist.models;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public class TrackItem {
	private Album album;
	private List<Artist> artists;
	private int duration_ms;
	private String name;

	public Album getAlbum() {
		return album;
	}

	public List<Artist> getArtists() {
		return artists;
	}

	public int getDuration_ms() {
		return duration_ms;
	}

	public String getName() {
		return name;
	}
}

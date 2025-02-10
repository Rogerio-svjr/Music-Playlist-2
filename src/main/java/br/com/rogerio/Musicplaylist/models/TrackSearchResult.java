package br.com.rogerio.Musicplaylist.models;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public class TrackSearchResult {
	private Tracks tracks;

	public Tracks getTracks() {
		return tracks;
	}
}

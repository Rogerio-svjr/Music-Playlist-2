package br.com.rogerio.Musicplaylist.entity;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonIgnoreProperties(ignoreUnknown = true)
public class TrackSearchResult {
	@JsonProperty("tracks")
	private Playlist playlist;

	public Playlist getPlaylist() {
		return playlist;
	}
}

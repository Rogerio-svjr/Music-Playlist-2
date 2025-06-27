package br.com.rogerio.Musicplaylist.entity;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

import br.com.rogerio.Musicplaylist.dto.ErrorTrackRequest;

@JsonIgnoreProperties(ignoreUnknown = true)
public class TrackSearchResult {
	@JsonProperty("tracks")
	private PlaylistEntity playlist;
	@JsonProperty("error")
	private ErrorTrackRequest error;

	public PlaylistEntity getPlaylist() {
		return playlist;
	}

	public ErrorTrackRequest getError() {
		return error;
	}

	public void setError(ErrorTrackRequest error) {
		this.error = error;
	}
}

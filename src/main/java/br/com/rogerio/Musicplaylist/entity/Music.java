package br.com.rogerio.Musicplaylist.entity;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.Entity;

// @Entity
@JsonIgnoreProperties(ignoreUnknown = true)
public class Music {
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

	public float getDuration_s() {
		return duration_ms / 1000;
	}

	public String getDuration_min() {
		float f_durationMin = this.getDuration_s() / 60;
		return String.format("%d:%02d", (int) f_durationMin, (int) (( f_durationMin - (int) f_durationMin ) * 60) );
	}

	public String getName() {
		return name;
	}
}

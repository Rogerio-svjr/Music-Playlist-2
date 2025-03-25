package br.com.rogerio.Musicplaylist.entity;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

// import jakarta.annotation.Generated;
import jakarta.persistence.*;

@Entity
@JsonIgnoreProperties(ignoreUnknown = true)
public class Music {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@JsonIgnore
	private short id;
	
	@Column(nullable = false)
	private String name;
	
	@Transient
	private List<Artist> artists;
	
	@Column(nullable = false)
	private String artistName;

	@Transient
	private Album album;

	@Column(nullable = false)
	private String AlbumName;
	
	@Column(nullable = false)
	private int duration_ms;

	//Getters and setter
	public short getId() {
		return id;
	}
	
	public String getName() {
		return name;
	}

	public List<Artist> getArtists() {
		return artists;
	}

	public String getArtistName(){
		return this.getArtists().get(0).getName();
	}

	public Album getAlbum() {
		return album;
	}

	public String getAlbumName(){
		return this.getAlbum().getName();
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
}

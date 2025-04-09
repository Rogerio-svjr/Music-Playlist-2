package br.com.rogerio.Musicplaylist.entity;

import java.util.List;
import java.util.stream.Collectors;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import br.com.rogerio.Musicplaylist.dto.MusicDTO;
import jakarta.persistence.*;

@Entity
@JsonIgnoreProperties(ignoreUnknown = true)
public class MusicEntity {
	// Class properties
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@JsonIgnore
	private Long id;
	
	@Column(nullable = false)
	private String name;
	
	@Transient
	private List<Artist> artists;
	
	@Column(nullable = false)
	private String artistsNames;

	@Transient
	private Album album;

	@Column(nullable = false)
	private String AlbumName;
	
	@Column(nullable = false)
	private int duration_ms;

	@ManyToOne
	@JoinColumn(name = "playlist_id")
	private PlaylistEntity playlist;

	//Constructors
	public MusicEntity( MusicDTO music ){
		this.id = music.getId();
    this.name = music.getName();
		this.artists = music.getArtistsList().stream().map(Artist::new).toList();
		this.album = new Album(music.getAlbum());
		this.duration_ms = (int) music.getDuration_s() * 1000;
		// this.playlist = new PlaylistEntity( music.getPlaylist() );
	}
	public MusicEntity() {
	}

	//Getters
	public Long getId() {
		return id;
	}
	public String getName() {
		return name;
	}
	public List<Artist> getArtists() {
		return artists;
	}
	public String getArtistsNames(){
		// return this.getArtists().get(0).getName();
		List<String> artistsList = this.getArtists().stream().map(Artist::getName).toList();
		return artistsList.stream().collect(Collectors.joining(", "));
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
	public PlaylistEntity getPlaylist() {
		return playlist;
	}

	// HashCode and Equals
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((id == null) ? 0 : id.hashCode());
		result = prime * result + ((name == null) ? 0 : name.hashCode());
		result = prime * result + ((artists == null) ? 0 : artists.hashCode());
		return result;
	}
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		MusicEntity other = (MusicEntity) obj;
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		if (name == null) {
			if (other.name != null)
				return false;
		} else if (!name.equals(other.name))
			return false;
		if (artists == null) {
			if (other.artists != null)
				return false;
		} else if (!artists.equals(other.artists))
			return false;
		return true;
	}
}

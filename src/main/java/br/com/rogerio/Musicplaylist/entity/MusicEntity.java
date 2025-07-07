package br.com.rogerio.Musicplaylist.entity;
import br.com.rogerio.Musicplaylist.dto.MusicDTO;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import jakarta.persistence.*;

@Entity
@Table(
	name = "music",
	uniqueConstraints = {
		@UniqueConstraint(columnNames = {"name", "artistsNames"})
	}
)
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

	private String albumName;
	
	@Column(nullable = false)
	private int duration_ms;

	// @ManyToOne
	// @JoinColumn(name = "playlist_id")
	@ManyToMany(mappedBy = "musics") // inverse side
	private List<PlaylistEntity> playlist = new ArrayList<>();

	private boolean liked = false;

	//Constructors
	public MusicEntity() {}
	public MusicEntity( MusicDTO music ) {
		this.id = music.getId();
    this.name = music.getName();
		this.artists = music.getArtistsList().stream().map(Artist::new).toList();
		this.artistsNames = this.getArtistsNames();
		this.album = new Album(music.getAlbum());
		this.albumName = this.getAlbum().getName();
		this.duration_ms = (int) music.getDuration_s() * 1000;
		if ( !music.getPlaylist().isEmpty() ) {
			this.playlist = music.getPlaylist().stream()
				.map(PlaylistEntity::new).toList();
		}
		this.liked = music.getLiked();
	}

	//Getters
	public Long getId() {
		return id;
	}
	public String getName() {
		return name;
	}
	public List<Artist> getArtists() {
		// If the entity came from the DB, the Artist field would be null
		if ( this.artists == null && this.artistsNames != null) {
			this.artists = Arrays.stream(artistsNames.split(","))
				.map(String::trim)
				.map(Artist::new)
				.toList();
		} else if ( this.artists == null && this.artistsNames == null ){
			this.artists = new ArrayList<>();
		}
		return artists;
	}
	public String getArtistsNames() {
		// If the entity came from the API the artistsNames field would be null
		if ( this.artists != null && this.artistsNames == null ) {
			List<String> artistsList = this.getArtists().stream().map(Artist::getName).toList();
			this.artistsNames = artistsList.stream().collect(Collectors.joining(", "));
		}
		return artistsNames;
	}
	public Album getAlbum() {
		// If the entity came from the DB, the album field would be null
		if ( this.album == null && this.albumName != null ) {
			this.album = new Album(albumName);
		}
		return album;
	}
	public String getAlbumName() {
		// If the entity came from API, the albumName field would be null
		if ( this.album != null && this.albumName == null ) {
			this.albumName = this.album.getName();
		}
		return albumName;
	}
	public int getDuration_ms() {
		return duration_ms;
	}
	public List<PlaylistEntity> getPlaylist() {
		return playlist;
	}
	public boolean getLiked() {
		return liked;
	}

	// Setters
	public void setPlaylist(List<PlaylistEntity> playlist) {
		this.playlist = playlist;
	}
	public void setLiked(boolean liked) {
		this.liked = liked;
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

package br.com.rogerio.Musicplaylist.entity;

import java.util.List;

import org.springframework.beans.BeanUtils;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import br.com.rogerio.Musicplaylist.dto.MusicDTO;
// import jakarta.annotation.Generated;
import jakarta.persistence.*;

@Entity
@JsonIgnoreProperties(ignoreUnknown = true)
public class MusicEntity {
	// Class fields
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

	@ManyToOne
	@JoinColumn(name = "playlist_id")
	private PlaylistEntity playlist;

	//Constructors
	MusicEntity( MusicDTO music ){
		BeanUtils.copyProperties(music, this);
	}
	
	MusicEntity() {

	}

	//Getters
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

	public PlaylistEntity getPlaylist() {
		return playlist;
	}

	// HashCode and Equals
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + id;
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
		if (id != other.id)
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

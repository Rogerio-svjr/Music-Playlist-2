package br.com.rogerio.Musicplaylist.dto;
import br.com.rogerio.Musicplaylist.entity.Artist;
import br.com.rogerio.Musicplaylist.entity.MusicEntity;

import java.util.List;
import java.util.stream.Collectors;

public class MusicDTO {
  // Class properties
  private Long id;
	
	private String name;
	
	private List<String> artistsList;
	
	private String album;
  
	private float duration_s;
  
	private PlaylistDTO playlist;

  private boolean liked;

  // Constructors
  public MusicDTO( MusicEntity music ) {
    this.id = music.getId();
    this.name = music.getName();
    this.artistsList = music.getArtists().stream().map(Artist::getName).toList();
    this.album = music.getAlbumName();
    this.duration_s = music.getDuration_ms() / 1000;
    try {
      this.playlist = new PlaylistDTO( music.getPlaylist() );
    } catch( NullPointerException e ) {
    }
    this.liked = music.getLiked();
  }
  public MusicDTO() {
  }

  // Getters
  public Long getId() {
    return id;
  }
  public String getName() {
    return name;
  }
  public List<String> getArtistsList() {
    return artistsList;
  }
  public String getAlbum() {
    return album;
  }
  public float getDuration_s() {
    return duration_s;
  }
  public PlaylistDTO getPlaylist() {
    return playlist;
  }
  public boolean getLiked() {
    return liked;
  }

  //Setters
	public void setPlaylist(PlaylistDTO playlist) {
		this.playlist = playlist;
	}
	public void setLiked(boolean liked) {
		this.liked = liked;
	}

  // Methods
  // Display string of the music artists separated by column
  public String getArtistsNames() {
    return artistsList.stream().collect(Collectors.joining(", "));
  }
  // Display string of the duration in minutes
  public String getDuration_min() {
		float f_durationMin = this.getDuration_s() / 60;
		return String.format("%d:%02d", (int) f_durationMin, (int) (( f_durationMin - (int) f_durationMin ) * 60) );
	}
}

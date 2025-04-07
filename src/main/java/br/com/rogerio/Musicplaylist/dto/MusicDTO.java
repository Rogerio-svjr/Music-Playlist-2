package br.com.rogerio.Musicplaylist.dto;
import br.com.rogerio.Musicplaylist.entity.MusicEntity;

import java.util.List;

import org.springframework.beans.BeanUtils;


public class MusicDTO {
  // Class field
  private short id;
	
	private String name;
	
	private List<String> artists;
	
	private String album;
  
	private int duration_ms;
  
	private PlaylistDTO playlist;

  // Constructors
  public MusicDTO( MusicEntity music ) {
    BeanUtils.copyProperties(music, this);
    return;
  }

  public MusicDTO() {

  }

  // Getters
  public short getId() {
    return id;
  }

  public String getName() {
    return name;
  }

  public List<String> getArtists() {
    return artists;
  }

  public String getAlbum() {
    return album;
  }

  public int getDuration_ms() {
    return duration_ms;
  }

  public PlaylistDTO getPlaylist() {
    return playlist;
  }
}

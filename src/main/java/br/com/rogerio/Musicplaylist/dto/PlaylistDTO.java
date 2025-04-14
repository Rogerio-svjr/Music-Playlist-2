package br.com.rogerio.Musicplaylist.dto;
import br.com.rogerio.Musicplaylist.entity.PlaylistEntity;

import java.util.List;

// import org.springframework.beans.BeanUtils;

public class PlaylistDTO {
  // Class properteies
  private Long id;

  private String name;

  private List<MusicDTO> playlist;

  // Constructors
  public PlaylistDTO( PlaylistEntity playlist ){
    // BeanUtils.copyProperties(playlist, this);
    this.id = playlist.getId();
    this.name = playlist.getName();
    this.playlist = playlist.getPlaylist().stream().map(MusicDTO::new).toList();
  }
  public PlaylistDTO() {
  }

  // Getters
  public Long getId() {
    return id;
  }
  public String getName() {
    return name;
  }
  public List<MusicDTO> getPlaylist() {
    return playlist;
  }

  // Setters
  public void setName(String name) {
    this.name = name;
  }
  public void setPlaylist(List<MusicDTO> music) {
    this.playlist = music;
  }

  // Methods
  public void addMusic( MusicDTO music ) {
    this.playlist.add(music);
  }

  public void listMusics() {
    this.playlist.forEach(music -> System.out.println(
      music.getName() + " - " + music.getArtistsNames() ));
    System.out.println();
  }
}

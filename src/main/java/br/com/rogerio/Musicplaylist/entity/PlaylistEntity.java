package br.com.rogerio.Musicplaylist.entity;
import br.com.rogerio.Musicplaylist.dto.MusicDTO;
import br.com.rogerio.Musicplaylist.dto.PlaylistDTO;

import java.util.List;

// import org.springframework.beans.BeanUtils;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

import jakarta.persistence.*;

@Entity
@JsonIgnoreProperties(ignoreUnknown = true)
public class PlaylistEntity {
  // Class properties
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @Column(nullable = false)
  private String name;

  @JsonProperty("items")
  @OneToMany(mappedBy = "playlist", cascade = CascadeType.ALL)
  private List<MusicEntity> playlist;
  
  // Constructors
  public PlaylistEntity( PlaylistDTO playlist ){
    // BeanUtils.copyProperties(playlist, this);
    this.id = playlist.getId();
    this.name = playlist.getName();
    this.playlist = playlist.getPlaylist().stream().map(MusicEntity::new).toList();
  }
  public PlaylistEntity(){
  }

  // Getters
  public List<MusicEntity> getPlaylist() {
    return playlist;
  }
  public Long getId() {
    return id;
  }
  public String getName() {
    return name;
  }

  // Setters
  public void setName(String name) {
    this.name = name;
  }
  public void setPlaylist(List<MusicEntity> music) {
    this.playlist = music;
  }
}

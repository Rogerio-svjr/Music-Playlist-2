package br.com.rogerio.Musicplaylist.entity;
import br.com.rogerio.Musicplaylist.dto.PlaylistDTO;

import java.util.List;

import org.springframework.beans.BeanUtils;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

import jakarta.persistence.*;

@Entity
@JsonIgnoreProperties(ignoreUnknown = true)
public class PlaylistEntity {
  // Class fields
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @Column(nullable = false)
  private String name;

  @JsonProperty("items")
  @OneToMany(mappedBy = "playlist", cascade = CascadeType.ALL)
  private List<MusicEntity> music;
  
  // Constructors
  public PlaylistEntity( PlaylistDTO playlist ){
    BeanUtils.copyProperties(playlist, this);
  }

  public PlaylistEntity(){

  }

  // Getters
  public List<MusicEntity> getMusic() {
    return music;
  }

  public Long getId() {
    return id;
  }

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }
}

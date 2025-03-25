package br.com.rogerio.Musicplaylist.entity;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

import jakarta.persistence.*;

// @Entity
@JsonIgnoreProperties(ignoreUnknown = true)
public class Playlist {

  // @Id
  // @GeneratedValue(strategy = GenerationType.IDENTITY)
  // private short id;

  // @Column(nullable = false)
  private String name;

  @JsonProperty("items")
  private List<Music> music;

  public List<Music> getMusic() {
    return music;
  }

  public short getId() {
    return id;
  }

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }
}

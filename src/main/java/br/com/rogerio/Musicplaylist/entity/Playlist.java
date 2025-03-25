package br.com.rogerio.Musicplaylist.entity;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonIgnoreProperties(ignoreUnknown = true)
public class Playlist {
  @JsonProperty("items")
  private List<Music> music;

  public List<Music> getMusic() {
    return music;
  }
}

package br.com.rogerio.Musicplaylist.dto;
import br.com.rogerio.Musicplaylist.entity.PlaylistEntity;

import java.util.List;

import org.springframework.beans.BeanUtils;

public class PlaylistDTO {
  // Class field
  private Long id;

  private String name;

  private List<MusicDTO> music;

  // Constructors
  public PlaylistDTO( PlaylistEntity playlist ){
    BeanUtils.copyProperties(playlist, this);
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

  public List<MusicDTO> getMusic() {
    return music;
  }
}

package br.com.rogerio.Musicplaylist.dto;
import br.com.rogerio.Musicplaylist.entity.PlaylistEntity;

import java.util.ArrayList;
import java.util.List;

public class PlaylistDTO {
  // Class properteies
  private Long id;

  private String name;

  private List<MusicDTO> musics = new ArrayList<>();

  // Constructors
  public PlaylistDTO() {}
  public PlaylistDTO( PlaylistEntity playlist ){
    this.id = playlist.getId();
    this.name = playlist.getName();
    if (playlist.getMusics() != null) {
      this.musics = playlist.getMusics().stream()
        .map(MusicDTO::new).toList();
    }
  }

  // Getters
  public Long getId() {
    return id;
  }
  public String getName() {
    return name;
  }
  public List<MusicDTO> getMusics() {
    return musics;
  }

  // Setters
  public void setName(String name) {
    this.name = name;
  }
  public void setMusics(List<MusicDTO> music) {
    this.musics = new ArrayList<>(music);
  }

  // Methods
  public void addMusic( MusicDTO music ) {
    this.musics.add( music );
  }

  public void listMusics() {
    this.musics.forEach(music -> System.out.println(
      music.getName() + " - " + music.getArtistsNames() ));
    System.out.println();
  }
}

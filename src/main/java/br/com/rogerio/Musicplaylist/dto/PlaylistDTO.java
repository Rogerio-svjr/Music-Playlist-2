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
    this.musics = new ArrayList<>(
      playlist.getMusics().stream()
        .map(MusicDTO::new).toList());
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

  // HashCode and Equals
  @Override
  public int hashCode() {
    final int prime = 31;
    int result = 1;
    result = prime * result + ((id == null) ? 0 : id.hashCode());
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
    PlaylistDTO other = (PlaylistDTO) obj;
    if (id == null) {
      if (other.id != null)
        return false;
    } else if (!id.equals(other.id))
      return false;
    return true;
  }
  
  // Methods
  public void addMusic( MusicDTO music ) {
    // Check if music is in the playlist already
    for( MusicDTO musicInPlaylist : this.musics ) {
      if( musicInPlaylist.getId().equals(music.getId()) ) {
        return;
      }
    }
    this.musics.add( music );
    music.addPlaylist(this);
  }

  public void listMusics() {
    this.musics.forEach(music -> System.out.println(
      music.getName() + " - " + music.getArtistsNames() ));
    System.out.println();
  }
}

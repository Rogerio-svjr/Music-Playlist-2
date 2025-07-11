package br.com.rogerio.Musicplaylist.entity;
import br.com.rogerio.Musicplaylist.dto.PlaylistDTO;

import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

import jakarta.persistence.*;

@Entity
@Table(name = "playlist")
@JsonIgnoreProperties(ignoreUnknown = true)
public class PlaylistEntity {
  // Class properties
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @Column(nullable = false)
  private String name;

  @JsonProperty("items")
  // @OneToMany(mappedBy = "playlist", cascade = CascadeType.MERGE)
  @ManyToMany
  @JoinTable(
    name = "playlist_music", // intermediary table
    joinColumns = @JoinColumn(name = "playlist_id"),
    inverseJoinColumns = @JoinColumn(name = "music_id")
  )
  private List<MusicEntity> musics = new ArrayList<>();
  
  // Constructors
  public PlaylistEntity(){}
  public PlaylistEntity( PlaylistDTO playlist ){
    this.id = playlist.getId();
    this.name = playlist.getName();
    this.musics = new ArrayList<>(
      playlist.getMusics().stream()
        .map(MusicEntity::new).toList());
  }

  // Getters
  public List<MusicEntity> getMusics() {
    return musics;
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
  public void setMusics(List<MusicEntity> music) {
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
    PlaylistEntity other = (PlaylistEntity) obj;
    if (id == null) {
      if (other.id != null)
        return false;
    } else if (!id.equals(other.id))
      return false;
    return true;
  }

  // Methods
  public void addMusic(MusicEntity music) {
    this.getMusics().add(music);
  }
}

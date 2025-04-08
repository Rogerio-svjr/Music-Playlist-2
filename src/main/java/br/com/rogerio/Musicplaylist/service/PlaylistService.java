package br.com.rogerio.Musicplaylist.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import br.com.rogerio.Musicplaylist.dto.PlaylistDTO;
import br.com.rogerio.Musicplaylist.entity.PlaylistEntity;
import br.com.rogerio.Musicplaylist.repository.PlaylistRepository;

@Service
public class PlaylistService {
  @Autowired
  private final PlaylistRepository playlistRepository;

  // Constructor
  public PlaylistService( PlaylistRepository playlistRepository ) {
    this.playlistRepository = playlistRepository;
  }

  // Create and Update
  public PlaylistEntity createPLaylist( PlaylistDTO playlist ){
    PlaylistEntity playlistEntity = new PlaylistEntity(playlist);
    return playlistRepository.save(playlistEntity);
  }

  // Read 
  public List<PlaylistDTO> readAllPlaylists() {
    List<PlaylistEntity> playlists = playlistRepository.findAll();
    return playlists.stream().map(PlaylistDTO::new).toList();
  }
  
  public PlaylistDTO readPlaylist( Long id ) {
    return new PlaylistDTO( playlistRepository.findById(id).get() );
  }

  // Delete
  public void deletePlaylist( Long id ) {
    PlaylistEntity playlist = playlistRepository.findById(id).get();
    playlistRepository.delete(playlist);
  }
}

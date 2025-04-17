package br.com.rogerio.Musicplaylist.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import br.com.rogerio.Musicplaylist.dto.PlaylistDTO;
import br.com.rogerio.Musicplaylist.entity.MusicEntity;
import br.com.rogerio.Musicplaylist.entity.PlaylistEntity;
import br.com.rogerio.Musicplaylist.repository.PlaylistRepository;

@Service
public class PlaylistService {
  @Autowired
  private final PlaylistRepository playlistRepository;
  @Autowired
	private MusicService musicService;

  // Constructor
  public PlaylistService( PlaylistRepository playlistRepository ) {
    this.playlistRepository = playlistRepository;
  }

  // Create 
  public PlaylistDTO createPlaylist( PlaylistDTO playlist ){
    PlaylistEntity playlistEntity = new PlaylistEntity(playlist);
    return new PlaylistDTO( playlistRepository.save(playlistEntity) );
  }

  // Update
  @Transactional
  public PlaylistDTO updatePlaylist( PlaylistDTO playlist ){
    PlaylistEntity playlistEntity = new PlaylistEntity(playlist);
    // Make sure that all the playlist's musics are on database
    List<MusicEntity> musics = playlist.getMusics().stream()
      .map( musicDTO -> {
        return new MusicEntity( musicService.createMusic(musicDTO) );
      }).toList();
    playlistEntity.setMusics(musics);
    // Update the database
    return new PlaylistDTO( playlistRepository.save(playlistEntity) );
  }

  // Read 
  @Transactional(readOnly = true)
  public List<PlaylistDTO> readAllPlaylists() {
    List<PlaylistEntity> playlists = playlistRepository.findAll();
    return playlists.stream().map(PlaylistDTO::new).toList();
  }
  
  @Transactional(readOnly = true)
  public PlaylistDTO readPlaylistById( Long id ) {
    return new PlaylistDTO( playlistRepository.findById(id).get() );
  }

  // Delete
  public void deletePlaylist( Long id ) {
    PlaylistEntity playlist = playlistRepository.findById(id).get();
    playlistRepository.delete(playlist);
  }
}

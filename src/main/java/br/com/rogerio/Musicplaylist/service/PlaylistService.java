package br.com.rogerio.Musicplaylist.service;

import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import br.com.rogerio.Musicplaylist.dto.MusicDTO;
import br.com.rogerio.Musicplaylist.dto.PlaylistDTO;
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
  public PlaylistDTO createPlaylist( PlaylistDTO playlist ) throws Exception {
    if( playlist.getId() != null ) {
      throw new Exception("Provided playlist with id");
    }
    // Check if playlist already have musics in it
    if( !playlist.getMusics().isEmpty() ) {
      throw new Exception("Playlist already exists");
    }
    // Create playlist and returns it
    PlaylistEntity playlistEntity = new PlaylistEntity(playlist);
    return new PlaylistDTO( playlistRepository.save(playlistEntity) );
  }

  // Update
  @Transactional
  public PlaylistDTO updatePlaylist( PlaylistDTO playlist ) {
    // Check if playlist already have musics in it
    if( !playlist.getMusics().isEmpty() ) {
      // Create auxiliar music list to set playlist's "musics" field
      List<MusicDTO> auxMusicList = new ArrayList<>();
      // If it has, update them as well
      for( MusicDTO music : playlist.getMusics() ) {
        // music.addPlaylist(playlist);
        try {
          auxMusicList.add( musicService.updateMusic( music ) );
        } catch( Exception e ) {}
      }
      // Set musics field
      playlist.setMusics(auxMusicList);
    }
    PlaylistEntity playlistEntity = new PlaylistEntity(playlist);
    return new PlaylistDTO( playlistRepository.save(playlistEntity) );
  }

  // Read 
  @Transactional(readOnly = true)
  public List<PlaylistDTO> readAllPlaylists() throws Exception {
    List<PlaylistEntity> playlists = playlistRepository.findAll();
    // Verify if the database is empty
    if( playlists.isEmpty() ) {
      throw new Exception("No playlists in database");
    }
    // Returns a list of all present playlists
    return playlists.stream().map(PlaylistDTO::new).toList();
  }
  
  @Transactional(readOnly = true)
  public PlaylistDTO readPlaylistById( Long id ) throws NoSuchElementException {
    return new PlaylistDTO( playlistRepository.findById(id).get() );
  }

  // Delete
  public void deletePlaylist( Long id ) {
    PlaylistEntity playlist = playlistRepository.findById(id).get();
    playlistRepository.delete(playlist);
  }
}

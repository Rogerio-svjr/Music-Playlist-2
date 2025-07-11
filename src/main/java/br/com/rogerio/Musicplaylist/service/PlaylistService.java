package br.com.rogerio.Musicplaylist.service;

import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import br.com.rogerio.Musicplaylist.dto.MusicDTO;
import br.com.rogerio.Musicplaylist.dto.PlaylistDTO;
import br.com.rogerio.Musicplaylist.entity.MusicEntity;
import br.com.rogerio.Musicplaylist.entity.PlaylistEntity;
import br.com.rogerio.Musicplaylist.repository.MusicRepository;
import br.com.rogerio.Musicplaylist.repository.PlaylistRepository;

@Service
public class PlaylistService {
  @Autowired
  private final PlaylistRepository playlistRepository;
  @Autowired
  private final MusicRepository musicRepository;
  @Autowired
	private MusicService musicService;

  // Constructor
  public PlaylistService( PlaylistRepository playlistRepository, MusicRepository musicRepository) {
    this.playlistRepository = playlistRepository;
    this.musicRepository = musicRepository;
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
  public PlaylistDTO updatePlaylist( PlaylistDTO playlist ) throws Exception {
    // Verify if provided playlist has an id
    if( playlist.getId() == null ) {
      throw new Exception("Provided playlist without id");
    }
    // Verify if provided playlist is in the database
    Optional<PlaylistEntity> optPlaylist = playlistRepository.findById(playlist.getId());
    if( !optPlaylist.isPresent() ) {
      throw new Exception("Provided playlist not found");
    }

    // Update Playlist first
    PlaylistEntity playlistEntity = new PlaylistEntity( playlist );
    PlaylistDTO resultPlaylistDto = new PlaylistDTO( playlistRepository.save( playlistEntity ) );

    // Create auxiliar music list to set playlist's "musics" field
    List<MusicDTO> auxMusicList = new ArrayList<>();

    // Update the musics too
    for( MusicDTO musicDto : playlist.getMusics() ) {
      // Add the playlist if it's not in the music yet
      if( !musicDto.getPlaylist().contains(playlist) ) {
        musicDto.addPlaylist(playlist);
      } 
      // Create a new music if it wasn't in the database before, or update it if it was
      if( musicDto.getId() == null ) {
        System.out.println("DEBUG: Should NOT pass through here");
        auxMusicList.add( this.musicService.createMusic(musicDto) );
      } else {
        System.out.println("DEBUG: Should pass through here");
        auxMusicList.add( this.musicService.updateMusic(musicDto) );
      }
    }

    // Set musics field
    resultPlaylistDto.setMusics(auxMusicList);
    return resultPlaylistDto;
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
  public void deletePlaylist( Long id ) throws Exception {
    Optional<PlaylistEntity> optPlaylist = playlistRepository.findById(id);
    if( !optPlaylist.isPresent() ) {
      throw new Exception("No playlist with provided id found");
    }
    PlaylistEntity playlist = optPlaylist.get();

    // Delete the playlist from musics
    for( MusicEntity musicEntity : playlist.getMusics() ) {
      musicEntity.getPlaylist().remove(playlist);
      musicRepository.save(musicEntity);
    }
    playlistRepository.delete(playlist);
  }
}

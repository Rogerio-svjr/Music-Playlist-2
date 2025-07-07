package br.com.rogerio.Musicplaylist.service;

import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.ExampleMatcher;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import br.com.rogerio.Musicplaylist.dto.MusicDTO;
import br.com.rogerio.Musicplaylist.dto.PlaylistDTO;
import br.com.rogerio.Musicplaylist.entity.MusicEntity;
import br.com.rogerio.Musicplaylist.entity.PlaylistEntity;
import br.com.rogerio.Musicplaylist.repository.MusicRepository;
import br.com.rogerio.Musicplaylist.repository.PlaylistRepository;

@Service
public class MusicService {
  @Autowired
  private final MusicRepository musicRepository;
  @Autowired
  private final PlaylistRepository playlistRepository;

  // Constructor
  public MusicService(MusicRepository musicRepository, PlaylistRepository playlistRepository) {
    this.musicRepository = musicRepository;
    this.playlistRepository = playlistRepository;
  }

  // Create
  @Transactional
  public MusicDTO createMusic( MusicDTO music ) {
    MusicEntity musicEntity = new MusicEntity(music);
    System.out.println("DEBUG: Inside createMusic");
    // Check if music is already on the database
    // Use an example matcher to ignore "Liked" and "playlist" fields
    ExampleMatcher matcher = ExampleMatcher.matching()
    .withIgnorePaths("liked", "playlist");
    Example<MusicEntity> example = Example.of(musicEntity, matcher);
    Optional<MusicEntity> result = musicRepository.findOne(example);
    // If it is, returns it. If not, creates it.
    MusicEntity returnMusicEntity = new MusicEntity();
    if( result.isPresent() ) {
      returnMusicEntity = result.get();
    } else {
      returnMusicEntity = musicRepository.save(musicEntity);
    }
    MusicDTO returnMusic = new MusicDTO(returnMusicEntity);
    // Set "playlist" field since the MusicDTO contructor doesn't handle it
    returnMusic.setPlaylist(returnMusicEntity.getPlaylist()
      .stream().map(PlaylistDTO::new).toList());
    return returnMusic;
  }

  // Update
  @Transactional
  public MusicDTO updateMusic( MusicDTO music ) throws Exception {
    // Verify if the provided music has an id
    if( music.getId() == null ) {
      throw new Exception("Provided music without id");
    }
    // Verify if the provided music is on database
    Optional<MusicEntity> opt = musicRepository.findById(music.getId());
    if ( !opt.isPresent() ) {
      throw new Exception("Provided music not found");
    }
    MusicEntity musicEntity = new MusicEntity( music );
    // Set the "playlist" field since the MusicEntity constructor doesn't handle it
    musicEntity.setPlaylist(music.getPlaylist()
      .stream().map(PlaylistEntity::new).toList());
    // Update music on database
    MusicEntity resultMusicEntity = musicRepository.save( musicEntity ); 
    // Run through all the results' playlists to update them as well
    List<PlaylistEntity> resultPlaylistList = new ArrayList<>();
    for( PlaylistEntity playlist : resultMusicEntity.getPlaylist() ) {
      // Add the music in the playlist if it's not in it yet
      if( !playlist.getMusics().contains(resultMusicEntity) ) {
        playlist.getMusics().add( resultMusicEntity );
      }
      // Add each playlist in an auxiliar playlist list after updating them
      resultPlaylistList.add( playlistRepository.save(playlist) );
    }
    // Set the "playlist" field since the MusicDTO constructor doesn't handle it
    MusicDTO returnMusicDto = new MusicDTO( resultMusicEntity );
    returnMusicDto.setPlaylist(resultPlaylistList
      .stream().map(PlaylistDTO::new).toList());
    // Return updated music
    return returnMusicDto;
  }

  // Read 
  @Transactional
  public List<MusicDTO> readAllMusic() throws Exception {
    List<MusicEntity> musics = musicRepository.findAll();
    // Verify if the database is empty
    if( musics.isEmpty() ) {
      throw new Exception("No musics in database");
    }
    // Returns a list of all present musics
    return musics.stream().map(MusicDTO::new).toList();
  }
  
  @Transactional
  public MusicDTO readMusicById( Long id ) throws NoSuchElementException {
    // Read music by id
    MusicEntity music = musicRepository.findById(id).get();
    // Retrieve all playlists on database
    List<PlaylistEntity> playlists = playlistRepository.findAll();
    List<PlaylistEntity> auxPlaylistList = new ArrayList<>();
    // run through all playlists
    playlists.forEach( playlist -> {
      // If the playlist have the requested music, add it to the auxiliar playlist list
      if ( playlist.getMusics().contains(music) ) {
        auxPlaylistList.add(playlist);
      }
    });
    // Set the "playlist" field since the MusicDTO contructor doesn't handle it
    MusicDTO musicDto = new MusicDTO( music );
    musicDto.setPlaylist(auxPlaylistList.stream()
      .map(PlaylistDTO::new).toList());
    // Returns requested music
    return musicDto;
  }

  // Delete
  @Transactional
  public void deleteMusic( Long id ) throws Exception {
    // Check if music really exists and throws exception if not
    Optional<MusicEntity> musicOpt = musicRepository.findById(id);
    if ( !musicOpt.isPresent() ) {
      throw new Exception("No music with provided id found");
    } 
    MusicEntity music = musicOpt.get();
    // Deletes it from all playlists that contains it
    List<PlaylistEntity> playlists = playlistRepository.findAll();
    playlists.forEach( playlist -> {
      if ( playlist.getMusics().contains(music) ) {
        playlist.getMusics().remove(music);
        playlistRepository.save(playlist);
      }
    });
    // Delete music
    musicRepository.delete(music);
  }
}

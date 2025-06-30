package br.com.rogerio.Musicplaylist.service;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.ExampleMatcher;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import br.com.rogerio.Musicplaylist.dto.MusicDTO;
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
    // Check if music is already on the database
    MusicEntity musicEntity = new MusicEntity(music);
    ExampleMatcher matcher = ExampleMatcher.matching()
    .withIgnorePaths("liked", "playlist");
    Example<MusicEntity> example = Example.of(musicEntity, matcher);
    Optional<MusicEntity> result = musicRepository.findOne(example);
    // If it is, returns it. If not, creates it.
    if( result.isPresent() ) {
      System.out.println("\nMUSIC ALREADY IN DATABASE!\n");
      return new MusicDTO( result.get() );
    } else {
      System.out.println("\nmusic put into databse: " + music.getName() + "\n");
      return new MusicDTO( musicRepository.save(musicEntity) );
    }
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
    MusicEntity musicEntity = new MusicEntity(music);
    // Update music on database and returns it
    return new MusicDTO( musicRepository.save(musicEntity) );
  }

  // Read 
  @Transactional
  public List<MusicDTO> readAllMusic() throws Exception {
    List<MusicEntity> musics = musicRepository.findAll();
    // Verify if the database is empty
    if( musics.isEmpty() ) {
      throw new Exception("No music in database");
    }
    // Returns a list of all present musics
    return musics.stream().map(MusicDTO::new).toList();
  }
  
  @Transactional
  public MusicDTO readMusicById( Long id ) throws NoSuchElementException {
    return new MusicDTO( musicRepository.findById(id).get() );
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

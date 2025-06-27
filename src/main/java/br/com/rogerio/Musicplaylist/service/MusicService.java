package br.com.rogerio.Musicplaylist.service;

import java.util.List;
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
    System.out.println("entity id: " + musicEntity.getId());
    ExampleMatcher matcher = ExampleMatcher.matching()
    .withIgnorePaths("liked", "playlist", "playlists");
    // System.out.println("Before matcher...");
    Example<MusicEntity> example = Example.of(musicEntity, matcher);
    // System.out.println("After matcher. Before findOne...");
    Optional<MusicEntity> result = musicRepository.findOne(example);
    // System.out.println("After findOne...");
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
  public MusicDTO updateMusic( MusicDTO music ) {
    MusicEntity musicEntity = new MusicEntity(music);
    return new MusicDTO( musicRepository.save(musicEntity) );
  }

  // Read 
  @Transactional
  public List<MusicDTO> readAllMusic() {
    List<MusicEntity> musics = musicRepository.findAll();
    return musics.stream().map(MusicDTO::new).toList();
  }
  
  @Transactional
  public MusicDTO readMusicById( Long id ) {
    return new MusicDTO( musicRepository.findById(id).get() );
  }

  // Delete
  @Transactional
  public void deleteMusic( Long id ) {
    // Check if music really exists
    Optional<MusicEntity> musicOpt = musicRepository.findById(id);
    if ( musicOpt.isPresent() ) {
      MusicEntity music = musicOpt.get();
      // Deletes it from all playlists that contains it
      List<PlaylistEntity> playlists = playlistRepository.findAll();
      playlists.forEach( playlist -> {
        if ( playlist.getMusics().contains(music) ) {
          playlist.getMusics().remove(music);
          playlistRepository.save(playlist);
        }
      });
      musicRepository.delete(music);
    }
  }
}

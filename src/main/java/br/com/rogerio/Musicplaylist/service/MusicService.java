package br.com.rogerio.Musicplaylist.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.Example;
import org.springframework.stereotype.Service;

import br.com.rogerio.Musicplaylist.dto.MusicDTO;
import br.com.rogerio.Musicplaylist.entity.MusicEntity;
import br.com.rogerio.Musicplaylist.repository.MusicRepository;

@Service
public class MusicService {
  @Autowired
  private final MusicRepository musicRepository;

  // Constructor
  public MusicService( MusicRepository musicRepository ) {
    this.musicRepository = musicRepository;
  }

  // Create
  public MusicDTO createMusic( MusicDTO music ){
    MusicEntity musicEntity = new MusicEntity(music);
    try {
      return new MusicDTO( musicRepository.save(musicEntity) );
      // musicRepository.save(musicEntity);
    } catch ( DataIntegrityViolationException e ) {
      if( e.getCause().toString().contains("Duplicate") ){
        System.out.println("\nMUSIC ALREADY IN DATABASE!\n");

        // Tries to find the duplicate in the database
        Example<MusicEntity> example = Example.of(musicEntity);
        Optional<MusicEntity> exists = musicRepository.findOne(example);

        // If it finds, returns it. If not, returns null.
        if( exists.isPresent() ) {
          return new MusicDTO( exists.get() );
        } else {
          System.err.println("Duplicate detected but no matching entity found.");
          return null;
        }
      }
    }
    return null;
  }

  // Read 
  public List<MusicDTO> readAllMusic() {
    List<MusicEntity> musics = musicRepository.findAll();
    return musics.stream().map(MusicDTO::new).toList();
  }
  
  public MusicDTO readMusicById( Long id ) {
    return new MusicDTO( musicRepository.findById(id).get() );
  }

  // Delete
  public void deleteMusic( Long id ) {
    MusicEntity music = musicRepository.findById(id).get();
    musicRepository.delete(music);
  }
}

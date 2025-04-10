package br.com.rogerio.Musicplaylist.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
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

  // Create and Update
  public MusicEntity createMusic( MusicDTO music ){
    MusicEntity musicEntity = new MusicEntity(music);
    return musicRepository.save(musicEntity);
  }

  // Read 
  public List<MusicDTO> readAllMusic() {
    List<MusicEntity> musics = musicRepository.findAll();
    return musics.stream().map(MusicDTO::new).toList();
  }
  
  public MusicDTO readMusic( Long id ) {
    return new MusicDTO( musicRepository.findById(id).get() );
  }

  // Delete
  public void deleteMusic( Long id ) {
    MusicEntity music = musicRepository.findById(id).get();
    musicRepository.delete(music);
  }
}

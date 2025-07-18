package br.com.rogerio.Musicplaylist.service;

import org.springframework.boot.SpringBootConfiguration;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;

import br.com.rogerio.Musicplaylist.repository.MusicRepository;
import br.com.rogerio.Musicplaylist.repository.PlaylistRepository;

@SpringBootConfiguration
@EnableAutoConfiguration
@TestConfiguration
public class ServiceTestConfig {

  @Bean
  public MusicService musicService(MusicRepository musicRepository, PlaylistRepository playlistRepository) {
    return new MusicService(musicRepository, playlistRepository);
  }

  @Bean
  public PlaylistService playlistService(PlaylistRepository playlistRepository, MusicRepository musicRepository) {
    return new PlaylistService(playlistRepository, musicRepository);
  }

}

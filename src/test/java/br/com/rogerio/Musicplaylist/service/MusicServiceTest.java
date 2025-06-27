package br.com.rogerio.Musicplaylist.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.test.context.ActiveProfiles;

import br.com.rogerio.Musicplaylist.dto.MusicDTO;
import jakarta.persistence.EntityManager;

@DataJpaTest
@ActiveProfiles("test")
@EnableJpaRepositories(basePackages = "br.com.rogerio.Musicplaylist.repository")
@EntityScan(basePackages = "br.com.rogerio.Musicplaylist.entity")
public class MusicServiceTest {

  @Autowired
  private MusicService musicService;

  private MusicDTO musicDto;

  // private PlaylistDTO playlistDto;

  @BeforeEach
  void setup() {
    // Create DTO based on JSON string
    String musicDTOJson = "{\"name\":\"Test Music\",\"artistsList\":[\"Artist 1\",\"Artist 2\"],\"album\":\"Test Album\",\"duration_s\":180.0,\"playlist\":[],\"liked\":true}";
    // String playlistDTOJson = "{\"id\":1,\"name\":\"Test Playlist\",\"musics\":[]}";
    ConvertsData deserialize = new ConvertsData();
    musicDto = deserialize.getData(musicDTOJson, MusicDTO.class);
    // playlistDto = deserialize.getData(playlistDTOJson, PlaylistDTO.class);
  }

  @Test
  public void testCreateMusic_SaveMusicInDatabase_ShouldCreateANewMusicInDatabase() {
    MusicDTO resultMusic = this.musicService.createMusic(musicDto);
    
    assertEquals(1, resultMusic.getId());
    assertEquals(musicDto.getName(), resultMusic.getName());
    assertEquals(musicDto.getArtistsNames(), resultMusic.getArtistsNames());
    assertEquals(musicDto.getAlbum(), resultMusic.getAlbum());
    assertEquals(musicDto.getDuration_s(), resultMusic.getDuration_s());
    assertEquals(musicDto.getLiked(), resultMusic.getLiked());
  }

  @Test
  public void testCreateMusic_MusicAlreadyInDatabase_ShouldNotCreateANewMusic() {
    MusicDTO firstMusic = this.musicService.createMusic(musicDto);
    MusicDTO secondMusic = this.musicService.createMusic(musicDto);

    assertEquals(firstMusic.getId(), secondMusic.getId());
    assertEquals(firstMusic.getName(), secondMusic.getName());
    assertEquals(firstMusic.getArtistsNames(), secondMusic.getArtistsNames());
    assertEquals(firstMusic.getAlbum(), secondMusic.getAlbum());
    assertEquals(firstMusic.getDuration_s(), secondMusic.getDuration_s());
    assertEquals(firstMusic.getLiked(), secondMusic.getLiked());
  }

  
}

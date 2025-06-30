package br.com.rogerio.Musicplaylist.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.assertj.core.api.Assertions.assertThat;

import java.util.List;
import java.util.NoSuchElementException;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ActiveProfiles;

import br.com.rogerio.Musicplaylist.dto.MusicDTO;
import br.com.rogerio.Musicplaylist.entity.MusicEntity;
import jakarta.persistence.EntityManager;

@DataJpaTest
@ActiveProfiles("test")
@EnableJpaRepositories(basePackages = "br.com.rogerio.Musicplaylist.repository")
@EntityScan(basePackages = "br.com.rogerio.Musicplaylist.entity")
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
public class MusicServiceTest {

  @Autowired
  private MusicService musicService;

  @Autowired
  private EntityManager entityManager;

  @Test
  public void testReadAllMusic_ShouldReceiveListOfAllMusics() throws Exception {
    // Insert musics in database
    MusicEntity musicEntity = new MusicEntity(this.createMusicWithoutId());
    this.entityManager.persist(musicEntity);
    musicEntity = new MusicEntity(this.createMusicWithoutId2());
    this.entityManager.persist(musicEntity);
    // Test readAllMusic()
    List<MusicDTO> result = this.musicService.readAllMusic();
    // Verify
    assertNotNull(result.get(0));
    assertNotNull(result.get(1));
    assertEquals(1, result.get(0).getId());
    assertEquals(2, result.get(1).getId());
    assertEquals("Test Music", result.get(0).getName());
    assertEquals("Test Music 2", result.get(1).getName());
  }

  @Test
  public void testReadAllMusic_NoMusicsInDatabase_ShouldThrowException() {
    // Test readAllMusic() without inserting any music in database
    Exception thrown = assertThrows(Exception.class, () -> {
      List<MusicDTO> result = this.musicService.readAllMusic();
      // Verify
      assertThat(result.isEmpty()).isTrue();
    });
    assertEquals("No music in database", thrown.getMessage());
  }

  @Test
  public void testReadMusicById_ShouldRetriveSpecifiedMusic() throws Exception{
    // Insert musics in database
    MusicEntity musicEntity = new MusicEntity(this.createMusicWithoutId());
    this.entityManager.persist(musicEntity);
    // Test readMusicById()
    MusicDTO result = this.musicService.readMusicById(1L);
    // Verify
    assertNotNull(result.getId());
    assertEquals(1L, result.getId());
    assertEquals("Test Music", result.getName());
  }

  @Test
  public void testReadMusicById_MusicInAPlaylist_ShouldReadPlaylistAsWell() {

  }

  @Test
  public void testReadMusicById_RequestedMusicNotInDatabase_ShouldThrowException() {
    // Test readMusicById() without inserting any music in database
    assertThrows(NoSuchElementException.class, () -> this.musicService.readMusicById(1L));
  }

  @Test
  public void testCreateMusic_ShouldCreateANewMusicInDatabase() {
    // Create musicDto
    MusicDTO musicDto = this.createMusicWithoutId();
    // Test creatMusic()
    MusicDTO resultMusic = this.musicService.createMusic(musicDto);
    // Verify
    assertNotNull(resultMusic.getId());
    assertEquals(musicDto.getName(), resultMusic.getName());
    assertEquals(musicDto.getArtistsNames(), resultMusic.getArtistsNames());
    assertEquals(musicDto.getAlbum(), resultMusic.getAlbum());
    assertEquals(musicDto.getDuration_s(), resultMusic.getDuration_s());
    assertEquals(musicDto.getLiked(), resultMusic.getLiked());
  }

  @Test
  public void testCreateMusic_MusicAlreadyInDatabase_ShouldNotCreateANewMusicAndReturnTheMusic() {
    // Create musicDto
    MusicDTO musicDto = this.createMusicWithoutId();
    // Test creatMusic() twice
    MusicDTO firstMusic = this.musicService.createMusic(musicDto);
    MusicDTO secondMusic = this.musicService.createMusic(musicDto);
    // Verify if it only save it once
    assertEquals(firstMusic.getId(), secondMusic.getId());
    assertEquals(firstMusic.getName(), secondMusic.getName());
    assertEquals(firstMusic.getArtistsNames(), secondMusic.getArtistsNames());
    assertEquals(firstMusic.getAlbum(), secondMusic.getAlbum());
    assertEquals(firstMusic.getDuration_s(), secondMusic.getDuration_s());
    assertEquals(firstMusic.getLiked(), secondMusic.getLiked());
  }

  @Test
  public void testUpdateMusic_ShouldChangeMusicDataInDatabase() throws Exception {
    // Insert music in database with liked true
    MusicEntity musicEntity = new MusicEntity(this.createMusicWithoutId());
    musicEntity.setLiked(true);
    this.entityManager.persist(musicEntity);
    this.entityManager.clear();
    // Create duplicated music with id and change liked value
    MusicDTO duplicate = this.createMusicWithId();
    duplicate.setLiked(false);
    // Test updateMusic()
    MusicDTO result = this.musicService.updateMusic(duplicate);
    // Verify
    assertEquals(duplicate.getId(), result.getId());
    assertFalse(result.getLiked());
    assertNotEquals(musicEntity.getLiked(), result.getLiked());
  }
  
  @Test
  public void testUpdateMusic_MusicIncludedInPlaylists_ShouldUpdatePlaylistsAsWell() {
    
  }

  @Test
  public void testUpdateMusic_ProvidedMusicWithoutId_ShouldThrowException() {
    // Create music without id
    MusicDTO musicDTO = this.createMusicWithoutId();
    // Test updateMusic()
    Exception thrown = assertThrows(Exception.class, () -> {
      MusicDTO result = this.musicService.updateMusic(musicDTO);
      // Verify
      assertNull(result.getId());
    });
    assertEquals("Provided music without id", thrown.getMessage());
  }

  @Test
  public void testUpdateMusic_ProvidedMusicNotFound_ShouldThrowException() {
    // Create music with id but don't insert it into database
    MusicDTO musicDTO = this.createMusicWithId();
    // Test updateMusic()
    Exception thrown = assertThrows(Exception.class, () -> {
      MusicDTO result = this.musicService.updateMusic(musicDTO);
      // Verify
      assertNull(result.getId());
    });
    assertEquals("Provided music not found", thrown.getMessage());
  }

  @Test
  public void testDeleteMusic_ShouldDeleteMusicFromDatabase() throws Exception{
    // Insert musics in database
    MusicEntity musicEntity = new MusicEntity(this.createMusicWithoutId());
    this.entityManager.persist(musicEntity);
    // Make sure it is in the database
    assertTrue(this.entityManager.contains(musicEntity));
    // Test deleteMusic()
    this.musicService.deleteMusic(1L);
    // Verify
    assertFalse(this.entityManager.contains(musicEntity));
  }

  @Test
  public void testDeleteMusic_MusicNotFound_ShouldThrowException() {
    // Try to delete a music from database without inserting it first
    Exception thrown = assertThrows(Exception.class, () -> {
      this.musicService.deleteMusic(1L);
    });
    assertEquals("No music with provided id found", thrown.getMessage());
  }

  @Test
  public void testDeleteMusic_MusicInPlaylists_ShouldDeleteFromPlaylistsAsWell() {

  }

  private MusicDTO createMusicWithoutId() {
    // Create DTO based on JSON string
    String musicDTOJson = "{\"name\":\"Test Music\",\"artistsList\":[\"Artist 1\",\"Artist 2\"],\"album\":\"Test Album\",\"duration_s\":180.0,\"playlist\":[],\"liked\":true}";
    ConvertsData deserialize = new ConvertsData();
    return deserialize.getData(musicDTOJson, MusicDTO.class);
  }
  
  private MusicDTO createMusicWithoutId2() {
    // Create DTO based on JSON string
    String musicDTOJson = "{\"name\":\"Test Music 2\",\"artistsList\":[\"Artist 1\",\"Artist 2\"],\"album\":\"Test Album 2\",\"duration_s\":210.0,\"playlist\":[],\"liked\":false}";
    ConvertsData deserialize = new ConvertsData();
    return deserialize.getData(musicDTOJson, MusicDTO.class);
  }
  
  private MusicDTO createMusicWithId() {
    // Create DTO identical with the first method but with id based on JSON string
    String musicDTOJson = "{\"id\":\"1\",\"name\":\"Test Music\",\"artistsList\":[\"Artist 1\",\"Artist 2\"],\"album\":\"Test Album\",\"duration_s\":180.0,\"playlist\":[],\"liked\":true}";
    ConvertsData deserialize = new ConvertsData();
    return deserialize.getData(musicDTOJson, MusicDTO.class);
  }
}

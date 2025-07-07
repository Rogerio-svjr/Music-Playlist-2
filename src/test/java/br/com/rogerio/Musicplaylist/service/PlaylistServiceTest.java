package br.com.rogerio.Musicplaylist.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.List;
import java.util.NoSuchElementException;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ActiveProfiles;

import br.com.rogerio.Musicplaylist.dto.PlaylistDTO;
import br.com.rogerio.Musicplaylist.entity.PlaylistEntity;
import jakarta.persistence.EntityManager;

@DataJpaTest
@ActiveProfiles("test")
@EnableJpaRepositories(basePackages = "br.com.rogerio.Musicplaylist.repository")
@EntityScan(basePackages = "br.com.rogerio.Musicplaylist.entity")
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
public class PlaylistServiceTest {
  
  @Autowired
  PlaylistService playlistService;

  @Autowired
  EntityManager entityManager;

  @Test
  public void testReadAllPlaylists_ShouldReturnListWithAllPlaylists() throws Exception {
    // Create playlists
    PlaylistEntity playlistEntity = new PlaylistEntity( this.createPlaylistWithMusics() );
    PlaylistEntity playlistEntity2 = new PlaylistEntity( this.createPlaylistWithMusics() );
    // Insert in database
    this.entityManager.persist(playlistEntity);
    this.entityManager.persist(playlistEntity2);
    // test readAllPlaylists
    List<PlaylistDTO> result = playlistService.readAllPlaylists();
    // Verify
    assertEquals(2, result.size());
    assertEquals(1L, result.get(0).getId());
    assertEquals(2L, result.get(1).getId());
    assertEquals("Test Playlist", result.get(0).getName());
    assertEquals("Test Playlist", result.get(1).getName());
    assertEquals("Test Music", result.get(0).getMusics().get(0).getName());
    assertEquals("Test Music", result.get(1).getMusics().get(0).getName());
  }

  @Test
  public void testReadAllPlaylists_NoPlaylistsInDatabase_ShouldThrowException() {
    // Test readAllPlaylists() without inserting any playlist in database
    Exception thrown = assertThrows(Exception.class, () -> {
      List<PlaylistDTO> result = this.playlistService.readAllPlaylists();
      // Verify
      assertTrue(result.isEmpty());
    });
    assertEquals("No playlists in database", thrown.getMessage());
  }
  
  @Test
  public void testReadPlaylistById_ShouldReturnRequestedPlaylist() {
    // Create playlist and insert in database 
    PlaylistEntity playlistEntity = new PlaylistEntity( this.createPlaylistWithMusics() );
    this.entityManager.persist(playlistEntity);
    // test readPlaylistById
    PlaylistDTO result = playlistService.readPlaylistById(1L);
    // Verify
    assertEquals(1L, result.getId());
    assertEquals("Test Playlist", result.getName());
    assertEquals("Test Music", result.getMusics().get(0).getName());
  }

  @Test
  public void testReadPlaylistById_PlaylistNotFound_ShouldThrowException() {
    // Test readPlaylistById() without inserting any playlist in database
    assertThrows(NoSuchElementException.class, () -> this.playlistService.readPlaylistById(1L));
  }

  @Test
  public void testCreatePlaylist_ShouldCreateNewPlaylist() throws Exception{
    // Test createPlaylist
    PlaylistDTO result = this.playlistService.createPlaylist(this.createPlaylistWithoutId());
    // Verify
    assertEquals(1L, result.getId());
    assertEquals("Test Playlist", result.getName());
  }

  @Test void testCreatePlaylist_PlaylistAlreadyWithMusics_ShouldThrowException() {
    // Test readAllPlaylists()
    Exception thrown = assertThrows(Exception.class, () -> {
      PlaylistDTO result = this.playlistService.createPlaylist(this.createPlaylistWithMusics());
      // Verify
      assertNull(result.getId());
    });
    assertEquals("Playlist already exists", thrown.getMessage());
  }

  @Test
  public void testCreatePlaylist_PlaylistWithIdProvided_ShouldThrowException() {
    // Test readAllPlaylists()
    Exception thrown = assertThrows(Exception.class, () -> {
      PlaylistDTO result = this.playlistService.createPlaylist(this.createPlaylistWithId());
      // Verify
      assertNull(result.getId());
    });
    assertEquals("Provided playlist with id", thrown.getMessage());
  }

  @Test
  public void testUpdatePlaylist_ShouldUpdatePlaylistAndMusicsPlaylistField() {
    // Create playlist and insert in database 
    PlaylistEntity playlistEntity = new PlaylistEntity( this.createPlaylistWithoutId() );
    this.entityManager.persist(playlistEntity);
    this.entityManager.clear();
     // Assert the previous value of each field
    assertEquals(1L, playlistEntity.getId());
    assertEquals("Test Playlist", playlistEntity.getName());
    assertTrue(playlistEntity.getMusics().isEmpty());
    // Changes playlist's fields
    PlaylistDTO playlistDto = this.createPlaylistWithId();
    playlistDto.setName("Updated Name");
    // Test updatePlaylist()
    PlaylistDTO result = this.playlistService.updatePlaylist(playlistDto);
    // Verify
    assertEquals(1L, result.getId());
    assertEquals("Updated Name", result.getName());
    assertFalse(result.getMusics().isEmpty());
    assertEquals(1L, result.getMusics().get(0).getId()); // updatePlaylist Should Create music as well
    assertEquals("Test Music", result.getMusics().get(0).getName());
    assertEquals("Updated Name", result.getMusics().get(0).getPlaylist().get(0).getName());
  }

  @Test 
  public void testUpdatePlaylist_PlaylistWithoutIdProvided_ShouldThrowException() {

  }

  @Test
  public void testUpdatePlaylist_PlaylistNotFound_ShouldThrowException() {

  }

  @Test
  public void testDeletePlaylist_ShouldDeletePlaylistAndMusicsPlaylistField() {

  }

  @Test
  public void testDeletePlaylist_PlaylistNotFound_ShouldThrowException() {

  }

  private PlaylistDTO createPlaylistWithoutId() {
    // Create DTO based on JSON string
    String playlistDTOJson = "{\"name\":\"Test Playlist\"}";
    ConvertsData deserialize = new ConvertsData();
    return deserialize.getData(playlistDTOJson, PlaylistDTO.class);
  }

  private PlaylistDTO createPlaylistWithMusics() {
    // Create DTO based on JSON string
    String playlistDTOJson = "{\"name\":\"Test Playlist\",\"musics\":[{\"name\":\"Test Music\",\"artistsList\":[\"Artist 1\",\"Artist 2\"],\"album\":\"Test Album\",\"duration_s\":180.0,\"playlist\":[{\"name\":\"Test Playlist\"}],\"liked\":true}]}";
    ConvertsData deserialize = new ConvertsData();
    return deserialize.getData(playlistDTOJson, PlaylistDTO.class);
  }

  private PlaylistDTO createPlaylistWithId() {
    // Create DTO based on JSON string
    String playlistDTOJson = "{\"id\":\"1\",\"name\":\"Test Playlist\",\"musics\":[{\"name\":\"Test Music\",\"artistsList\":[\"Artist 1\",\"Artist 2\"],\"album\":\"Test Album\",\"duration_s\":180.0,\"playlist\":[{\"name\":\"Test Playlist\"}],\"liked\":true}]}";
    ConvertsData deserialize = new ConvertsData();
    return deserialize.getData(playlistDTOJson, PlaylistDTO.class);
  }
}

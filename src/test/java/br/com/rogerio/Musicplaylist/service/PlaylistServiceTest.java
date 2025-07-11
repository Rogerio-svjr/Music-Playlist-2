package br.com.rogerio.Musicplaylist.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;

import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ActiveProfiles;

import br.com.rogerio.Musicplaylist.dto.PlaylistDTO;
import br.com.rogerio.Musicplaylist.entity.Artist;
import br.com.rogerio.Musicplaylist.entity.MusicEntity;
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
  public void testCreatePlaylist_ShouldCreateNewPlaylist() throws Exception {
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
  public void testUpdatePlaylist_ShouldUpdatePlaylistName() throws Exception {
    // Create playlist and insert in database 
    PlaylistDTO playlistDto = this.createPlaylistWithoutId();
    PlaylistEntity playlistEntity = new PlaylistEntity( playlistDto );
    this.entityManager.persist(playlistEntity);
    this.entityManager.clear();
    // Assert the previous value of each field
    assertEquals(1L, playlistEntity.getId());
    assertEquals("Test Playlist", playlistEntity.getName());
    // Set playlist's id and changes playlist's name
    playlistDto = new PlaylistDTO(playlistEntity);
    playlistDto.setName("Updated Name");
    // Test updatePlaylist()
    PlaylistDTO result = this.playlistService.updatePlaylist(playlistDto);
    // Verify
    assertEquals(1L, result.getId());
    assertEquals("Updated Name", result.getName());
  }

  @Test
  public void testUpdatePlaylist_AddedMusicsInPlaylist_ShouldUpdatePlaylistAndMusics() throws Exception {
    // Create playlist and insert in database 
    PlaylistDTO playlistDto = this.createPlaylistWithoutId();
    PlaylistEntity playlistEntity = new PlaylistEntity( playlistDto );
    this.entityManager.persist(playlistEntity);
    this.entityManager.clear();
    // Assert the previous value of each field
    assertEquals(1L, playlistEntity.getId());
    assertEquals("Test Playlist", playlistEntity.getName());
    assertTrue(playlistEntity.getMusics().isEmpty());
    // Changes playlist's fields
    playlistDto = this.createPlaylistWithId(); // Add musics
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
  public void testUpdatePlaylist_ChangedAlreadyExistingPlaylistName_ShouldUpdateMusicsPlaylistField() throws Exception {
    // Create playlist and music and insert them in database 
    PlaylistDTO playlistDto = this.createPlaylistWithMusics();
    PlaylistEntity playlistEntity = new PlaylistEntity( playlistDto );
    MusicEntity musicEntity = playlistEntity.getMusics().get(0);
    musicEntity.setPlaylist( List.of( playlistEntity ) );
    this.entityManager.persist( playlistEntity );
    this.entityManager.persist( musicEntity );
    this.entityManager.clear();
    // Assert the previous value of each field
    assertEquals(1L, playlistEntity.getId());
    assertEquals("Test Playlist", playlistEntity.getName());
    assertFalse(playlistEntity.getMusics().isEmpty());
    assertEquals(1L, musicEntity.getId());
    assertEquals("Test Playlist", musicEntity.getPlaylist().get(0).getName());
    // Change playlist name
    PlaylistDTO changedPlaylist = new PlaylistDTO( playlistEntity );
    changedPlaylist.setName("Updated Name"); 
    // Test updatePlaylist
    PlaylistDTO result = this.playlistService.updatePlaylist( changedPlaylist );
    // Verify
    assertEquals(1L, result.getMusics().get(0).getId());
    assertEquals("Updated Name", result.getName());
    assertEquals("Updated Name", result.getMusics().get(0).getPlaylist().get(0).getName());
  }

  @Test 
  public void testUpdatePlaylist_PlaylistWithoutIdProvided_ShouldThrowException() {
    // Create playlist without id 
    PlaylistDTO playlistDto = this.createPlaylistWithoutId();
    // Test updatePlaylist
    Exception thrown = assertThrows(Exception.class, () -> {
      PlaylistDTO result = this.playlistService.updatePlaylist(playlistDto);
      assertNull(result.getId());
    });
    assertEquals("Provided playlist without id", thrown.getMessage());
  }

  @Test
  public void testUpdatePlaylist_PlaylistNotFound_ShouldThrowException() {
    // Create playlist without id 
    PlaylistDTO playlistDto = this.createPlaylistWithId();
    // Test updatePlaylist
    Exception thrown = assertThrows(Exception.class, () -> {
      PlaylistDTO result = this.playlistService.updatePlaylist(playlistDto);
      assertNull(result.getId());
    });
    assertEquals("Provided playlist not found", thrown.getMessage());
  }

  @Test
  public void testDeletePlaylist_ShouldDeletePlaylistAndMusicsPlaylistField() throws Exception {
     // Create playlist and music and insert them in database 
    PlaylistDTO playlistDto = this.createPlaylistWithMusics();
    PlaylistEntity playlistEntity = new PlaylistEntity( playlistDto );
    MusicEntity musicEntity = playlistEntity.getMusics().get(0);
    List<PlaylistEntity> playlistList = new ArrayList<>(List.of(playlistEntity));
    musicEntity.setPlaylist( playlistList );
    this.entityManager.persist( playlistEntity );
    this.entityManager.persist( musicEntity );
    // Assert that the playlist and music are saved
    assertEquals(1L, playlistEntity.getId());
    assertFalse(playlistEntity.getMusics().isEmpty());
    assertEquals(1L, musicEntity.getId());
    assertEquals(musicEntity, playlistEntity.getMusics().get(0));
    // Test deletePlaylist
    playlistService.deletePlaylist(1L);
    // Verify
    assertFalse(entityManager.contains(playlistEntity));
    assertTrue(musicEntity.getPlaylist().isEmpty());
  }

  @Test
  public void testDeletePlaylist_PlaylistNotFound_ShouldThrowException() {
    // Try to delete a playlist from database without inserting it first
    Exception thrown = assertThrows(Exception.class, () -> {
      this.playlistService.deletePlaylist(1L);
    });
    assertEquals("No playlist with provided id found", thrown.getMessage());
  }

  private PlaylistDTO createPlaylistWithoutId() {
    // Create mocks
    PlaylistEntity mockPlaylistEntity = Mockito.mock(PlaylistEntity.class);
    // Define mock behavior
    Mockito.when(mockPlaylistEntity.getId()).thenReturn(null);
    Mockito.when(mockPlaylistEntity.getName()).thenReturn("Test Playlist");
    return new PlaylistDTO(mockPlaylistEntity);
  }

  private PlaylistDTO createPlaylistWithMusics() {
    // Create mocks
    PlaylistEntity mockPlaylistEntity = Mockito.mock(PlaylistEntity.class);
    MusicEntity mockMusicEntity = Mockito.mock(MusicEntity.class);
    // Define mock behavior
    Mockito.when(mockPlaylistEntity.getId()).thenReturn(null);
    Mockito.when(mockPlaylistEntity.getName()).thenReturn("Test Playlist");
    Mockito.when(mockMusicEntity.getId()).thenReturn(null);
    Mockito.when(mockMusicEntity.getName()).thenReturn("Test Music");
    Mockito.when(mockMusicEntity.getArtists()).thenReturn(List.of(new Artist("A1"), new Artist("A2")));
    Mockito.when(mockMusicEntity.getAlbumName()).thenReturn("Test Album");
    Mockito.when(mockMusicEntity.getDuration_ms()).thenReturn(185000);
    Mockito.when(mockMusicEntity.getLiked()).thenReturn(false);
    List<MusicEntity> musicsField = new ArrayList<>();
    musicsField.add(mockMusicEntity);
    Mockito.when(mockPlaylistEntity.getMusics()).thenReturn(musicsField);
    return new PlaylistDTO(mockPlaylistEntity);
  }

  private PlaylistDTO createPlaylistWithId() {
    // Create mocks
    PlaylistEntity mockPlaylistEntity = Mockito.mock(PlaylistEntity.class);
    MusicEntity mockMusicEntity = Mockito.mock(MusicEntity.class);
    // Define mock behavior
    Mockito.when(mockPlaylistEntity.getId()).thenReturn(1L);
    Mockito.when(mockPlaylistEntity.getName()).thenReturn("Test Playlist");
    Mockito.when(mockMusicEntity.getId()).thenReturn(null);
    Mockito.when(mockMusicEntity.getName()).thenReturn("Test Music");
    Mockito.when(mockMusicEntity.getArtists()).thenReturn(List.of(new Artist("A1"), new Artist("A2")));
    Mockito.when(mockMusicEntity.getAlbumName()).thenReturn("Test Album");
    Mockito.when(mockMusicEntity.getDuration_ms()).thenReturn(185000);
    Mockito.when(mockMusicEntity.getLiked()).thenReturn(false);
    List<MusicEntity> musicsField = new ArrayList<>();
    musicsField.add(mockMusicEntity);
    Mockito.when(mockPlaylistEntity.getMusics()).thenReturn(musicsField);
    return new PlaylistDTO(mockPlaylistEntity);
  }
}

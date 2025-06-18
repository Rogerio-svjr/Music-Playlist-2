package br.com.rogerio.Musicplaylist.entity;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.fail;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import br.com.rogerio.Musicplaylist.dto.MusicDTO;
import br.com.rogerio.Musicplaylist.dto.PlaylistDTO;

public class MusicEntityTest {
  @Test
  public void testConstructor_MusicNotInAPlaylist_ShouldCreateEntityWithEmptyPlaylist() {
    // Create a mock object 
    MusicDTO mockMusicDTO = Mockito.mock(MusicDTO.class);
    // Defines expected mock behavior
    Mockito.when(mockMusicDTO.getId()).thenReturn(1L);
    Mockito.when(mockMusicDTO.getName()).thenReturn("Test Music");
    Mockito.when(mockMusicDTO.getArtistsList()).thenReturn(List.of("Artist 1", "Artist 2"));
    Mockito.when(mockMusicDTO.getArtistsNames()).thenReturn("Artist 1, Artist 2");
    Mockito.when(mockMusicDTO.getAlbum()).thenReturn("Test Album");
    Mockito.when(mockMusicDTO.getDuration_s()).thenReturn(180F);
    Mockito.when(mockMusicDTO.getLiked()).thenReturn(false);
    // Creates expected results
    List<Artist> expectedArtists = List.of(new Artist("Artist 1"), new Artist("Artist 2"));
    Album expectedAlbum = new Album("Test Album");
    List<PlaylistEntity> expectedPlaylist = new ArrayList<>();
    // Test constructor
    MusicEntity musicEntity = new MusicEntity(mockMusicDTO);
    // Verify
    assertEquals(1L, musicEntity.getId());
    assertEquals("Test Music", musicEntity.getName());
    assertEquals(expectedArtists.get(0).getName(), musicEntity.getArtists().get(0).getName());
    assertEquals(expectedArtists.get(1).getName(), musicEntity.getArtists().get(1).getName());
    assertEquals(expectedAlbum.getName(), musicEntity.getAlbum().getName());
    assertEquals(180000, musicEntity.getDuration_ms());
    assertEquals(expectedPlaylist, musicEntity.getPlaylist());
    assertFalse(musicEntity.getLiked());
  }

  @Test
  public void testConstructor_MusicInTwoPlaylists_shouldReturnPlaylistsId() {
    // Create mock objects 
    PlaylistDTO mockPlaylist1 = Mockito.mock(PlaylistDTO.class);
    PlaylistDTO mockPlaylist2 = Mockito.mock(PlaylistDTO.class);
    MusicDTO mockMusicDTO = Mockito.mock(MusicDTO.class);
    // Defines expected mock behavior
    Mockito.when(mockPlaylist1.getId()).thenReturn(1L);
    Mockito.when(mockPlaylist2.getId()).thenReturn(2L);
    Mockito.when(mockMusicDTO.getPlaylist()).thenReturn(List.of(mockPlaylist1, mockPlaylist2));
    // Test constructor
    MusicEntity musicEntity = new MusicEntity(mockMusicDTO);
    // Verify
    assertEquals(1L, musicEntity.getPlaylist().get(0).getId());
    assertEquals(2L, musicEntity.getPlaylist().get(1).getId());
  }

  @Test
  public void testGetArtists_EntityCameFromDB_ShouldCreateNewArtistsWithArtistsNames() {
    // From database -> artists == null, artistsNames != null
    // Set test up
    MusicEntity testMusicEntity = new MusicEntity();
    // Set "artistsNames" field and keep "artists" field null 
    // to simulate the entity coming from database
    try {
      Field namesField = MusicEntity.class.getDeclaredField("artistsNames");
      namesField.setAccessible(true);
      namesField.set(testMusicEntity, "Test Artist");
    } catch (Exception e) {
      fail("Failed to set up test: " + e.getMessage());
    }
    // Call method under test
    List<Artist> testArtists = testMusicEntity.getArtists();
    // Verify
    assertEquals("Test Artist", testMusicEntity.getArtistsNames());
    assertEquals("Test Artist", testArtists.get(0).getName());
  }
   
  @Test
  public void testGetArtists_EntityCameFromAPI_ShouldReturnArtists() {
    // From API -> artists != null, artistsNames == null
    // Set test up
    MusicEntity testMusicEntity = new MusicEntity();
    List<Artist> testArtist = List.of(new Artist("Test Artist"));
    // Set "artists" field and keep "artistsNames" field null 
    // to simulate the entity coming from Web API
    try {
      Field artistsField = MusicEntity.class.getDeclaredField("artists");
      artistsField.setAccessible(true);
      artistsField.set(testMusicEntity, testArtist);
    } catch(Exception e) {
      fail("Failed to set up test: " + e.getMessage());
    }
    // Call method under test
    List<Artist> testArtists = testMusicEntity.getArtists();
    // Verify
    assertEquals("Test Artist", testArtists.get(0).getName());
  }

  @Test
  public void testGetArtistsNames_EntityCameFromDB_ShouldReturnArtistsNames() {
    // From database -> artists == null, artistsNames != null
    // Set test up
    MusicEntity testMusicEntity = new MusicEntity();
    // Set "artistsNames" field and keep "artists" field null 
    // to simulate the entity coming from database
    try {
      Field namesField = MusicEntity.class.getDeclaredField("artistsNames");
      namesField.setAccessible(true);
      namesField.set(testMusicEntity, "Test Artist");
    } catch (Exception e) {
      fail("Failed to set up test: " + e.getMessage());
    }
    // Call method under test
    String testArtistsNames = testMusicEntity.getArtistsNames();
    // Verify
    assertEquals("Test Artist", testArtistsNames);
  }
  
  @Test
  public void testGetArtistsNames_EntityCameFromAPI_ShouldCreateNewArtistsNames() {
    // From API -> artists != null, artistsNames == null
    // Set test up
    MusicEntity testMusicEntity = new MusicEntity();
    List<Artist> actualArtist = List.of(new Artist("Test Artist"));
    // Set "artists" field and keep "artistsNames" field null 
    // to simulate the entity coming from Web API
    try {
      Field artistsField = MusicEntity.class.getDeclaredField("artists");
      artistsField.setAccessible(true);
      artistsField.set(testMusicEntity, actualArtist);
    } catch(Exception e) {
      fail("Failed to set up test: " + e.getMessage());
    }
    // Call method under test
    String testArtists = testMusicEntity.getArtistsNames();
    // Verify
    assertEquals("Test Artist", testArtists);
  }

  @Test
  public void testGetAlbum_EntityCameFromDB_ShouldCreateNewAlbum() {
    // From database -> album == null, albumName != null
    MusicEntity testMusicEntity = new MusicEntity();
    String testAlbum = "Test Album";
    try {
      // Reflect albumName field, supress it's access controls checks and set it's value
      Field albumField = MusicEntity.class.getDeclaredField("albumName");
      albumField.setAccessible(true);
      albumField.set(testMusicEntity, testAlbum);
      // Keep album null
    } catch(Exception e) {
      fail("Failed to set up test: " + e.getMessage());
    }
    // Call method under test
    Album actualAlbum = testMusicEntity.getAlbum();
    // Verify
    assertEquals("Test Album", actualAlbum.getName());
  }
  
  @Test
  public void testGetAlbum_EntityCameFromAPI_ShouldReturnAlbum() {
    // From API -> album != null, albumName == null
    MusicEntity testMusicEntity = new MusicEntity();
    Album testAlbum = new Album("Test Album");
    // Reflect album field, suppress it's access control checks and set it's value
    try {
      Field albumField = MusicEntity.class.getDeclaredField("album");
      albumField.setAccessible(true);
      albumField.set(testMusicEntity, testAlbum);
      // Keep albumName null
    } catch(Exception e) {
      fail("Failed to set up test: " + e.getMessage());
    }
    // Call method under test
    Album actualAlbum = testMusicEntity.getAlbum();
    // Verify
    assertEquals("Test Album", actualAlbum.getName());
  }

  @Test
  public void testGetAlbumName_EntityCameFromDB_ShouldReturnAlbumName() {
    // From DB -> album == null, albumNames != null
    MusicEntity testMusicEntity = new MusicEntity();
    String testAlbumName = "Test Album";
    // Reflect albumName field, suppress it's access control checks and set it's value
    try {
      Field albumNameField = MusicEntity.class.getDeclaredField("albumName");
      albumNameField.setAccessible(true);
      albumNameField.set(testMusicEntity, testAlbumName);
    } catch (Exception e) {
      fail("Failed to set up test: " + e.getMessage());
    }
    // Call method under test
    String actualAlbumName = testMusicEntity.getAlbumName();
    // Verify
    assertEquals("Test Album", actualAlbumName);
  }
  
  @Test
  public void testGetAlbumName_EntityCameFromAPI_ShouldCreateNewAlbumName() {
    // From API -> album != null, albumNames == null
    MusicEntity testMusicEntity = new MusicEntity();
    Album testAlbum = new Album("Test Album");
    // Reflect albumName field, suppress it's access control checks and set it's value
    try {
      Field albumNameField = MusicEntity.class.getDeclaredField("album");
      albumNameField.setAccessible(true);
      albumNameField.set(testMusicEntity, testAlbum);
    } catch (Exception e) {
      fail("Failed to set up test: " + e.getMessage());
    }
    // Call method under test
    String actualAlbumName = testMusicEntity.getAlbumName();
    // Verify
    assertEquals("Test Album", actualAlbumName);
  }
}

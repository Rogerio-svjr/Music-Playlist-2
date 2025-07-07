package br.com.rogerio.Musicplaylist.dto;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import br.com.rogerio.Musicplaylist.entity.Artist;
import br.com.rogerio.Musicplaylist.entity.MusicEntity;
import br.com.rogerio.Musicplaylist.entity.PlaylistEntity;

public class MusicDTOTest {
  @Test
  public void testConstructor_MusicEntityCameFromAPI_ShouldNotHaveId() {
    // Creates a mock object
    MusicEntity mockMusicEntity = Mockito.mock(MusicEntity.class);
    // Defines the expected mocked object behavior
    Mockito.when(mockMusicEntity.getId()).thenReturn( null );
    Mockito.when(mockMusicEntity.getName()).thenReturn("Test Music");
    Mockito.when(mockMusicEntity.getArtists()).thenReturn(List.of(new Artist("A1"), new Artist("A2")));
    Mockito.when(mockMusicEntity.getAlbumName()).thenReturn("Test Album");
    Mockito.when(mockMusicEntity.getDuration_ms()).thenReturn(185000);
    Mockito.when(mockMusicEntity.getLiked()).thenReturn(false);
    // Creates expected Artist list
    List<String> artistsList = List.of("A1", "A2");
    // Test constructor
    MusicDTO musicDTO = new MusicDTO(mockMusicEntity);

    assertNull( musicDTO.getId() );
    assertEquals( "Test Music", musicDTO.getName() );
    assertEquals( artistsList, musicDTO.getArtistsList() );
    assertEquals( "Test Album", musicDTO.getAlbum() );
    assertEquals( 185000 / 1000, musicDTO.getDuration_s() );
    assertEquals( false, musicDTO.getLiked() );
  }
  
  @Test
  public void testConstructor_MusicEntityCameFromDB_ShouldHaveId() {
    // Creates a mock object
    MusicEntity mockMusicEntity = Mockito.mock(MusicEntity.class);
    // Defines the expected mocked object behavior
    Mockito.when(mockMusicEntity.getId()).thenReturn( 1L );
    Mockito.when(mockMusicEntity.getName()).thenReturn("Test Music");
    Mockito.when(mockMusicEntity.getArtists()).thenReturn(List.of(new Artist("A1"), new Artist("A2")));
    Mockito.when(mockMusicEntity.getAlbumName()).thenReturn("Test Album");
    Mockito.when(mockMusicEntity.getDuration_ms()).thenReturn(185000);
    Mockito.when(mockMusicEntity.getLiked()).thenReturn(true);
    // Creates expected Artist list
    List<String> artistsList = List.of("A1", "A2");
    // Test constructor
    MusicDTO musicDTO = new MusicDTO(mockMusicEntity);

    assertEquals( 1L, musicDTO.getId() );
    assertEquals( "Test Music", musicDTO.getName() );
    assertEquals( artistsList, musicDTO.getArtistsList() );
    assertEquals( "Test Album", musicDTO.getAlbum() );
    assertEquals( 185000 / 1000, musicDTO.getDuration_s() );
    assertEquals( true, musicDTO.getLiked() );
  }

  @Test
  public void TestConstructor_PlaylistIsSettedManually_ShouldNotConvertPlaylist() {
    // Creates a mock object
    MusicEntity mockMusicEntity = Mockito.mock(MusicEntity.class);
    // Defines the expected mocked object behavior
    Mockito.when(mockMusicEntity.getId()).thenReturn( 1L );
    Mockito.when(mockMusicEntity.getName()).thenReturn("Test Music");
    Mockito.when(mockMusicEntity.getArtists()).thenReturn(List.of(new Artist("A1"), new Artist("A2")));
    Mockito.when(mockMusicEntity.getAlbumName()).thenReturn("Test Album");
    Mockito.when(mockMusicEntity.getDuration_ms()).thenReturn(185000);
    Mockito.when(mockMusicEntity.getLiked()).thenReturn(true);
    MusicDTO musicDTO = new MusicDTO(mockMusicEntity);
    // Creates expected empty array
    List<PlaylistDTO> expectedPlaylistList = new ArrayList<>(); 

    // Since the playlist field doesn't have any value until the music is 
    // added to a playlist it's expected value is and empty array
    assertEquals( expectedPlaylistList, musicDTO.getPlaylist());
  }

  @Test
  public void testSetPlaylist() {
    // Create playlists mocks
    PlaylistDTO mockPlaylistDTO1 = Mockito.mock(PlaylistDTO.class);
    PlaylistDTO mockPlaylistDTO2 = Mockito.mock(PlaylistDTO.class);
    // Define mocks behavior
    Mockito.when(mockPlaylistDTO1.getId()).thenReturn(1L);
    Mockito.when(mockPlaylistDTO2.getId()).thenReturn(2L);
    Mockito.when(mockPlaylistDTO1.getName()).thenReturn("playlist 1");
    Mockito.when(mockPlaylistDTO2.getName()).thenReturn("playlist 2");
    // Creates expected list of playlists
    MusicDTO musicDTO = new MusicDTO();
    List<PlaylistDTO> playlistList = List.of( mockPlaylistDTO1, mockPlaylistDTO2 );
    // Test set playlist
    musicDTO.setPlaylist(playlistList);

    assertEquals( playlistList, musicDTO.getPlaylist() );
    assertEquals(1L, musicDTO.getPlaylist().get(0).getId());
    assertEquals(2L, musicDTO.getPlaylist().get(1).getId());
    assertEquals( "playlist 1", musicDTO.getPlaylist().get(0).getName() );
    assertEquals( "playlist 2", musicDTO.getPlaylist().get(1).getName() );
  }

  @Test
  public void testGetArtistsNames_WithTwoArtists_ShouldReturnASingleStringSeparatedByComasAndSpace() {
    // Creates a mock entity
    MusicEntity mockMusicEntity = Mockito.mock(MusicEntity.class);
    // Defines the mock entity behavior
    Mockito.when(mockMusicEntity.getArtists()).thenReturn(List.of(new Artist("A1"), new Artist("A2")));
    // Creates musicDTO from mocked entity
    MusicDTO musicDTO = new MusicDTO(mockMusicEntity);
    // Creates expected artists string
    String expectedArtists = "A1, A2";

    assertEquals(expectedArtists, musicDTO.getArtistsNames());
  }
  
  @Test
  public void testGetArtistsNames_WithTwoArtists_ShouldNotReturnAStringWithoutSpaces() {
    // Creates a mock entity
    MusicEntity mockMusicEntity = Mockito.mock(MusicEntity.class);
    // Defines the mock entity behavior
    Mockito.when(mockMusicEntity.getArtists()).thenReturn(List.of(new Artist("A1"), new Artist("A2")));
    // Creates musicDTO from mocked entity
    MusicDTO musicDTO = new MusicDTO(mockMusicEntity);
    // Creates expected artists string
    String notExpectedArtists = "A1,A2";

    assertNotEquals(notExpectedArtists, musicDTO.getArtistsNames());
  }
  
  @Test
  public void testGetArtistsNames_WithTwoArtists_ShouldNotReturnAStringWithoutComas() {
    // Creates a mock entity
    MusicEntity mockMusicEntity = Mockito.mock(MusicEntity.class);
    // Defines the mock entity behavior
    Mockito.when(mockMusicEntity.getArtists()).thenReturn(List.of(new Artist("A1 "), new Artist("A2")));
    // Creates musicDTO from mocked entity
    MusicDTO musicDTO = new MusicDTO(mockMusicEntity);
    // Creates expected artists string
    String notExpectedArtists = "A1 A2";

    assertNotEquals(notExpectedArtists, musicDTO.getArtistsNames());
  }
  
  @Test
  public void testGetArtistsNames_WithTwoArtists_ShouldNotReturnAStringWithoutSpacesAndComas() {
    // Creates a mock entity
    MusicEntity mockMusicEntity = Mockito.mock(MusicEntity.class);
    // Defines the mock entity behavior
    Mockito.when(mockMusicEntity.getArtists()).thenReturn(List.of(new Artist("A1"), new Artist("A2")));
    // Creates musicDTO from mocked entity
    MusicDTO musicDTO = new MusicDTO(mockMusicEntity);
    // Creates expected artists string
    String notExpectedArtists = "A1A2";
    
    assertNotEquals(notExpectedArtists, musicDTO.getArtistsNames());
  }
  
  @Test
  public void testGetDuration_min() {
    // Creates a mock entity
    MusicEntity mockMusicEntity = Mockito.mock(MusicEntity.class);
    // Defines the mocked music duration to 3 min 10 s
    Mockito.when(mockMusicEntity.getDuration_ms()).thenReturn(190000); 
    MusicDTO musicDTO = new MusicDTO(mockMusicEntity);

    String actual = musicDTO.getDuration_min();
    String expected = "3:10";

    assertEquals(expected, actual);
  }
  
  @Test
  public void testGetDuration_min_MinutesShouldNotHaveTwoDigits() {
    // Creates a mock entity
    MusicEntity mockMusicEntity = Mockito.mock(MusicEntity.class);
    // Defines the mocked music duration to 3 min 10 s
    Mockito.when(mockMusicEntity.getDuration_ms()).thenReturn(190000); 
    MusicDTO musicDTO = new MusicDTO(mockMusicEntity);

    String actual = musicDTO.getDuration_min();
    String notExpected = "03:10";

    assertNotEquals(notExpected, actual);
  }
  
  @Test
  public void testGetDuration_min_PassingMaxValue_ShouldReturn35791MinAnd22S() {
    // Creates a mock entity
    MusicEntity mockMusicEntity = Mockito.mock(MusicEntity.class);
    // Defines the mocked music duration to integer maximum value (2^31-1)
    Mockito.when(mockMusicEntity.getDuration_ms()).thenReturn(Integer.MAX_VALUE); 
    MusicDTO musicDTO = new MusicDTO(mockMusicEntity);

    String actual = musicDTO.getDuration_min();
    String expected = "35791:22";

    assertEquals(expected, actual);
  }
  
  @Test
  public void testGetDuration_min_PassingMinValue_ShouldReturnMinus35791MinAndMinus22S() {
    // Creates a mock entity
    MusicEntity mockMusicEntity = Mockito.mock(MusicEntity.class);
    // Defines the mocked music duration to integer maximum value (2^31-1)
    Mockito.when(mockMusicEntity.getDuration_ms()).thenReturn(Integer.MIN_VALUE); 
    MusicDTO musicDTO = new MusicDTO(mockMusicEntity);

    String actual = musicDTO.getDuration_min();
    String Expected = "-35791:-22";

    assertEquals(Expected, actual);
  }

  @Test
  public void testAddPlaylist_ShouldAddNewPlaylistToplaylistField() {
    // Create playlists mock
    PlaylistEntity mockPlaylistEntity = Mockito.mock(PlaylistEntity.class);
    // Define mock behavior
    Mockito.when(mockPlaylistEntity.getId()).thenReturn(1L);
    Mockito.when(mockPlaylistEntity.getName()).thenReturn("Test Playlist");
    // Create music and playlist
    MusicDTO musicDto = new MusicDTO();
    PlaylistDTO playlistDto = new PlaylistDTO(mockPlaylistEntity);
    // test addPlaylist
    musicDto.addPlaylist(playlistDto);
    // Verify
    assertFalse(musicDto.getPlaylist().isEmpty());
    assertEquals(1L, musicDto.getPlaylist().get(0).getId());
    assertEquals("Test Playlist", musicDto.getPlaylist().get(0).getName());
  }
}
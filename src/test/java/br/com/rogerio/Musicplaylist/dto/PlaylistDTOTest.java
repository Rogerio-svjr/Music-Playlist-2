package br.com.rogerio.Musicplaylist.dto;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;

import java.util.List;

import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import br.com.rogerio.Musicplaylist.entity.MusicEntity;
import br.com.rogerio.Musicplaylist.entity.PlaylistEntity;

public class PlaylistDTOTest {
  @Test
  public void testConstructorFromAPI() {
    // Creates a mock entities
    PlaylistEntity mockPlaylistEntity = Mockito.mock(PlaylistEntity.class);
    MusicEntity mockMusicEntity1 = Mockito.mock(MusicEntity.class);
    MusicEntity mockMusicEntity2 = Mockito.mock(MusicEntity.class);
    // Defines mocks behavior
    Mockito.when(mockPlaylistEntity.getMusics()).thenReturn(List.of(mockMusicEntity1, mockMusicEntity2));
    Mockito.when(mockMusicEntity1.getAlbumName()).thenReturn("A1");
    Mockito.when(mockMusicEntity2.getAlbumName()).thenReturn("A2");
    Mockito.when(mockMusicEntity1.getName()).thenReturn("Music 1");
    Mockito.when(mockMusicEntity2.getName()).thenReturn("Music 2");
    // Prepare expected list of musics
    MusicDTO music1 = new MusicDTO(mockMusicEntity1);
    MusicDTO music2 = new MusicDTO(mockMusicEntity2);
    List<MusicDTO> expectedMusicList = List.of(music1, music2);
    // Test constructor
    PlaylistDTO playlistDTO = new PlaylistDTO(mockPlaylistEntity);

    assertNull( playlistDTO.getName() );
    assertNotNull( playlistDTO.getMusics() );
    assertEquals( expectedMusicList.get(0).getName(), playlistDTO.getMusics().get(0).getName() );
    assertEquals( expectedMusicList.get(1).getName(), playlistDTO.getMusics().get(1).getName() );
  }
  
  @Test
  public void testConstructorFromDB() {
    // Creates a mock entities
    PlaylistEntity mockPlaylistEntity = Mockito.mock(PlaylistEntity.class);
    // Defines mocks behavior
    Mockito.when(mockPlaylistEntity.getId()).thenReturn(1L);
    Mockito.when(mockPlaylistEntity.getName()).thenReturn("Test Playlist");
    // Test constructor
    PlaylistDTO playlistDTO = new PlaylistDTO(mockPlaylistEntity);

    assertEquals( 1L, playlistDTO.getId() );
    assertEquals("Test Playlist", playlistDTO.getName());
  }
}

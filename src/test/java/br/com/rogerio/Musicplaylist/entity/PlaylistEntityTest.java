package br.com.rogerio.Musicplaylist.entity;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import br.com.rogerio.Musicplaylist.dto.MusicDTO;
import br.com.rogerio.Musicplaylist.dto.PlaylistDTO;

public class PlaylistEntityTest {
  @Test
  public void testConstructor_PlaylistWithMusics_ShouldCreateMusicEntities() {
    // Create mock entities
    PlaylistDTO mockPlaylisDTO = Mockito.mock(PlaylistDTO.class);
    MusicDTO mockMusicDTO1 = Mockito.mock(MusicDTO.class);
    MusicDTO mockMusicDTO2 = Mockito.mock(MusicDTO.class);
    // Define mocks behavior
    Mockito.when(mockPlaylisDTO.getId()).thenReturn(1L);
    Mockito.when(mockPlaylisDTO.getName()).thenReturn("Test Playlist");
    Mockito.when(mockPlaylisDTO.getMusics()).thenReturn(List.of(mockMusicDTO1, mockMusicDTO2));
    Mockito.when(mockMusicDTO1.getName()).thenReturn("Test Music 1");
    Mockito.when(mockMusicDTO2.getName()).thenReturn("Test Music 2");
    // Test constructor
    PlaylistEntity testPlaylistEntity = new PlaylistEntity(mockPlaylisDTO);
    // Verify
    assertEquals(1L, testPlaylistEntity.getId());
    assertEquals("Test Playlist", testPlaylistEntity.getName());
    assertEquals("Test Music 1", testPlaylistEntity.getMusics().get(0).getName());
    assertEquals("Test Music 2", testPlaylistEntity.getMusics().get(1).getName());
  }

  @Test
  public void testConstructor_PlaylistEmpty_ShouldNotCreateMusicEntities() {
    // Create mock entities
    PlaylistDTO mockPlaylisDTO = Mockito.mock(PlaylistDTO.class);
    // Define mocks behavior
    Mockito.when(mockPlaylisDTO.getId()).thenReturn(1L);
    Mockito.when(mockPlaylisDTO.getName()).thenReturn("Test Playlist");
    Mockito.when(mockPlaylisDTO.getMusics()).thenReturn(new ArrayList<>());
    // Create expecte empty array list
    List<MusicEntity> excpectedMusicList = new ArrayList<>();
    // Test constructor
    PlaylistEntity testePlaylistEntity = new PlaylistEntity(mockPlaylisDTO);
    // Verify
    assertEquals(1L, testePlaylistEntity.getId());
    assertEquals("Test Playlist", testePlaylistEntity.getName());
    assertEquals(excpectedMusicList, testePlaylistEntity.getMusics());
  }
}

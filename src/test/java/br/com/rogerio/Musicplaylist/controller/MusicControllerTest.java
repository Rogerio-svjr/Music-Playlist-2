package br.com.rogerio.Musicplaylist.controller;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.fasterxml.jackson.databind.ObjectMapper;

import br.com.rogerio.Musicplaylist.dto.MusicDTO;
import br.com.rogerio.Musicplaylist.service.ConvertsData;
import br.com.rogerio.Musicplaylist.service.MusicService;
import br.com.rogerio.Musicplaylist.service.SpotifyRequestService;

@WebMvcTest(MusicController.class)
@AutoConfigureMockMvc(addFilters = false)
@ExtendWith(MockitoExtension.class)
public class MusicControllerTest {
  
  @Autowired
  private MockMvc mockMvc; // Performs requests

  @Autowired
  private ObjectMapper objectMapper;

  @MockBean
  private MusicService musicService;

  @MockBean
  private SpotifyRequestService spotifyRequestService;

  private static final String END_POINT_PATH = "/api/music";

  @Test
  public void testInsertMusic_ShouldInsertMusicAndReturnStatus200Ok() throws Exception {
    MusicDTO musicDto = createMusicWithoutId();

    String requestBody = objectMapper.writeValueAsString(musicDto);

    ResultActions response = mockMvc.perform(post(END_POINT_PATH)
      .contentType(MediaType.APPLICATION_JSON)
      .content(requestBody));

    response.andExpect(status().isCreated());
  }

  private MusicDTO createMusicWithoutId() {
    // Create DTO based on JSON string
    String musicDTOJson = "{\"name\":\"Test Music\",\"artistsList\":[\"Artist 1\",\"Artist 2\"],\"album\":\"Test Album\",\"duration_s\":180.0,\"playlist\":[{\"name\":\"Test Playlist\"}],\"liked\":true}";
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

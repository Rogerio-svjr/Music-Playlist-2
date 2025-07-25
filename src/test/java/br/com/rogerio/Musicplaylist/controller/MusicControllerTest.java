package br.com.rogerio.Musicplaylist.controller;

import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.List;

import com.fasterxml.jackson.databind.ObjectMapper;

import br.com.rogerio.Musicplaylist.dto.MusicDTO;
import br.com.rogerio.Musicplaylist.service.ConvertsData;
import br.com.rogerio.Musicplaylist.service.MusicService;
import br.com.rogerio.Musicplaylist.service.SpotifyRequestService;

@WebMvcTest(MusicController.class)
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
    // Creates a request body without id
    String requestBody = "{\"name\":\"Test Music\",\"artistsList\":[\"Artist 1\",\"Artist 2\"],\"album\":\"Test Album\",\"duration_s\":180.0,\"playlist\":[{\"name\":\"Test Playlist\"}],\"liked\":true}";
    // Send the post request with the music in the request body
    ResultActions response = mockMvc.perform(post(END_POINT_PATH)
      .contentType(MediaType.APPLICATION_JSON)
      .content(requestBody));
    // Verify if the response status is 201 Created
    response.andExpect(status().isCreated());
    // Verify if the createMusic method happened
    verify(musicService).createMusic(any(MusicDTO.class));
  }

  @Test
  public void testInsertMusic_RequestBodyWithInvalidData_ShouldReturnStatus400BadRequest() throws Exception {
    // Creates and empty request body
    String requestBody = "";
    // Send the Post request with the empty string in the request body
    ResultActions response = mockMvc.perform(post(END_POINT_PATH)
      .contentType(MediaType.APPLICATION_JSON)
      .content(requestBody));
    // Verify if the response status is 400 Bad Request
    response.andExpect(status().isBadRequest());
  }

  @Test
  public void testSearchMusic_ShouldSearchMusicInSpotifyAndReturnStatus200Ok() throws Exception {
    // Creates and defines mocked service behavior
    List<MusicDTO> mockedList = List.of(createMusicWithoutId(), createMusicWithoutId2(), createMusicWithoutId3());
    Mockito.when(spotifyRequestService.searchMusic("Test")).thenReturn(mockedList);
    // Send the Get request with the music name variable on the request path
    ResultActions response = mockMvc.perform(get(END_POINT_PATH + "/search/Test"));
    // Verify if the response status is 200 OK and if the searchMusic method happened
    response.andExpect(status().isOk())
      .andExpect(jsonPath("$.size()").value(3))
      .andExpect(jsonPath("$[0].name").value("Test Music"));
  }

  @Test
  public void testSearchMusic_PathWithoutVariable_ShouldReturnStatus400BadRequest() throws Exception {
    // Send the Get request without the music name variable on the request path
    ResultActions response = mockMvc.perform(get(END_POINT_PATH + "/search"));
    // Verify if the response status is 400 Bad Request
    response.andExpect(status().isBadRequest());
  }

  @Test
  public void testListAllMusic_ShouldReturnListOfAllMusicsAndStatus200Ok() throws Exception {
    // Creates and defines mocked service behavior
    List<MusicDTO> mockedList = List.of(createMusicWithId(), createMusicWithId2());
    Mockito.when(musicService.readAllMusic()).thenReturn(mockedList);
    // Send Get request with "all" path
    ResultActions response = mockMvc.perform(get(END_POINT_PATH + "/all"));
    // Verify if the response status is 200 Ok and if the readAllMusic() method happened
    response.andExpect(status().isOk())
      .andExpect(jsonPath("$.size()").value(2))
      .andExpect(jsonPath("$[0].name").value("Test Music"));
  }

  /*
   * First: Write all unit tests and implement in the controller what needs to be implemented
   * to succeed in the tests, with generic Exceptions when needed;
   * 
   * Second: Create an "exceptions" package and custom exceptions for every service;
   * 
   * Third: Treat those custom exceptions in the controller layer and adapt the tests to throw them
   */

  @Test
  public void testListAllMusics_NoMusicsInDatabase_ShouldReturnEmptyListAndStatus204NoContent() {

  }

  @Test
  public void testRequestMusicById_ShouldReturnRequestedMusicAndStatus200Ok() {

  }
  
  @Test
  public void testRequestMusicById_MusicNotInDatabase_ShouldReturnStatus404NotFound() {

  }

  @Test
  public void testUpdateMusic_ShouldUpdateMusicAndReturnStatus200Ok() {

  }

  @Test
  public void testUpdateMusic_RequestBodyWithoutId_ShouldReturnStatus400BadRequest() {

  }

  @Test
  public void testUpdateMusic_RequestBodyNotFound_ShouldReturnStatus404NotFound() {

  }

  @Test
  public void testDeleteMusic_ShouldDeleteMusicAndReturnStatus204NoContent() {

  }

  @Test
  public void testDeleteMusic_RequestBodyNotFound_ShouldReturnStatus404NotFound() {

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
  
  private MusicDTO createMusicWithoutId3() {
    // Create DTO based on JSON string
    String musicDTOJson = "{\"name\":\"Test Music 3\",\"artistsList\":[\"Artist 1\",\"Artist 2\"],\"album\":\"Test Album 2\",\"duration_s\":210.0,\"playlist\":[],\"liked\":false}";
    ConvertsData deserialize = new ConvertsData();
    return deserialize.getData(musicDTOJson, MusicDTO.class);
  }
  
  private MusicDTO createMusicWithId() {
    // Create DTO identical with the first method but with id based on JSON string
    String musicDTOJson = "{\"id\":\"1\",\"name\":\"Test Music\",\"artistsList\":[\"Artist 1\",\"Artist 2\"],\"album\":\"Test Album\",\"duration_s\":180.0,\"playlist\":[],\"liked\":true}";
    ConvertsData deserialize = new ConvertsData();
    return deserialize.getData(musicDTOJson, MusicDTO.class);
  }
  
  private MusicDTO createMusicWithId2() {
    // Create DTO identical with the first method but with id based on JSON string
    String musicDTOJson = "{\"id\":\"1\",\"name\":\"Test Music 2\",\"artistsList\":[\"Artist 1\",\"Artist 2\"],\"album\":\"Test Album\",\"duration_s\":180.0,\"playlist\":[],\"liked\":true}";
    ConvertsData deserialize = new ConvertsData();
    return deserialize.getData(musicDTOJson, MusicDTO.class);
  }
}

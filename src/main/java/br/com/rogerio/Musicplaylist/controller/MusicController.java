package br.com.rogerio.Musicplaylist.controller;

import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import br.com.rogerio.Musicplaylist.dto.MusicDTO;
import br.com.rogerio.Musicplaylist.service.MusicService;
import br.com.rogerio.Musicplaylist.service.SpotifyRequestService;

@RestController
@RequestMapping(value = "/api/music")
public class MusicController {

  @Autowired
  private SpotifyRequestService spotifyRequest;

  @Autowired
  private MusicService musicService;

  @GetMapping("/search/{name}")
  public List<MusicDTO> searchMusic( @PathVariable String name ) {
    // Searches musics in Spotify portfolio and returns a list with the 3 first results
    return spotifyRequest.searchMusic(name);
  }

  @GetMapping("/all")
  public List<MusicDTO> listAllMusics() {
    try {
      // Searches all musics in the database and returns them
      return musicService.readAllMusic();
    } catch( Exception e ) {
      // Returns an empty list if there's no musics in the database
      return new ArrayList<MusicDTO>();
    }
  }

  @GetMapping("/{id}")
  public MusicDTO requestMusicById( @PathVariable Long id ) {
    try {
      // Searches music with specified id in the database
      return musicService.readMusicById( id );
    } catch( NoSuchElementException e ) {
      // Throws a 400 Bad Request error if there's no music with specified id
      throw new ResponseStatusException( HttpStatus.BAD_REQUEST, "Music not found with ID: " + id );
      // Idea: Create custom exceptions for easier error responses
    }
  }

  @PostMapping
  public ResponseEntity<MusicDTO> insertMusic( @RequestBody MusicDTO music ) {
    return new ResponseEntity<>( musicService.createMusic(music), HttpStatus.CREATED );
  }

  @PutMapping
  public MusicDTO updateMusic( @RequestBody MusicDTO music ) {
    try {
      // Update the music that matches the id of the provided music
      return musicService.updateMusic(music);
    } catch ( Exception e ) {
      // Throw a 400 Bad Request error if no music that matches the specified music id is found
      throw new ResponseStatusException( HttpStatus.BAD_REQUEST, e.getMessage() );
    }
  }
  
  @DeleteMapping("/{id}")
  public void deteleMusic( @PathVariable Long id ) {
    try {
      // Delete the music that matches the provided id
      musicService.deleteMusic(id);
    } catch( Exception e ) {
      // Throw a 400 Bad Request error if no music that matches the specified id was found
      throw new ResponseStatusException( HttpStatus.BAD_REQUEST, e.getMessage() );
    }
  }

}

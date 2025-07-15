package br.com.rogerio.Musicplaylist.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import br.com.rogerio.Musicplaylist.service.SpotifyRequestService;

@RestController
@RequestMapping(value = "/music")
public class MusicController {

  @Autowired
  private SpotifyRequestService spotifyRequest;

  // public List<MusicDTO> searchMusic( String name ) {
  //   // Searches musics in Spotify portfolio and returns a list with the 10 first results
    
  // }

}

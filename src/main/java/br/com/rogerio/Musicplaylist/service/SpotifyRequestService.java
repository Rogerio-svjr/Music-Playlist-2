package br.com.rogerio.Musicplaylist.service;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpRequest.BodyPublishers;
import java.net.http.HttpResponse;
import java.net.http.HttpResponse.BodyHandlers;
import java.util.ArrayList;
import java.util.Base64;
import java.util.List;

import br.com.rogerio.Musicplaylist.config.SpotifyApiAuthCode;
import br.com.rogerio.Musicplaylist.dto.AccessToken;
import br.com.rogerio.Musicplaylist.entity.TrackSearchResult;
import br.com.rogerio.Musicplaylist.dto.MusicDTO;

public class SpotifyRequestService {
	private AccessToken token;
	String accessToken;

	HttpClient client = HttpClient.newHttpClient();
	
	ConvertsData convertsData = new ConvertsData();

	private short searchOffset = 0;

	private short searchLimit = 3;

	private String currentSearchName = null;

	public void setSearchLimit(short searchLimit) {
		this.searchLimit = searchLimit;
	}

	public void setSearchOffset(short searchOffset) {
		this.searchOffset = searchOffset;
	}

	public short getSearchOffset() {
		return searchOffset;
	}

	private AccessToken tokenRequest() {
		// Request body from a String
		String bodyStr = "grant_type=client_credentials";
		// Spotify "clinet_id":"client_secret" (most not be revealed)
		String originalAuth = SpotifyApiAuthCode.getAuthorizationCode(); 
		String encodedAutho = Base64.getEncoder().encodeToString( originalAuth.getBytes() );

		// POST request
		HttpRequest requestToken = HttpRequest.newBuilder()
			.uri( URI.create( "https://accounts.spotify.com/api/token" ) )
			.header( "Content-Type", "application/x-www-form-urlencoded" )
			.header( "Authorization", "Basic " + encodedAutho )
			.POST( BodyPublishers.ofString( bodyStr ) )
			.build();

		// POST request response
		HttpResponse<String> responsePOST = null;
		try {
			responsePOST = client.send( requestToken, BodyHandlers.ofString() );
		} catch ( InterruptedException | IOException e ) {
			e.printStackTrace();
		}

		return convertsData.getData( responsePOST.body(), AccessToken.class );
	}

	public TrackSearchResult searchRequest( String searchName ) {
		// Refactor the track name to fit in the url
		searchName = searchName.replace(" ", "-");
		this.currentSearchName = searchName;
		// Build the track request with uri and header
		HttpRequest requestTrack = HttpRequest.newBuilder()
			.uri( URI.create( "https://api.spotify.com/v1/search?q=" + searchName + "&type=track&limit=" + searchLimit + "&offset=" + searchOffset ) )
			.header( "Authorization", "Bearer " + this.accessToken )
			.build();
		// Send the request and get the response
		HttpResponse<String> responseGET = null;
		try {
			responseGET = client.send( requestTrack, HttpResponse.BodyHandlers.ofString() );
		} catch (IOException | InterruptedException e) {
			e.printStackTrace();
		}
		// Deserialize the resonse JSON string
		TrackSearchResult result = convertsData.getData( responseGET.body(), TrackSearchResult.class );
		// If the access token expired or is invalid the request response is an error 401,
		// So the access token needs to be requested again
		if ( result.getPlaylist() == null && result.getError() != null ) {
			if ( result.getError().status() == 401 ) {
				this.token = this.tokenRequest();
				this.accessToken = token.accessToken().replace(" ", "+");
				result = this.searchRequest(searchName);
			}
		} else if ( result.getPlaylist() != null ) {
			result.setError(null);
		}

		return result;
	}

	public List<MusicDTO> searchMusic( String musicName ) {
		TrackSearchResult searchResult = this.searchRequest( musicName );
		// Converts from entity to dto
		List<MusicDTO> searchResulDto = new ArrayList<>(searchResult.getPlaylist().getMusics()
			.stream().map(MusicDTO::new).toList());
			// When the music comes from API, there's no need to set the "playlist" field
		return searchResulDto;
	}
	
	public TrackSearchResult nextTrackRequestPage( ) {
		// Update the offset parameter in the request uri to request the next 10 tracks 
		this.searchOffset += this.searchLimit;
		return this.searchRequest( currentSearchName );
	}
	
	public TrackSearchResult previousTrackRequestPage( ) {
		// Make sure the offset won't ever get below 0
		if( this.searchOffset == 0 ) {
			return null;
		} else if (this.searchLimit > this.searchOffset) {
			this.searchOffset = 0;
		} else {
			// Update the offset parameter in the request uri to request the previous 10 tracks 
			this.searchOffset -= this.searchLimit;
		}
		return this.searchRequest( currentSearchName );
	}
}

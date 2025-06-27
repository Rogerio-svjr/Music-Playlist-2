package br.com.rogerio.Musicplaylist.service;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpRequest.BodyPublishers;
import java.net.http.HttpResponse;
import java.net.http.HttpResponse.BodyHandlers;
import java.util.Base64;

import br.com.rogerio.Musicplaylist.dto.AccessToken;
import br.com.rogerio.Musicplaylist.entity.TrackSearchResult;

public class spotifyRequest {
	private AccessToken token;
	String accessToken;

	HttpClient client = HttpClient.newHttpClient();
	
	ConvertsData convertsData = new ConvertsData();

	private int searchOffset = 0;
	private String currentTrackName = null;

	// public ApiConsumption()

	private AccessToken tokenRequest() {
		// Request body from a String
		String bodyStr = "grant_type=client_credentials";
		// Spotify "clinet_id":"client_secret" (most not be revealed)
		String originalAuth = "a4579528d58b40568d2f86713eeb7144:ef424bd474bd489faf1d3831cc6f1f43"; 
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

	public TrackSearchResult trackRequest( String trackName ) {
		// Refactor the track name to fit in the url
		trackName = trackName.replace(" ", "-");
		currentTrackName = trackName;
		// Build the track request with uri and header
		HttpRequest requestTrack = HttpRequest.newBuilder()
			.uri( URI.create( "https://api.spotify.com/v1/search?q=" + trackName + "&type=track&limit=10&offset=" + searchOffset ) )
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
				result = this.trackRequest(trackName);
			}
		} else if ( result.getPlaylist() != null ) {
			result.setError(null);
		}

		return result;
	}
	
	public TrackSearchResult nextTrackRequestPage( ) {
		// Update the offset parameter in the request uri to request the next 10 tracks 
		searchOffset += 10;
		return this.trackRequest( currentTrackName );
	}
	
	public TrackSearchResult previousTrackRequestPage( ) {
		// Update the offset parameter in the request uri to request the previous 10 tracks 
		searchOffset -= 10;
		return this.trackRequest( currentTrackName );
	}
}

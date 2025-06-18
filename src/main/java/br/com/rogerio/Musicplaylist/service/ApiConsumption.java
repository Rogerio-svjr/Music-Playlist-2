package br.com.rogerio.Musicplaylist.service;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpRequest.BodyPublishers;
import java.net.http.HttpResponse;
import java.net.http.HttpResponse.BodyHandlers;
import java.util.Base64;

import br.com.rogerio.Musicplaylist.entity.AccessToken;
import br.com.rogerio.Musicplaylist.entity.TrackSearchResult;

public class ApiConsumption {
	HttpClient client = HttpClient.newHttpClient();
	ConvertsData convertsData = new ConvertsData();

	private byte searchOffset = 0;
	private String currentTrackName = null;

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

		return convertsData.GetData( responsePOST.body(), AccessToken.class );
	}

	public TrackSearchResult trackRequest( String trackName ) {
		trackName = trackName.replace(" ", "-");
		currentTrackName = trackName;
		AccessToken token = this.tokenRequest();
		String accessToken = token.accessToken().replace(" ", "+");
		
		HttpRequest requestTrack = HttpRequest.newBuilder()
			.uri( URI.create( "https://api.spotify.com/v1/search?q=" + trackName + "&type=track&limit=10&offset=" + searchOffset ) )
			.header( "Authorization", "Bearer " + accessToken )
			.build();

		HttpResponse<String> responseGET = null;
		
		try {
			responseGET = client.send( requestTrack, HttpResponse.BodyHandlers.ofString() );
		} catch (IOException | InterruptedException e) {
			e.printStackTrace();
		}
		
		return convertsData.GetData( responseGET.body(), TrackSearchResult.class );
	}
	
	public TrackSearchResult nextTrackRequestPage( ) {
		searchOffset += 10;
		return this.trackRequest( currentTrackName );
	}

	public TrackSearchResult previousTrackRequestPage( ) {
		searchOffset -= 10;
		return this.trackRequest( currentTrackName );
	}
}

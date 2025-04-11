package br.com.rogerio.Musicplaylist;

import java.util.List;
import java.util.Scanner;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import br.com.rogerio.Musicplaylist.dto.MusicDTO;
import br.com.rogerio.Musicplaylist.entity.MusicEntity;
import br.com.rogerio.Musicplaylist.entity.TrackSearchResult;
import br.com.rogerio.Musicplaylist.service.ApiConsumption;
import br.com.rogerio.Musicplaylist.service.MusicService;

@SpringBootApplication
public class MusicplaylistApplication implements CommandLineRunner{
	@Autowired
	private MusicService musicService;
	public static void main(String[] args) {
		SpringApplication.run(MusicplaylistApplication.class, args);
	}

	@Override
	public void run( String... args ){
		var apiConsumption = new ApiConsumption();

		// Searches a track named "savior" and receives 10 results  
		TrackSearchResult trackResult = apiConsumption.trackRequest( "Kingslayer" );
		List<MusicEntity> items = trackResult.getPlaylist().getMusic();
		// Print the results
		try {
			items.forEach( item -> System.out.println( ( items.indexOf( item ) + 1 ) + " - " + item.getName() + 
											" - " + item.getArtists().get(0).getName() ) );
		} catch ( IndexOutOfBoundsException e ) {
			System.out.println( "Index out of bounds" );
		}
		
		Scanner keyboard = new Scanner(System.in);

		// Asks the user for the chosen track
		System.out.print( "\nChoose track: " );
		byte user = keyboard.nextByte();
		keyboard.close();
		MusicDTO track = new MusicDTO( items.get(user - 1) );

		// Save information on the database
		MusicDTO music = musicService.createMusic(track);
		System.out.println("\n" + music.getName() + "\nLiked: " + music.getLiked() + "\n");
		music.setLiked(!music.getLiked());
		music = musicService.updateMusic(music);
		System.out.println("\n" + music.getName() + "\nLiked: " + music.getLiked() + "\n");
	}

	public void testDTOEntityConstructors() {
		var apiConsumption = new ApiConsumption();

		// Searches a track named "savior" and receives 10 results  
		TrackSearchResult trackResult = apiConsumption.trackRequest( "Kingslayer" );
		List<MusicEntity> items = trackResult.getPlaylist().getMusic();
		// Print the results
		try {
			items.forEach( item -> System.out.println( ( items.indexOf( item ) + 1 ) + " - " + item.getName() + 
											" - " + item.getArtists().get(0).getName() ) );
		} catch ( IndexOutOfBoundsException e ) {
			System.out.println( "Index out of bounds" );
		}
		
		Scanner keyboard = new Scanner(System.in);

		// Asks the user for the chosen track
		System.out.print( "\nChoose track: " );
		byte user = keyboard.nextByte();
		keyboard.close();
		MusicDTO track = new MusicDTO(items.get( user - 1 ));
		// Print track information
		System.out.println( 
			"\nDTO Print:" +
			"\nName: " + track.getName() +
			"\nArtist: " + track.getArtistsNames() +
			"\nAlbum: " + track.getAlbum() +
			"\nDuration: " + track.getDuration_min() );
		
		MusicEntity trackEntity = new MusicEntity(track);
		System.out.println( 
			"\nEntity Print:" +
			"\nName: " + trackEntity.getName() +
			"\nArtist: " + trackEntity.getArtistsNames() +
			"\nAlbum: " + trackEntity.getAlbumName() +
			"\nDuration: " + trackEntity.getDuration_ms() + "\n" );
	}

	public void testTrackSearch() throws Exception 
  {
		var apiConsumption = new ApiConsumption();

		// Searches a track named "savior" and receives 10 results 
		TrackSearchResult trackResult = apiConsumption.trackRequest( "Savior" );
		List<MusicEntity> items = trackResult.getPlaylist().getMusic();
		// Print the results
		try {
			items.forEach( item -> System.out.println( item.getName() + " - " + item.getArtists().get(0).getName() ) );
		} catch ( IndexOutOfBoundsException e ) {
			System.out.println( "Index out of bounds" );
		}

		// Searches and prints the next 10 results
		trackResult = apiConsumption.nextTrackRequestPage();
		items = trackResult.getPlaylist().getMusic();
		System.out.println();
		try {
			items.forEach( item -> System.out.println( item.getName() + " - " + item.getArtists().get(0).getName() ) );
		} catch ( IndexOutOfBoundsException e ) {
			System.out.println( "Index out of bounds" );
		}

		// Searches previous 10 results again 
		trackResult = apiConsumption.previousTrackRequestPage();
		items = trackResult.getPlaylist().getMusic();
		System.out.println();
		try {
			items.forEach( item -> System.out.println( item.getName() + " - " + item.getArtists().get(0).getName() ) );
		} catch ( IndexOutOfBoundsException e ) {
			System.out.println( "Index out of bounds" );
		}
	}
}

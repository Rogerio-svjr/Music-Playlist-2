package br.com.rogerio.Musicplaylist;

import java.util.List;
import java.util.Scanner;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import br.com.rogerio.Musicplaylist.models.TrackItem;
import br.com.rogerio.Musicplaylist.models.TrackSearchResult;
import br.com.rogerio.Musicplaylist.service.ApiConsumption;

@SpringBootApplication
public class MusicplaylistApplication implements CommandLineRunner{
	public static void main(String[] args) {
		SpringApplication.run(MusicplaylistApplication.class, args);
	}

	@Override
	public void run( String... args ){
		var apiConsumption = new ApiConsumption();

		// Searches a track named "savior" and receives 10 results  
		TrackSearchResult trackResult = apiConsumption.trackRequest( "Savior" );
		List<TrackItem> items = trackResult.getTracks().getItems();
		// Print the results
		try {
			items.forEach( item -> System.out.println( ( items.indexOf( item ) + 1 ) + " - " + item.getName() + 
											" - " + item.getArtists().get(0).getName() ) );
		} catch ( IndexOutOfBoundsException e ) {
			System.out.println( "Index out of bounds" );
		}
		
		Scanner keyboard = new Scanner(System.in);

		// Asks the user for the chosen track
		System.out.println();
		System.out.print( "Choose track: " );
		byte user = keyboard.nextByte();
		TrackItem track = items.get( user - 1 );
		// Print track information
		System.out.println( 
			"\nName: " + track.getName() +
			"\nAtrist: " + track.getArtists().get( 0 ).getName() +
			"\nAlbum: " + track.getAlbum().getName() +
			"\nDuration: " + track.getDuration_min() );

		keyboard.close();
	}

	public void testTrackSearch() throws Exception 
  {
		var apiConsumption = new ApiConsumption();

		// Searches a track named "savior" and receives 10 results  
		TrackSearchResult trackResult = apiConsumption.trackRequest( "Savior" );
		List<TrackItem> items = trackResult.getTracks().getItems();
		// Print the results
		try {
			items.forEach( item -> System.out.println( item.getName() + " - " + item.getArtists().get(0).getName() ) );
		} catch ( IndexOutOfBoundsException e ) {
			System.out.println( "Index out of bounds" );
		}

		// Searches and prints the next 10 results
		trackResult = apiConsumption.nextTrackRequestPage();
		items = trackResult.getTracks().getItems();
		System.out.println();
		try {
			items.forEach( item -> System.out.println( item.getName() + " - " + item.getArtists().get(0).getName() ) );
		} catch ( IndexOutOfBoundsException e ) {
			System.out.println( "Index out of bounds" );
		}

		// Searches previous 10 results again 
		trackResult = apiConsumption.previousTrackRequestPage();
		items = trackResult.getTracks().getItems();
		System.out.println();
		try {
			items.forEach( item -> System.out.println( item.getName() + " - " + item.getArtists().get(0).getName() ) );
		} catch ( IndexOutOfBoundsException e ) {
			System.out.println( "Index out of bounds" );
		}
	}
}

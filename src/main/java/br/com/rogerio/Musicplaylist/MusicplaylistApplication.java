package br.com.rogerio.Musicplaylist;

import java.util.List;

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
	public void run(String... args) throws Exception 
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

		// Searches and prints for the next 10 results
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

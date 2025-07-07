package br.com.rogerio.Musicplaylist;

import java.util.List;
import java.util.Scanner;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import br.com.rogerio.Musicplaylist.dto.MusicDTO;
import br.com.rogerio.Musicplaylist.dto.PlaylistDTO;
import br.com.rogerio.Musicplaylist.entity.MusicEntity;
import br.com.rogerio.Musicplaylist.entity.TrackSearchResult;
import br.com.rogerio.Musicplaylist.service.spotifyRequest;
import br.com.rogerio.Musicplaylist.service.MusicService;
import br.com.rogerio.Musicplaylist.service.PlaylistService;

@SpringBootApplication
public class MusicplaylistApplication implements CommandLineRunner{
	@Autowired
	private MusicService musicService;
	@Autowired
	private PlaylistService playlistService;
	
	public static void main(String[] args) {
		SpringApplication.run(MusicplaylistApplication.class, args);
	}

	@Override
	public void run( String... args ){
		var apiConsumption = new spotifyRequest();

		// Searches a track named "savior" and receives 10 results  
		TrackSearchResult trackResult = apiConsumption.trackRequest( "Bleed it Out" );
		List<MusicEntity> items = trackResult.getPlaylist().getMusics();
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

		// TESTING CRUD OPERATIONS

		// Save music on the database (testing Create)
		MusicDTO music = musicService.createMusic(track);
		System.out.println("\nCREATE:\n" + music.getName() + "\nLiked: " + music.getLiked() + "\n");
		// Read musics from the database (testing Read) 
		PlaylistDTO playlist = new PlaylistDTO();
		try {
			playlist.setMusics( musicService.readAllMusic() );
		} catch (Exception e) {}
		System.out.println("\nREAD ALL:");
		playlist.listMusics();
		try {
			music = musicService.readMusicById( music.getId() );
		} catch (Exception e) {}
		System.out.println("\nREAD ONE:\n" + music.getName() + " - " + music.getArtistsNames() + "\n");
		// Modify musics on database (testing Update)
		music.setLiked(!music.getLiked());
		try {
			music = musicService.updateMusic(music);
		} catch (Exception e) {}
		System.out.println("\nUPDATE:\n" + music.getName() + "\nLiked: " + music.getLiked() + "\n");
		// Delete musics from database (testing Delete)
		Long id = music.getId();
		try {
			musicService.deleteMusic(id);
		} catch (Exception e) {}
		try {
			playlist.setMusics( musicService.readAllMusic() );
		} catch (Exception e) {}
		System.out.println("\nDELETE:");
		playlist.listMusics();
		
		// Save playlist on the database (testing create)
		PlaylistDTO rock = new PlaylistDTO();
		rock.setName("Rock");
		try{
			rock = playlistService.createPlaylist(rock);
		} catch(Exception e) {}
		System.out.println("\nCREATE:\n" + rock.getName() + "\n");
		// Read playlist from database
		List<PlaylistDTO> playlistList;
		try {
			playlistList = playlistService.readAllPlaylists();
			System.out.println("\nREAD ALL:");
			playlistList.forEach(list -> System.out.println(list.getName() + " - " + list.getMusics().size()));
			System.out.println();
		} catch (Exception e) {}
		// Update playlist from database
		playlist.addMusic(track);
		rock.setMusics(playlist.getMusics());
		rock.setName("Indie");
		rock = playlistService.updatePlaylist(rock);
		PlaylistDTO indie = playlistService.readPlaylistById(rock.getId());
		System.out.println("\nREAD ONE and UPDATE:\n" + indie.getName());
		indie.listMusics();
		// Delete playlist from database
		System.out.println("\nDELETE:");
		playlistService.deletePlaylist(indie.getId());
		try {
			playlistList = playlistService.readAllPlaylists();
			playlistList.forEach(list -> System.out.println(list.getName() + " - " + list.getMusics().size()));
		} catch (Exception e) {}
	}

	public void testDTOEntityMusicConstructors() {
		var apiConsumption = new spotifyRequest();

		// Searches a track named "savior" and receives 10 results  
		TrackSearchResult trackResult = apiConsumption.trackRequest( "Kingslayer" );
		List<MusicEntity> items = trackResult.getPlaylist().getMusics();
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
		var apiConsumption = new spotifyRequest();

		// Searches a track named "savior" and receives 10 results 
		TrackSearchResult trackResult = apiConsumption.trackRequest( "Savior" );
		List<MusicEntity> items = trackResult.getPlaylist().getMusics();
		// Print the results
		try {
			items.forEach( item -> System.out.println( item.getName() + " - " + item.getArtists().get(0).getName() ) );
		} catch ( IndexOutOfBoundsException e ) {
			System.out.println( "Index out of bounds" );
		}

		// Searches and prints the next 10 results
		trackResult = apiConsumption.nextTrackRequestPage();
		items = trackResult.getPlaylist().getMusics();
		System.out.println();
		try {
			items.forEach( item -> System.out.println( item.getName() + " - " + item.getArtists().get(0).getName() ) );
		} catch ( IndexOutOfBoundsException e ) {
			System.out.println( "Index out of bounds" );
		}

		// Searches previous 10 results again 
		trackResult = apiConsumption.previousTrackRequestPage();
		items = trackResult.getPlaylist().getMusics();
		System.out.println();
		try {
			items.forEach( item -> System.out.println( item.getName() + " - " + item.getArtists().get(0).getName() ) );
		} catch ( IndexOutOfBoundsException e ) {
			System.out.println( "Index out of bounds" );
		}
	}
}

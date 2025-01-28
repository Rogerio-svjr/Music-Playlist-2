package br.com.rogerio.Musicplaylist;

import java.util.List;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import br.com.rogerio.Musicplaylist.models.AccessToken;
import br.com.rogerio.Musicplaylist.models.TrackItem;
import br.com.rogerio.Musicplaylist.models.TrackSearchResult;
import br.com.rogerio.Musicplaylist.models.Tracks;
import br.com.rogerio.Musicplaylist.service.ApiConsumption;
import br.com.rogerio.Musicplaylist.service.ConvertsData;

@SpringBootApplication
public class MusicplaylistApplication implements CommandLineRunner{
	public static void main(String[] args) {
		SpringApplication.run(MusicplaylistApplication.class, args);
	}

	@Override
	public void run(String... args) throws Exception {
		var apiConsumption = new ApiConsumption();
		var json = apiConsumption.tokenRequest();
		//System.out.println(json);
		var convertsData = new ConvertsData();
		AccessToken token = convertsData.GetData(json, AccessToken.class);
		//System.out.println(token);
		json = apiConsumption.trackRequest("savior", token.accessToken());
		//System.out.println(json);
		TrackSearchResult trackResult = convertsData.GetData(json, TrackSearchResult.class);
		Tracks track = trackResult.getTracks();
		List<TrackItem> items = track.getItems();
		System.out.println(items.get(0).getName());
	}

}

package spotify.spotify;

import java.io.IOException;

import javax.swing.JLabel;
import javax.swing.JList;

import retrofit2.Call;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class ArtistThread extends Thread{
	private ArtistObject artist;
	private JList<Artist> artists;
	private String name;
	
	public ArtistThread(JList<Artist> artists, String name){
		this.artists = artists;
		this.name = name;
	}
	
	public void run(){
		Retrofit retrofit = new Retrofit.Builder().baseUrl("http://developer.echonest.com/api/v4/")
				.addConverterFactory(GsonConverterFactory.create()).build();
		SpotifyService service = retrofit.create(SpotifyService.class);
		Call<ArtistObject> call = service.searchArtist(name);
		
		Response<ArtistObject> response = null;
		try {
			response = call.execute();
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		artist = response.body();
		//artistName.setText(artist.getArtists());
		artists.setListData(artist.getArtists());
	}
}

package spotify.spotify;

import java.io.IOException;

import javax.swing.JList;

import retrofit2.Call;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class SimilarArtistThread extends Thread{

	private JList<Artist> similar;
	private String artist;
	
	public SimilarArtistThread(JList<Artist> similar, String artist){
		this.similar = similar;
		this.artist = artist;
	}
	
	public void run(){
		Retrofit retrofit = new Retrofit.Builder().baseUrl("http://developer.echonest.com/api/v4/")
				.addConverterFactory(GsonConverterFactory.create()).build();
		SpotifyService service = retrofit.create(SpotifyService.class);
		Call<SimilarArtists> call = service.getSimilar(artist);
		
		Response<SimilarArtists> response;
		try {
			response = call.execute();
			SimilarArtists obj = response.body();
			similar.setListData(obj.getArtists());
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
}

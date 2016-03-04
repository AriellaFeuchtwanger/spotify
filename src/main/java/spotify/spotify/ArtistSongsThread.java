package spotify.spotify;

import java.io.IOException;

import javax.swing.JList;

import retrofit2.Call;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class ArtistSongsThread extends Thread{
	private JList<Song> songs;
	private String name;
	
	public ArtistSongsThread(JList<Song> songs, String name){
		this.songs = songs;
		this.name = name;
	}
	
	public void run() {
		Retrofit retrofit = new Retrofit.Builder()
				.baseUrl("http://developer.echonest.com/api/v4/")
				.addConverterFactory(GsonConverterFactory.create()).build();
		SpotifyService service = retrofit.create(SpotifyService.class);
				Call<SongObject> call = service.getSimilarSongs(name);

		Response<SongObject> response = null;
		try {
			response = call.execute();
		} catch (IOException e) {
			e.printStackTrace();
		}
		SongObject obj = response.body();
		Song[] songs = obj.getSongs();
		// Song song = songs[0];
		this.songs.setListData(songs);

	}
}

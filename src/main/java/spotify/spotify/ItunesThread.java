package spotify.spotify;

import java.io.IOException;

import javax.naming.Context;

import retrofit2.Call;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import javafx.application.Application;
import javafx.embed.swing.JFXPanel;
import javafx.scene.media.*;

public class ItunesThread extends Thread {
	private String artist;
	private String songTitle;
	private MediaPlayer player;
	private Context context;

	public ItunesThread(String artist, String songTitle) {
		this.artist = artist.toLowerCase();
		this.songTitle = songTitle.toLowerCase();
	}

	public void run() {
		Retrofit retrofit = new Retrofit.Builder()
				.baseUrl("https://itunes.apple.com/")
				.addConverterFactory(GsonConverterFactory.create()).build();
		ItunesService service = retrofit.create(ItunesService.class);
		// String term = URLEncoder.encode("anthem lights just fall", "UTF-8");
		String term = artist + " " + songTitle;
		Call<ItunesObject> call = service.searchSongPreview(term);

		Response<ItunesObject> response = null;
		try {
			response = call.execute();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		ItunesObject obj = response.body();
		String url = obj.getPreviewURL();
		JFXPanel fxPanel = new JFXPanel();
		Media song = new Media(url);
		player = new MediaPlayer(song);
		// fxPanel.add(player);
		player.play();
	}
}

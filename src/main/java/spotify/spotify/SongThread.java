package spotify.spotify;

import java.awt.Container;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;

import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JList;

import retrofit2.Call;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class SongThread extends Thread {
	private String title;
	private String artist;
	private JList<Song> songList;
	private JList<Song> similar;

	/*
	 * public SongThread(String title, String artist, JLabel songLbl, JLabel
	 * artistLbl, JLabel imageLbl, JList<Song> songs) { this.title = title;
	 * this.artist = artist; this.songLbl = songLbl; this.artistLbl = artistLbl;
	 * this.imageLbl = imageLbl; this.songs = songs; }
	 */

	public SongThread(String title, String artist, JList<Song> songs,
			JList<Song> similar) {
		this.title = title;
		this.artist = artist;
		this.songList = songs;
		this.similar = similar;
	}

	public void run() {
		Retrofit retrofit = new Retrofit.Builder()
				.baseUrl("http://developer.echonest.com/api/v4/")
				.addConverterFactory(GsonConverterFactory.create()).build();
		SpotifyService service = retrofit.create(SpotifyService.class);
		Call<SongObject> call = service.searchSong(title, artist);

		Response<SongObject> response = null;
		try {
			response = call.execute();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		SongObject obj = response.body();
		Song[] songs = obj.getSongs();
		// Song song = songs[0];
		songList.setListData(songs);

		try {
			call = service.getSimilarSongs(songs[0].getArtist());
			response = call.execute();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch(ArrayIndexOutOfBoundsException e2){
			songs = new Song[1];
			songs[0] = new Song("Song not found", "Check  your spelling!");
			songList.setListData(songs);
		}

		obj = response.body();
		songs = obj.getSongs();
		similar.setListData(songs);
	}
}

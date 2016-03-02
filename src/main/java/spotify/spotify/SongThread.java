package spotify.spotify;

import java.awt.image.BufferedImage;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;

import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JLabel;

import retrofit2.Call;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class SongThread extends Thread{
	private String title;
	private String artist;
	private JLabel songLbl;
	private JLabel artistLbl;
	private JLabel imageLbl;
	
	public SongThread(String title, String artist, JLabel songLbl,
			JLabel artistLbl, JLabel imageLbl) {
		this.title = title;
		this.artist = artist;
		this.songLbl = songLbl;
		this.artistLbl = artistLbl;
		this.imageLbl = imageLbl;
	}

	public void run(){
		Retrofit retrofit = new Retrofit.Builder().baseUrl("http://developer.echonest.com/api/v4/")
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
		Song song = songs[0];
		songLbl.setText(song.getTitle());
		artistLbl.setText(song.getArtist());

		Track[] tracks = songs[0].getTracks();
		String imageURL = tracks[0].getRelease_image();
		URL track;
		try {
			track = new URL(imageURL);
			BufferedImage trackImage = ImageIO.read(track);
			imageLbl.setIcon(new ImageIcon(trackImage));
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}

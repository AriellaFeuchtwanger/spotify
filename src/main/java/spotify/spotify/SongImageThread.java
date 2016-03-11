package spotify.spotify;

import java.awt.image.BufferedImage;
import java.io.IOException;
import java.net.URL;

import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JLabel;

public class SongImageThread extends Thread{
	
	private String url;
	private JLabel imageLbl;
	public SongImageThread(String url, JLabel imageLbl){
		this.url = url;
		this.imageLbl = imageLbl;
	}
	
	public void start(){
		URL track;
		try {
			track = new URL(url);
			BufferedImage trackImage = ImageIO.read(track);
			imageLbl.setIcon(new ImageIcon(trackImage));
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}

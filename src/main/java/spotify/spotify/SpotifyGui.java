package spotify.spotify;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Container;
import java.awt.event.ActionEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.swing.AbstractAction;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.border.LineBorder;

import retrofit2.Call;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class SpotifyGui extends JFrame {

	private static final long serialVersionUID = 1L;
	private JPanel searchPanel, trackPanel, westPanel, eastPanel;
	private JLabel songLbl, artistLbl, imageLbl;
	private ImageIcon trackImage, defaultImage;
	private JTextField titleSearch, artistSearch;
	private JButton searchButton;
	
	private Color spotifyGreen; // #638c00

	public SpotifyGui() {
		setTitle("Spotify");
		setSize(800, 600);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

		Container container = getContentPane();
		container.setLayout(new BorderLayout());

		try {
			setIconImage(ImageIO.read(new File("spotify_icon.png")));
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		spotifyGreen = Color.decode("#638c00");
		defaultImage = new ImageIcon("defaultImage.png");

		// CENTER - track

		trackPanel = new JPanel(new BorderLayout());
		trackPanel.setBorder(new LineBorder(Color.BLACK));
		trackPanel.setBackground(spotifyGreen);
		songLbl = new JLabel("song");
		artistLbl = new JLabel("artist");
		imageLbl = new JLabel();
		//image.setSize(200, 200);
		songLbl.setBorder(new LineBorder(Color.BLACK));
		artistLbl.setBorder(new LineBorder(Color.BLACK));
		imageLbl.setBorder(new LineBorder(Color.BLACK));
		trackPanel.add(songLbl, BorderLayout.SOUTH);
		trackPanel.add(artistLbl, BorderLayout.NORTH);
		trackPanel.add(imageLbl, BorderLayout.CENTER);
		container.add(trackPanel, BorderLayout.CENTER);

		// NORTH - search
		searchPanel = new JPanel();
		searchPanel.setBackground(Color.BLACK);
		titleSearch = new JTextField("search song title                 ");
		artistSearch = new JTextField("search song artist               ");
		
		//clear search textFields on click
		titleSearch.addMouseListener(new MouseAdapter() {

			@Override
			public void mouseClicked(MouseEvent e){
				titleSearch.setText(" ");
			}
			
			
		});

		artistSearch.addMouseListener(new MouseAdapter() {

			@Override
			public void mouseClicked(MouseEvent e){
				artistSearch.setText(" ");
			}
			
			
		});

		searchButton = new JButton("SEARCH");
		searchButton.setBackground(spotifyGreen);

		searchPanel.add(titleSearch);
		searchPanel.add(artistSearch);
		searchPanel.add(searchButton);
		container.add(searchPanel, BorderLayout.NORTH);

		searchButton.addActionListener(new AbstractAction() {

			private static final long serialVersionUID = 1L;

			@Override
			public void actionPerformed(ActionEvent arg0) {

				// search song and get artist.

				Retrofit retrofit = new Retrofit.Builder().baseUrl("http://developer.echonest.com/api/v4/")
						.addConverterFactory(GsonConverterFactory.create()).build();
				SpotifyService service = retrofit.create(SpotifyService.class);
				Call<SongObject> call = service.searchSong(titleSearch.getText(), artistSearch.getText());

				Response<SongObject> response;

				try {
					response = call.execute();
					SongObject obj = response.body();
					
					Song[] songs = obj.getSongs();
					Song song = songs[0];
					songLbl.setText(song.getTitle());
					artistLbl.setText(song.getArtist());
					
					try{
					Track[] tracks = songs[0].getTracks();
					String imageURL = tracks[0].getRelease_image();
					trackImage = new ImageIcon(imageURL);
					imageLbl.setIcon(trackImage);
					
					//IMAGE IS NOT SETTING YET
					
					} catch (ArrayIndexOutOfBoundsException e){
						imageLbl.setText("image unavailable");
						//imageLbl.setIcon(defaultImage);
						
						//IMAGE IS NOT SETTING YET
					}

				} catch (ArrayIndexOutOfBoundsException e) {

					songLbl.setText("unknown song");
					artistLbl.setText("unknown artist");

				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}

			}

		});
		
		
		//RIGHT - 
		
		eastPanel = new JPanel();
		eastPanel.setBackground(spotifyGreen);
		eastPanel.setBorder(new LineBorder(Color.BLACK));
		container.add(eastPanel, BorderLayout.EAST);
		
		
		//LEFT - 
		westPanel = new JPanel();
		westPanel.setBackground(spotifyGreen);
		westPanel.setBorder(new LineBorder(Color.BLACK));
		container.add(westPanel, BorderLayout.WEST);
		
		
		
		
		

	} // end GUI

	private void setIcon() {
		// TODO Auto-generated method stub

	}

	public static void main(String[] args) {
		SpotifyGui gui = new SpotifyGui();
		gui.setVisible(true);
	}

}

package spotify.spotify;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Container;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.net.URL;

import javax.imageio.ImageIO;
import javax.swing.AbstractAction;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JList;
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
	private JList<Artist> artists;
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

		// NORTH - search
		searchPanel = new JPanel();
		searchPanel.setBackground(Color.BLACK);
		titleSearch = new JTextField("search song title                 ");
		artistSearch = new JTextField("search song artist               ");

		// clear search textFields on click
		titleSearch.addMouseListener(new MouseAdapter() {

			@Override
			public void mouseClicked(MouseEvent e) {
				titleSearch.setText(null);
			}

		});

		artistSearch.addMouseListener(new MouseAdapter() {

			@Override
			public void mouseClicked(MouseEvent e) {
				artistSearch.setText(null);
			}

		});

		searchButton = new JButton("SEARCH");
		searchButton.setBackground(spotifyGreen);

		searchPanel.add(titleSearch);
		searchPanel.add(artistSearch);
		searchPanel.add(searchButton);
		container.add(searchPanel, BorderLayout.NORTH);

		searchButton.addActionListener(new ActionListener() {

			private static final long serialVersionUID = 1L;

			@Override
			public void actionPerformed(ActionEvent arg0) {
				String title = titleSearch.getText();
				String artist = artistSearch.getText();
				
				if (title.equals("") || title.equals("search song title                 ")) {
					artists = new JList<Artist>();
					artists.setBackground(spotifyGreen);
					container.add(artists, BorderLayout.CENTER);
					new ArtistThread(artists, artist).start();
				} else {
					
						// CENTER - track
						Font font = new Font("Times New Roman", Font.PLAIN, 18);
						trackPanel = new JPanel(new BorderLayout());
						trackPanel.setBorder(new LineBorder(Color.BLACK));
						trackPanel.setBackground(spotifyGreen);
						songLbl = new JLabel("song");
						songLbl.setFont(font);
						artistLbl = new JLabel("artist");
						artistLbl.setFont(font);
						imageLbl = new JLabel(new ImageIcon("defaultImage.jpg"));
						songLbl.setBorder(new LineBorder(Color.BLACK));
						artistLbl.setBorder(new LineBorder(Color.BLACK));
						trackPanel.add(songLbl, BorderLayout.SOUTH);
						trackPanel.add(artistLbl, BorderLayout.NORTH);
						trackPanel.add(imageLbl, BorderLayout.CENTER);
						container.add(trackPanel, BorderLayout.CENTER);
						SongThread thread = new SongThread(title, artist, songLbl, artistLbl, imageLbl);
						thread.start();
				}
			}
		});

		// RIGHT -

		eastPanel = new JPanel();
		eastPanel.setBackground(spotifyGreen);
		eastPanel.setBorder(new LineBorder(Color.BLACK));
		container.add(eastPanel, BorderLayout.EAST);

		// LEFT -
		westPanel = new JPanel();
		westPanel.setBackground(spotifyGreen);
		westPanel.setBorder(new LineBorder(Color.BLACK));
		container.add(westPanel, BorderLayout.WEST);
		
		// DEFAULT CENTER
		JLabel defaultImage = new JLabel(new ImageIcon("bigspotify.png"));
		container.add(defaultImage, BorderLayout.CENTER);
		container.setBackground(spotifyGreen);

	}// end GUI

	public static void main(String[] args) {
		SpotifyGui gui = new SpotifyGui();
		gui.setVisible(true);
	}

}

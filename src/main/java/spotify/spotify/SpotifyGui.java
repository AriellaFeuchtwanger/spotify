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
import java.net.MalformedURLException;
import java.net.URL;

import javax.imageio.ImageIO;
import javax.swing.AbstractAction;
import javax.swing.BoxLayout;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.border.LineBorder;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

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
	private Container container;

	private Color spotifyGreen; // #638c00

	public SpotifyGui() {
		setTitle("Spotify");
		setSize(800, 600);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

		container = getContentPane();
		container.setLayout(new BorderLayout());

		try {
			setIconImage(ImageIO.read(new File("spotify_icon.png")));
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		spotifyGreen = Color.decode("#638c00");

		JLabel defaultImage = new JLabel(new ImageIcon("bigspotify.png"));
		container.add(defaultImage, BorderLayout.CENTER);
		container.setBackground(spotifyGreen);

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

				if (title.equals("")
						|| title.equals("search song title                 ")) {
					artists = new JList<Artist>();
					artists.setBackground(spotifyGreen);
					artists.addMouseListener(new MouseAdapter() {

						@Override
						public void mouseClicked(MouseEvent e) {
							if (e.getClickCount() == 2) {

								Artist artist = artists.getSelectedValue();
								setArtistInfo(artist);
							}
						}
					});
					container.add(artists, BorderLayout.CENTER);
					new ArtistThread(artists, artist).start();
				} else {
					if (artist.equals("")
							|| artist
									.equals("search song artist               ")) {
						artist = null;
					}
					// CENTER - track
					setUpTrack(title, artist);

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
	}// end GUI

	public static void main(String[] args) {
		SpotifyGui gui = new SpotifyGui();
		gui.setVisible(true);
	}

	private void setArtistInfo(Artist artist) {
		JPanel artistPanel = new JPanel();

		JPanel reviewPanel = new JPanel();
		reviewPanel.setLayout(new BoxLayout(reviewPanel, BoxLayout.Y_AXIS));
		ArtistReview[] reviews = artist.getReviews();

		for (ArtistReview review : reviews) {
			reviewPanel.add(new JLabel(review.getSummary()));
		}

		JPanel imagePanel = new JPanel();
		imagePanel.setLayout(new BoxLayout(imagePanel, BoxLayout.X_AXIS));

		ArtistImage[] images = artist.getImages();

		// for (int i = 0; i < 5; i++) {
		JLabel label = new JLabel();
		String imageURL = images[0].getURL();
		URL url;

		try {
			url = new URL(imageURL);
			BufferedImage artistImage = ImageIO.read(url);
			label.setIcon(new ImageIcon(artistImage));
		} catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		} catch (NullPointerException e2) {
			label.setIcon(new ImageIcon("defaultImage.jpg"));
		}
		imagePanel.add(label);
		// }

		artistPanel.add(imagePanel);
		artistPanel.add(reviewPanel);

		container.add(artistPanel, BorderLayout.CENTER);
		container.revalidate();
	}

	private void setUpTrack(String title, String artist) {

		JList<Song> songs = new JList<Song>();
		songs.setBackground(spotifyGreen);
		songs.addMouseListener(new MouseAdapter() {

			@Override
			public void mouseClicked(MouseEvent e) {
				if (e.getClickCount() == 2) {

					Song song = songs.getSelectedValue();
					setSongInfo(song);
				}
			}
		});
		container.add(songs, BorderLayout.CENTER);
		SongThread thread = new SongThread(title, artist, songs);
		thread.start();
		container.revalidate();
	}

	private void setSongInfo(Song song) {
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
		songLbl.setText(song.getTitle());
		artistLbl.setText(song.getArtist());

		Track[] tracks = song.getTracks();
		if (tracks.length > 0) {
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
		
		container.revalidate();
	}
}

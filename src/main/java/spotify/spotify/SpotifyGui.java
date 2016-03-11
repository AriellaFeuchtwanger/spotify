package spotify.spotify;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Container;
import java.awt.Dimension;
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
import javax.swing.BoxLayout;
import javax.swing.DefaultListModel;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.ListModel;
import javax.swing.SwingConstants;
import javax.swing.border.LineBorder;

import javafx.embed.swing.JFXPanel;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import retrofit2.Call;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class SpotifyGui extends JFrame {

	private static final long serialVersionUID = 1L;
	private JPanel searchPanel, trackPanel, westPanel, eastPanel;
	private JLabel songLbl, artistLbl, imageLbl;
	private JList<Artist> artists, similar;
	private JList<String> recentList;
	private JList<Song> artistSongs, songs;
	private DefaultListModel<String> recentModel;
	private JTextField titleSearch, artistSearch;
	private JButton searchButton;
	private Container container;
	private Component currCenter;
	private MediaPlayer player;
	private Color spotifyGreen; // #638c00

	public SpotifyGui() {
		setTitle("Spotify");
		setSize(850, 600);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

		container = getContentPane();
		container.setLayout(new BorderLayout());

		try {
			setIconImage(ImageIO.read(getClass().getResource("/spotifyIcon.png")));
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		spotifyGreen = Color.decode("#638c00");

		JLabel defaultImage = new JLabel(new ImageIcon(getClass().getResource("/bigspotify.png")));
		container.add(defaultImage, BorderLayout.CENTER);
		container.setBackground(spotifyGreen);
		currCenter = defaultImage;

		setUpWest(); // recent songs
		setUpEast(); // more songs from artist
		setUpNorth(); // search panel

	}// end GUI

	private void searchArtists(String artist) {
		artists = new JList<Artist>();
		artists.setBackground(spotifyGreen);
		artists.setForeground(Color.BLACK);
		artists.setFixedCellWidth(300);
		artists.addMouseListener(new MouseAdapter() {

			@Override
			public void mouseClicked(MouseEvent e) {
				if (e.getClickCount() == 2) {
					Artist artist = artists.getSelectedValue();
					recentList.add(new JLabel(artist.toString())); // add to
																	// recents
					setArtistInfo(artist);
				}
			}
		});
		JScrollPane scroll = new JScrollPane(artists);
		resetContainer(scroll);
		currCenter = scroll;
		
		setSimilar();
		
		new ArtistThread(artists, artist).start();
		new SimilarArtistThread(similar, artist).start();
	}

	private void setArtistInfo(Artist artist) {
		JPanel artistPanel = new JPanel();
		artistPanel.setLayout(new BoxLayout(artistPanel, BoxLayout.Y_AXIS));
		artistPanel.setBackground(spotifyGreen);

		JLabel artistName = new JLabel(artist.getName());
		artistName.setBackground(spotifyGreen);

		setSimilar();
		
		artistSongs = new JList<Song>();
		artistSongs.setBackground(spotifyGreen);
		artistSongs.setForeground(Color.BLACK);
		artistSongs.setFixedCellWidth(450);
		artistSongs.addMouseListener(new MouseAdapter() {

			@Override
			public void mouseClicked(MouseEvent e) {
				if (e.getClickCount() == 2) {
					Song song = artistSongs.getSelectedValue();
					searchTrack(song.getTitle(), song.getArtist());
				}
			}
		});
		JScrollPane scroll = new JScrollPane(artistSongs);
		new ArtistSongsThread(artistSongs, artist.getName()).start();

		artistPanel.add(artistName);
		artistPanel.add(scroll);

		resetContainer(artistPanel);
		currCenter = artistPanel;
		container.revalidate();

	}

	// Set up the songs
	private void searchTrack(String title, String artist) {
		songs = new JList<Song>();
		songs.setBackground(spotifyGreen);
		songs.setForeground(Color.BLACK);
		songs.addMouseListener(new MouseAdapter() {

			@Override
			public void mouseClicked(MouseEvent e) {
				if (e.getClickCount() == 2) {

					Song song = songs.getSelectedValue();
					setSongInfo(song);
					recentList.add(new JLabel(song.toString()));
					// MediaPlayer player = null;
					// new ItunesThread(song.getArtist(),
					// song.getTitle()).start();
					Retrofit retrofit = new Retrofit.Builder().baseUrl("https://itunes.apple.com/")
							.addConverterFactory(GsonConverterFactory.create()).build();
					ItunesService service = retrofit.create(ItunesService.class);
					// String term = URLEncoder.encode("anthem lights just
					// fall", "UTF-8");
					String term = song.getArtist().toLowerCase() + " " + song.getTitle().toLowerCase();
					Call<ItunesObject> call = service.searchSongPreview(term);

					Response<ItunesObject> response = null;
					try {
						response = call.execute();
					} catch (IOException ee) {
						// TODO Auto-generated catch block
						ee.printStackTrace();
					}

					ItunesObject obj = response.body();
					String url = obj.getPreviewURL();
					JFXPanel fxPanel = new JFXPanel();
					Media song2 = new Media(url);
					//new ItunesThread(song.getArtist(), song.getTitle(), song2).start();
					player = new MediaPlayer(song2);
					player.play();
				}
			}
		});
		songs.setFixedCellWidth(300);
		JScrollPane scroll = new JScrollPane(songs);
		resetContainer(scroll);
		currCenter = scroll;
		JList<Song> similar = new JList<Song>();
		SongThread thread = new SongThread(title, artist, songs, similar);
		try{
		thread.start();
		} catch (ArrayIndexOutOfBoundsException e){
			songs.add(new JLabel("No songs found"));
		}
		container.revalidate();
	}

	private void setSongInfo(Song song) {
		Font font = new Font("Times New Roman", Font.PLAIN, 18);
		trackPanel = new JPanel(new BorderLayout());
		trackPanel.setBackground(spotifyGreen);
		songLbl = new JLabel("song");
		songLbl.setFont(font);
		songLbl.setForeground(Color.BLACK);
		songLbl.setHorizontalAlignment(SwingConstants.CENTER);
		artistLbl = new JLabel("artist");
		artistLbl.setFont(font);
		artistLbl.setForeground(Color.BLACK);
		artistLbl.setHorizontalAlignment(SwingConstants.CENTER);
		imageLbl = new JLabel(new ImageIcon(getClass().getResource("/defaultImage.jpg")));
		songLbl.setBorder(new LineBorder(Color.BLACK));
		artistLbl.setBorder(new LineBorder(Color.BLACK));
		trackPanel.add(songLbl, BorderLayout.SOUTH);
		trackPanel.add(artistLbl, BorderLayout.NORTH);
		trackPanel.add(imageLbl, BorderLayout.CENTER);

		songLbl.setText(song.getTitle());
		artistLbl.setText(song.getArtist());

		Track[] tracks = song.getTracks();
		if (tracks.length > 0) {
			String imageURL = tracks[0].getRelease_image();
			new SongImageThread(imageURL, imageLbl).start();
		}

		resetContainer(trackPanel);
		currCenter = trackPanel;
		container.revalidate();
	}

	private void resetContainer(Component c) {
		container.remove(currCenter);
		container.add(c, BorderLayout.CENTER);
	}

	private void setUpEast() {
		eastPanel = new JPanel();
		eastPanel.setBackground(spotifyGreen);
		eastPanel.setBorder(new LineBorder(Color.BLACK));
		eastPanel.setPreferredSize(new Dimension(180, getHeight()));
		// eastPanel.setLayout(new FlowLayout());
		similar = new JList<Artist>();
		similar.setFixedCellWidth(150);
		similar.setBackground(spotifyGreen);
		similar.setForeground(Color.BLACK);
		

		// eastPanel.add(new JLabel("SIMILAR ARTISTS"));
		eastPanel.add(new JLabel(new ImageIcon(getClass().getResource("/similarArtists.png"))));
		//setSimilar();
		eastPanel.add(similar);
		// eastPanel.setAlignmentX(LEFT_ALIGNMENT);
		container.add(eastPanel, BorderLayout.EAST);
	}

	private void setUpWest() {
		westPanel = new JPanel();
		westPanel.setPreferredSize(new Dimension(180, getHeight()));
		westPanel.setBackground(spotifyGreen);
		westPanel.setBorder(new LineBorder(Color.BLACK));
		//westPanel.setLayout(new BoxLayout(westPanel, BoxLayout.Y_AXIS));
		recentModel = new DefaultListModel<String>();
		recentList = new JList<String>(recentModel);
		
		recentList.setBackground(spotifyGreen);
		recentList.setForeground(Color.BLACK);
		// recentModel.addElement("RECENT SEARCHES");
		recentList.setFixedCellWidth(150);
		westPanel.add(new JLabel(new ImageIcon(getClass().getResource("/recentSearches.png"))));
		//recentList.add(new JLabel(new ImageIcon("recentSearches.png")));
		westPanel.add(recentList);
		container.add(westPanel, BorderLayout.WEST);
	}

	private void setUpNorth() {
		// NORTH - search
		searchPanel = new JPanel();
		searchPanel.setBackground(Color.BLACK);
		titleSearch = new JTextField("search song title                 ");
		artistSearch = new JTextField("search song artist               ");

		titleSearch.setPreferredSize(new Dimension(200, 25));
		artistSearch.setPreferredSize(new Dimension(200, 25));
		titleSearch.setMaximumSize(new Dimension(400, 25));
		artistSearch.setMaximumSize(new Dimension(400, 25));
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

		searchButton = new JButton("SEARCH ");
		searchButton.setForeground(Color.WHITE);
		searchButton.setIcon(new ImageIcon(getClass().getResource("/searchIcon.jpeg")));
		searchButton.setBorder(new LineBorder(spotifyGreen));
		searchButton.setBackground(spotifyGreen);
		searchPanel.add(titleSearch);
		searchPanel.add(artistSearch);
		searchPanel.add(searchButton);
		container.add(searchPanel, BorderLayout.NORTH);

		searchButton.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent arg0) {
				similar = new JList<Artist>();
				String title = titleSearch.getText();
				String artist = artistSearch.getText();

				if (title.equals("") || title.equals("search song title                 ")) {
					searchArtists(artist);
					recentModel.addElement(artist);
				} else {
					if (artist.equals("") || artist.equals("search song artist               ")) {
						artist = null;
						//recentList.add(new JLabel(artist));
						recentModel.addElement(title);
					} else{
						//recentList.add(new JLabel(artist + " " + title));
						recentModel.addElement(artist + " - " + title);
					}
					// CENTER - track
					searchTrack(title, artist);

				}
			}
		});
	}

	public void setSimilar(){
		eastPanel.remove(similar);
		container.revalidate();
		similar = new JList<Artist>();
		similar.setFixedCellWidth(150);
		similar.setBackground(spotifyGreen);
		similar.addMouseListener(new MouseAdapter() {

			@Override
			public void mouseClicked(MouseEvent e) {
				if (e.getClickCount() == 2) {
					Artist artist = similar.getSelectedValue();
					searchArtists(artist.getName());
				}
			}
		});
		eastPanel.add(similar);
	}
	
	public static void main(String[] args) {
		SpotifyGui gui = new SpotifyGui();
		gui.setVisible(true);
	}

}

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
import javax.swing.GroupLayout.Alignment;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.border.LineBorder;

public class SpotifyGui extends JFrame {

	private static final long serialVersionUID = 1L;
	private JPanel searchPanel, trackPanel, westPanel, eastPanel;
	private JLabel songLbl, artistLbl, imageLbl;
	private JList<Artist> artists, similar;
	private JList<String> recentList, moreSongsList;
	private JList<Song> artistSongs;
	private DefaultListModel<String> recentModel, moreSongsModel;
	private JTextField titleSearch, artistSearch;
	private JButton searchButton;
	private Container container;
	private Component currCenter;

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
		currCenter = defaultImage;

		setUpWest(); // recent songs
		setUpEast(); // more songs from artist
		setUpNorth(); // search panel

	}// end GUI

	private void searchArtists(String artist) {
		artists = new JList<Artist>();
		artists.setBackground(spotifyGreen);
		artists.addMouseListener(new MouseAdapter() {

			@Override
			public void mouseClicked(MouseEvent e) {
				if (e.getClickCount() == 2) {
					Artist artist = artists.getSelectedValue();
					recentModel.addElement(artist.toString()); // add to recents
					setArtistSongs(artist); // add to artist's other songs
					setArtistInfo(artist);
				}
			}
		});

		resetContainer(artists);
		currCenter = artists;
		new ArtistThread(artists, artist).start();
		new SimilarArtistThread(similar, artist).start();

	}

	private void setArtistInfo(Artist artist) {
		JPanel artistPanel = new JPanel();
		artistPanel.setBackground(spotifyGreen);
		
		JLabel artistName = new JLabel(artist.getName());
		artistName.setBackground(spotifyGreen);
		
		artistSongs = new JList<Song>();
		artistSongs.setBackground(spotifyGreen);
		artistSongs.setFixedCellWidth(450);
		artistSongs.addMouseListener(new MouseAdapter() {

			@Override
			public void mouseClicked(MouseEvent e) {
				if (e.getClickCount() == 2) {
					Song song = artistSongs.getSelectedValue();
					setUpTrack(song.getTitle(), song.getArtist());
				}
			}
		});
		new ArtistSongsThread(artistSongs, artist.getName()).start();
		
		artistPanel.add(artistName);
		artistPanel.add(artistSongs);

		resetContainer(artistPanel);
		currCenter = artistPanel;
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
					recentModel.addElement(song.toString());

				}
			}
		});
		resetContainer(songs);
		currCenter = songs;
		JList<Song> similar = new JList<Song>();
		SongThread thread = new SongThread(title, artist, songs, similar);
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
		eastPanel.setLayout(new BoxLayout(eastPanel, BoxLayout.Y_AXIS));
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

		// moreSongsModel = new DefaultListModel<String>();
		// moreSongsList = new JList<String>(moreSongsModel);

		// eastPanel.add(new JLabel("More songs by artist:"));
		// eastPanel.add(moreSongsList);

		eastPanel.add(new JLabel("SIMILAR ARTISTS"));
		eastPanel.add(similar);
		container.add(eastPanel, BorderLayout.EAST);
	}

	private void setArtistSongs(Artist artist) {

		// get more songs from artist
		// http://developer.echonest.com/api/v4/song/search?api_key=FILDTEOIK2HBORODV&artist=artist
		// set moreSongsModel with songs

		// moreSongsModel.addElement();

	}

	private void setUpWest() {
		westPanel = new JPanel();
		westPanel.setBackground(spotifyGreen);
		westPanel.setBorder(new LineBorder(Color.BLACK));
		recentModel = new DefaultListModel<String>();
		recentList = new JList<String>(recentModel);
		recentList.setBackground(spotifyGreen);
		recentModel.addElement("RECENT SEARCHES");
		recentList.setFixedCellWidth(150);
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

		searchButton = new JButton("SEARCH");
		searchButton.setBackground(spotifyGreen);

		searchPanel.add(titleSearch);
		searchPanel.add(artistSearch);
		searchPanel.add(searchButton);
		container.add(searchPanel, BorderLayout.NORTH);

		searchButton.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent arg0) {
				String title = titleSearch.getText();
				String artist = artistSearch.getText();

				if (title.equals("")
						|| title.equals("search song title                 ")) {
					searchArtists(artist);
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
	}

	public static void main(String[] args) {
		SpotifyGui gui = new SpotifyGui();
		gui.setVisible(true);
	}

}

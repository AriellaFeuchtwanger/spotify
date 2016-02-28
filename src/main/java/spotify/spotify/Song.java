package spotify.spotify;

public class Song {
	private String title;
	private String artist_name;
	private Track[] tracks;
	private String artist_id;
	
	public void setTitle(String title) {
		this.title = title;
	}
	public void setArtist_name(String artist_name) {
		this.artist_name = artist_name;
	}
	public void setTracks(Track[] tracks) {
		this.tracks = tracks;
	}
	public void setArtist_id(String artist_id) {
		this.artist_id = artist_id;
	}
	
	
}

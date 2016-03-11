package spotify.spotify;

public class Song {
	private String title;
	private String artist_name;
	private Track[] tracks;
	private String artist_id;

	public Song(String title, String artist){
		this.title = title;
		this.artist_name = artist;
	}
	
	public String getArtist() {
		return artist_name;
	}
	
	public String getTitle(){
		return title;
	}
	
	public Track[] getTracks(){
		return tracks;
	}
	
	public String getArtistId(){
		return artist_id;
	}
	
	public String toString(){
		return title + " - " + artist_name;
	}
}

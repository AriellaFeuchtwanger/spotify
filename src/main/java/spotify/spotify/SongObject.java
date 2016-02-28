package spotify.spotify;

public class SongObject {
	private SongResponse response;
	
	public Song[] getSongs(){
		return response.getSongs();
	}
}

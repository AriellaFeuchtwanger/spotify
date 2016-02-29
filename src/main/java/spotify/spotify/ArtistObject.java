package spotify.spotify;

public class ArtistObject {
	private ArtistResponse response;
	
	public Artist[] getArtists(){
		return response.getArtists();
	}
}

package spotify.spotify;

public class SimilarArtists {
	private SimilarArtistResponse response;
	
	public Artist[] getArtists(){
		return response.getArtists();
	}
}

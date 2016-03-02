package spotify.spotify;

public class Artist {
	private String name;
	private ArtistReview[] reviews;
	private ArtistImage[] images;
	private String id;
	private ArtistLocation artist_location;
	
	public String getName() {
		return name;
	}
	public ArtistReview[] getReviews() {
		return reviews;
	}
	public ArtistImage[] getImages() {
		return images;
	}
	public String getId() {
		return id;
	}
	public ArtistLocation getArtist_location() {
		return artist_location;
	}
	
	public String toString(){
		return name;
	}
}

package spotify.spotify;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface SpotifyService {

	@GET("artist/search?api_key=VMDU6RQBYQKV6N0RR&format=json&bucket=id:7digital-US&bucket=images&bucket=artist_location&bucket=reviews")
	Call<ArtistObject> searchArtist(@Query("name") String name);
	
	@GET("song/search?api_key=VMDU6RQBYQKV6N0RR&format=json&&bucket=id:7digital-US&bucket=tracks")
	Call<SongObject> searchSong(@Query("title") String title, @Query("artist") String artist);
}

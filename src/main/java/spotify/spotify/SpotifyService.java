package spotify.spotify;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface SpotifyService {

	@GET("artist/search?api_key=VMDU6RQBYQKV6N0RR&format=json&results=40&bucket=id:7digital-US&bucket=images&bucket=artist_location&bucket=reviews")
	Call<ArtistObject> searchArtist(@Query("name") String name);

	@GET("song/search?api_key=VMDU6RQBYQKV6N0RR&format=json&results=40&bucket=id:7digital-US&bucket=tracks&sort=song_hotttnesss-desc")
	Call<SongObject> searchSong(@Query("title") String title, @Query("artist") String artist);

	@GET("artist/similar?api_key=VMDU6RQBYQKV6N0RR&format=json&results=40")
	Call<SimilarArtists> getSimilar(@Query("name") String name);

	@GET("song/search?api_key=VMDU6RQBYQKV6N0RR&sort=song_hotttnesss-desc&results=40")
	Call<SongObject> getSimilarSongs(@Query("artist") String artist);
}

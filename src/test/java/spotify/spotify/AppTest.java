package spotify.spotify;

import java.io.IOException;

import org.junit.Assert;
import org.junit.Test;

import retrofit2.Call;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class AppTest {

	@Test
	public void testArtist() throws IOException {
		Retrofit retrofit = new Retrofit.Builder().baseUrl("http://developer.echonest.com/api/v4/")
				.addConverterFactory(GsonConverterFactory.create()).build();
		SpotifyService service = retrofit.create(SpotifyService.class);
		Call<ArtistObject> call = service.searchArtist("anthem lights");

		Response<ArtistObject> response = call.execute();

		ArtistObject obj = response.body();

		Assert.assertNotNull(obj);
	}

	@Test
	public void testSong() throws IOException {
		Retrofit retrofit = new Retrofit.Builder().baseUrl("http://developer.echonest.com/api/v4/")
				.addConverterFactory(GsonConverterFactory.create()).build();
		SpotifyService service = retrofit.create(SpotifyService.class);
		Call<SongObject> call = service.searchSong("fight song", "rachel platten");

		Response<SongObject> response = call.execute();

		SongObject obj = response.body();

		Assert.assertNotNull(obj);
	}

	@Test
	public void testSimilar() throws IOException {
		Retrofit retrofit = new Retrofit.Builder().baseUrl("http://developer.echonest.com/api/v4/")
				.addConverterFactory(GsonConverterFactory.create()).build();
		SpotifyService service = retrofit.create(SpotifyService.class);
		Call<SimilarArtists> call = service.getSimilar("one direction");

		Response<SimilarArtists> response = call.execute();

		SimilarArtists obj = response.body();

		Assert.assertNotNull(obj.getArtists());
	}

	@Test
	public void testResults() throws IOException {
		Retrofit retrofit = new Retrofit.Builder().baseUrl("https://itunes.apple.com/")
				.addConverterFactory(GsonConverterFactory.create()).build();
		ItunesService service = retrofit.create(ItunesService.class);
		// String term = URLEncoder.encode("anthem lights just fall", "UTF-8");
		String term = "anthem lights just fall";
		Call<ItunesObject> call = service.searchSongPreview(term);

		Response<ItunesObject> response = call.execute();

		ItunesObject obj = response.body();

		Assert.assertNotNull(obj.getPreviewURL());
	}

}
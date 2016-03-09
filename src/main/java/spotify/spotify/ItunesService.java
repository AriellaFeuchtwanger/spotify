package spotify.spotify;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface ItunesService {
	
	@GET("search?media=music&entity=musicTrack")
	Call<ItunesObject> searchSongPreview(@Query("term") String term);
	
}

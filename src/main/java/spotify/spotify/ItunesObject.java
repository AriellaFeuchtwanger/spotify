package spotify.spotify;

public class ItunesObject {
	private ItunesResult[] results;

	public String getPreviewURL() {
		return results[0].getPreviewURL();
	}
	
	public String getName(){
		return results[0].getName();
	}

}

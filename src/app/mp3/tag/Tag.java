package app.mp3.tag;

/**
 * A very basic abstraction for an mp3 file's ID3 tag. Provides information for 
 * just the attributes that are of some concern.
 * @author gkats
 */
public interface Tag {

	public String getArtist();
	public String getAlbum();
	public String getYear();
	public String getTrack();
	public String getTitle();
	public boolean hasEnoughAttributes();
	
}
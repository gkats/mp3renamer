package app.mp3.tag;

import static org.apache.commons.lang3.StringUtils.isEmpty;

public class BasicAtrrsTag implements Tag {

	private String album;
	private String artist;
	private String title;
	private String track;
	private String year;
	
	public BasicAtrrsTag(String album, String artist, String title,
			String track, String year) {
		
		this.album = album;
		this.artist = artist;
		this.title = title;
		this.track = track;
		this.year = year;
	}

	@Override
	public String getAlbum() {
		return album;
	}

	@Override
	public String getArtist() {
		return artist;
	}

	@Override
	public String getTitle() {
		return title;
	}

	@Override
	public String getTrack() {
		return track;
	}

	@Override
	public String getYear() {
		return year;
	}

	@Override
	public boolean hasEnoughAttributes() {
		return !(isEmpty(title) || isEmpty(artist) || isEmpty(album));
	}
	
}
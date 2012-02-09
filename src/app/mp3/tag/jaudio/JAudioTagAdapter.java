package app.mp3.tag.jaudio;

import static org.apache.commons.lang3.StringUtils.isEmpty;

import org.jaudiotagger.tag.FieldKey;

import app.mp3.tag.Tag;

public class JAudioTagAdapter implements Tag {

	private org.jaudiotagger.tag.Tag tag;
	
	public JAudioTagAdapter(org.jaudiotagger.tag.Tag tag) {
		this.tag = tag;
	}
	
	@Override
	public String getAlbum() {
		return tag.getFirst(FieldKey.ALBUM);
	}

	@Override
	public String getArtist() {
		return tag.getFirst(FieldKey.ARTIST);
	}

	@Override
	public String getTitle() {
		return tag.getFirst(FieldKey.TITLE);
	}

	@Override
	public String getTrack() {
		return tag.getFirst(FieldKey.TRACK);
	}

	@Override
	public String getYear() {
		String y = tag.getFirst(FieldKey.ORIGINAL_YEAR);
		return isEmpty(y) ? tag.getFirst(FieldKey.YEAR) : y;
	}

	@Override
	public boolean hasEnoughAttributes() {
		return tag != null && !(isEmpty(tag.getFirst(FieldKey.TITLE)) 
				|| isEmpty(tag.getFirst(FieldKey.ARTIST)) 
				|| isEmpty(tag.getFirst(FieldKey.ALBUM)));
	}

}
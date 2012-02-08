package app.mp3.tag.jaudio;

import java.io.File;
import java.io.IOException;

import org.jaudiotagger.audio.exceptions.CannotWriteException;
import org.jaudiotagger.audio.exceptions.InvalidAudioFrameException;
import org.jaudiotagger.audio.exceptions.ReadOnlyFileException;
import org.jaudiotagger.audio.mp3.MP3File;
import org.jaudiotagger.tag.FieldKey;
import org.jaudiotagger.tag.TagException;

import app.log.Logger;
import app.log.Severity;
import app.mp3.tag.Tag;
import app.mp3.tag.TagWriter;

public class JAudioTagWriter implements TagWriter {

	public void writeTag(File file, Tag newTag) {
		try {
			MP3File mp3 = new MP3File(file);
			org.jaudiotagger.tag.Tag tag = mp3.getTag();
			if (tag != null) {
				tag.setField(FieldKey.ARTIST, newTag.getArtist());
				tag.setField(FieldKey.ALBUM, newTag.getAlbum());
				tag.setField(FieldKey.YEAR, newTag.getYear());
				tag.setField(FieldKey.TRACK, newTag.getTrack());
				tag.setField(FieldKey.TITLE, newTag.getTitle());
				mp3.commit();
			}
		} catch (IOException e) {
			Logger.INSTANCE.log(Severity.ERROR, "Could not read file " 
					+ file.getPath());
		} catch (TagException e) {
			Logger.INSTANCE.log(Severity.ERROR, "File " + file.getPath() 
					+ " has an invalid tag.");
		} catch (ReadOnlyFileException e) {
			Logger.INSTANCE.log(Severity.ERROR, "File " + file.getPath() 
					+ " is read-only.");
		} catch (InvalidAudioFrameException e) {
			Logger.INSTANCE.log(Severity.ERROR, "File " + file.getPath() 
					+ " contains an invalid audio frame.");
		} catch (CannotWriteException e) {
			Logger.INSTANCE.log(Severity.ERROR, "Cannot write file " 
					+ file.getPath());
		}
	}
}
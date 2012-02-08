package app.mp3.tag.jaudio;

import java.io.File;
import java.io.IOException;

import org.jaudiotagger.audio.exceptions.InvalidAudioFrameException;
import org.jaudiotagger.audio.exceptions.ReadOnlyFileException;
import org.jaudiotagger.audio.mp3.MP3File;
import org.jaudiotagger.tag.TagException;

import app.log.Logger;
import app.log.Severity;
import app.mp3.tag.Tag;
import app.mp3.tag.TagRetriever;

public class JAudioTagRetriever implements TagRetriever {

	@Override
	public Tag getTag(File file) {
		try {
			MP3File mp3 = new MP3File(file);
			return new JAudioTagAdapter(mp3.getTag());
		}
		catch (InvalidAudioFrameException e) {
			Logger.INSTANCE.log(Severity.ERROR, "File " + file.getPath() 
					+ " contains an invalid audio frame.");
		}
		catch (ReadOnlyFileException e) {
			Logger.INSTANCE.log(Severity.ERROR, "File " + file.getPath() 
					+ " is read-only.");
		}
		catch (TagException e) {
			Logger.INSTANCE.log(Severity.ERROR, "File " + file.getPath() 
					+ " has an invalid tag.");
		}
		catch (IOException e) {
			Logger.INSTANCE.log(Severity.ERROR, "Could not read file " 
					+ file.getPath());
		}
		return null;
	}

}
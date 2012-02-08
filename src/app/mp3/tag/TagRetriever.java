package app.mp3.tag;

import java.io.File;

public interface TagRetriever {

	public Tag getTag(File mp3);
}
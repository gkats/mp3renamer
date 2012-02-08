package app.mp3.tag;

import java.io.File;

public interface TagWriter {

	public void writeTag(File file, Tag tag);
}
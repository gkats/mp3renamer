package app.mp3;

import java.io.File;
import java.util.List;
import java.util.Map;

import app.log.Logger;
import app.log.Severity;
import app.mp3.tag.BasicAtrrsTag;
import app.mp3.tag.TagWriter;

/**
 * Renames mp3 files' ID3 tag attributes. The attributes written are artist, 
 * album, year, track and title. The attribute values are read from the filename 
 * and the containing folder name. Every file is assumed to be part of the 
 * following hierarchy:<p>
 * <code>.../&lt;artist&gt;/(&lt;year&gt;) &lt;album&gt;/&lt;track&gt;. &lt;title&gt;</code>
 * <p>
 * Possible exceptions are multi-disc albums for which the disc folder is 
 * directly one level up the from the mp3 file.
 * @author gkats
 */
public class Mp3TagRenamer {

	private TagWriter writer;
	private int writtenTags = 0;
	
	/**
	 * Writes all the ID3 tags in the current mp3 file listing.
	 * @param mp3Listings all the mp3 files whose tags are to be renamed, 
	 * grouped by parent folder
	 */
	public void renameTags(Map<String, List<File>> mp3Listings) {
		for (String dir : mp3Listings.keySet()) {
			List<File> mp3s = mp3Listings.get(dir);
			for (File mp3 : mp3s) {
				writeTag(mp3);
			}
		}
	}
	
	/**
	 * Writes an mp3 file's ID3 tag attributes. The attributes written are 
	 * artist, album, year, track and title.
	 * @param mp3 the file whose ID3 tag is to be written
	 */
	private void writeTag(File mp3) {
		File albumFolder = findAlbumFolder(mp3);
		if (albumFolder != null) {
			// artist - album information
			String albumName = albumFolder.getName();
			String year = albumName.substring(1, 5);
			String album = albumName.substring(albumName.indexOf(')') + 2);
			String artist = albumFolder.getParentFile().getName();
			
			// track information
			String name = mp3.getName();
			String track = name.substring(0, 2);
			String title = 
				name.substring(4, name.length() - Mp3Lister.MP3_EXTENSION.length());
			
			writer.writeTag(mp3, 
					new BasicAtrrsTag(album, artist, title, track, year));
			
			writtenTags++;
		}
		else {
			Logger.INSTANCE.log(Severity.ERROR, 
				"Could not determine album folder for file " + mp3.getPath());
		}
	}
	
	/**
	 * Finds the parent album folder for an mp3 track. The album might not be 
	 * directly one level up, as is the case for multi-disc albums.
	 * @param mp3 the mp3 file which represents a song of an album
	 * @return The containing album folder for this mp3 file.
	 */
	private File findAlbumFolder(File mp3) {
		File parent = mp3.getParentFile();
		while (parent != null 
				&& !parent.getName().matches("\\(\\d{4}_?\\d?\\).*")) {
			
			parent = parent.getParentFile();
		}
		return parent;
	}
	
	public int getWrittenTags() {
		return writtenTags;
	}
	
	public void setWriter(TagWriter writer) {
		this.writer = writer;
	}
}
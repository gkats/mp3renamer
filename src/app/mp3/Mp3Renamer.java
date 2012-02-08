package app.mp3;

import java.io.File;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.text.WordUtils;

import app.log.Logger;
import app.log.Severity;
import app.mp3.tag.Tag;
import app.mp3.tag.TagRetriever;

/**
 * Renames mp3 files and their album folders. The album folders are also moved 
 * to a corresponding artist folder. The name of the mp3 is created from its 
 * ID3 tag attributes. The renaming process follows these rules:
 * <ul>
 * <li>
 * The folder hierarchy must be 
 * <code>/&lt;artist&gt;/(&lt;year&gt;) &lt;album&gt;/&lt;filename&gt;</code>
 * </li>
 * <li>
 * <code>&lt;year&gt;</code> is in the 'YYYY' format.
 * </li>
 * <li>
 * The filename must be of the form 
 * <code>&lt;track_no&gt;. &lt;track_title&gt;.mp3</code>
 * </li>
 * <li>
 * <code>&lt;track_no&gt;</code> is the order number of the track formatted 
 * to two digits.
 * </li>
 * <li>
 * Every word in <code>&lt;track_title&gt;, &lt;artist&gt;, &lt;album&gt;</code> 
 * must begin with a capital letter and all words must be separated by a single 
 * space character.
 * </li>
 * <li>
 * All special characters are converted to the character '_'.
 * </li>
 * </ul> 
 * @author gkats
 *
 */
public class Mp3Renamer {

	public static final char[] DELIMITERS = new char[] { ' ', '_', '(' };
	
	private int renamedFiles = 0;
	private int renamedDirs = 0;
	private TagRetriever retriever;
	
	/**
	 * Renames all the mp3 files in the current listing. The files' top folder 
	 * is moved into the artist folder, which is created if it didn't exist.
	 * @param mp3Listings all the mp3 files to be renamed, grouped by parent 
	 * folder
	 */
	public void renameFiles(Map<String, List<File>> mp3Listings) {
		for (String dir : mp3Listings.keySet()) {
			List<File> mp3s = mp3Listings.get(dir);
			Tag tag = null;
			// first file's tag will suffice
			if (!mp3s.isEmpty()) {
				tag = retriever.getTag(mp3s.get(0));
			}
			for (File mp3 : mp3s) {
				rename(mp3);
			}
			// rename the parent (album) folder and move to proper artist folder
			move(dir, tag);
		}
	}
	
	/**
	 * Renames an mp3 file based on its ID3 tag attributes.
	 * @param mp3 the file to be renamed
	 */
	private void rename(File mp3) {
		Tag tag = retriever.getTag(mp3);
		// tag must have all values
		if (tag.hasEnoughAttributes()) {
			try {
				String path = mp3.getParent() + File.separator;
				mp3.renameTo(createFile(path, tag));
				renamedFiles++;
			}
			catch (IllegalArgumentException e) {
				Logger.INSTANCE.log(Severity.ERROR, 
						"Could not write track number " + tag.getTrack() 
						+ " for file: " + mp3.getPath());
			}
		}
		else {
			Logger.INSTANCE.log(Severity.WARNING, "Skipping " + mp3.getPath());
		}
	}

	/**
	 * Creates a file for renaming an mp3 file.
	 * @param path the parent folder's path
	 * @param tag the mp3 ID3 tag
	 * @return A file whose name follows the app's filename conventions
	 */
	private File createFile(String path, Tag tag) {
		StringBuilder nameBuilder = new StringBuilder(path);
		String track = tag.getTrack();
		if (track.length() < 2) {
			nameBuilder.append("0");
		}
		nameBuilder.append(track).append(". ");
		nameBuilder.append(compatibleFilename(capitalizeFully(tag.getTitle())));
		nameBuilder.append(Mp3Lister.MP3_EXTENSION);
		return new File(nameBuilder.toString());
	}
	
	/**
	 * Ensures tha a filename is compatible with file system naming 
	 * constraints.
	 * @param name the original filename
	 * @return A filename where all illegal characters are replaced with '_'
	 */
	private String compatibleFilename(String name) {
		return name.replaceAll("[\\/\\:*?\"<>|]", "_");
	}

	/**
	 * Renames a directory containing mp3 files. This directory represents an 
	 * album and is renamed according to this app's album naming conventions.
	 * The album directory is also moved to the appropriate artist directory.
	 * @param directory the album directory
	 * @param tag one of the album's mp3 files ID3 tag, used to get the artist, 
	 * album name and album year
	 */
	private void move(String directory, Tag tag) {
		if (tag != null && tag.hasEnoughAttributes()) {
			File dir = new File(directory);
			dir.renameTo(createAlbumDir(dir, tag));
			renamedDirs++;
		}
	}
	
	/**
	 * Constructs a file for an album directory based on the album name and 
	 * year, and then moves this folder under the corresponding artist folder.
	 * The artist folder is created if it does not exist already.
	 * @param dir the album folder
	 * @param tag one of the album's mp3s ID3 tag
	 * @return the renamed and moved album folder
	 */
	private File createAlbumDir(File dir, Tag tag) {
		StringBuilder nameBuilder = new StringBuilder(dir.getParent() 
													+ File.separator);
		nameBuilder.append(capitalizeFully(compatibleFilename(tag.getArtist())) 
																+ File.separator);
		File artistDir = new File(nameBuilder.toString());
		if (!artistDir.exists()) {
			if (artistDir.mkdir() == false) {
				Logger.INSTANCE.log(Severity.ERROR, 
						"Could not create folder " + artistDir.getPath());
			}
		}
		nameBuilder.append("(").append(tag.getYear()).append(") ");
		nameBuilder.append(compatibleFilename(capitalizeFully(tag.getAlbum())));
		return new File(nameBuilder.toString());
	}
	
	/**
	 * Fluent interface for {@link WordUtils#capitalizeFully(String, char[])}.
	 */
	private String capitalizeFully(String str) {
		return WordUtils.capitalizeFully(str, DELIMITERS);
	}
	
	public int getRenamedFiles() {
		return renamedFiles;
	}

	public int getRenamedDirs() {
		return renamedDirs;
	}
	
	public void setRetriever(TagRetriever retriever) {
		this.retriever = retriever;
	}
	
}
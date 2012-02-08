package app.mp3;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Lists all mp3 files in a folder.
 * @author gkats
 *
 */
public class Mp3Lister {

	public static final String MP3_EXTENSION = ".mp3";
	
	private int listedFiles = 0;
	private int listedDirs = 0;

	public Map<String, List<File>> listMp3Files(File dir) {
		Map<String, List<File>> files = new HashMap<String, List<File>>();
		parseDirs(dir, files);
		return files;
	}
	
	private void parseDirs(File dir, Map<String, List<File>> out) {
		for (File file : dir.listFiles()) {
			if (file.isDirectory()) {
				List<File> dirList = new ArrayList<File>();
				out.put(file.getPath(), dirList);
				listedDirs++;
				parseDirs(file, out);
			}
			else if (file.isFile() 
					&& file.getName().toLowerCase().endsWith(MP3_EXTENSION)){
				
				String parent = file.getParentFile().getPath();
				out.get(parent).add(file);
				out.put(parent, out.get(parent));
				listedFiles++;
			}
		}
	}

	public int getListedFiles() {
		return listedFiles;
	}

	public int getListedDirs() {
		return listedDirs;
	}
	
}
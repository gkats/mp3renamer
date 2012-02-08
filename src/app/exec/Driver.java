package app.exec;

import java.io.File;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;

import app.log.Logger;
import app.log.Severity;
import app.mp3.Mp3Lister;
import app.mp3.Mp3Renamer;
import app.mp3.Mp3TagRenamer;
import app.mp3.tag.TagRetriever;
import app.mp3.tag.TagWriter;
import app.mp3.tag.jaudio.JAudioTagRetriever;
import app.mp3.tag.jaudio.JAudioTagWriter;

public class Driver {

	public static void main(String[] args) {
		File dir;
		boolean verbose = false;
		boolean writeOperation = false;
		
		if (args.length < 1) {
			printHelpMessage();
			return;
		}
		else if (args.length == 1) {
			dir = new File(args[0]);
		}
		else {
			if (args[0].startsWith("-")) {
				if (args[0].contains("v")) {
					verbose = true;
				}
				if (args[0].contains("w")) {
					writeOperation = true;
				}
				dir = new File(args[1]);
			}
			else {
				printHelpMessage();
				return;
			}
		}
		if (!dir.isDirectory()) {
			Logger.INSTANCE.log(Severity.ERROR, 
					"Directory '" + args[0] + "' does not exist.");
			return;
		}
		
		Mp3Lister lister = new Mp3Lister();
		Map<String, List<File>> mp3Listings = lister.listMp3Files(dir);
		if (verbose) {
			Logger.INSTANCE.log(Severity.INFO, "Found " + lister.getListedFiles()
					+ " files, in " + lister.getListedDirs() + " folders.");
		}

		// no news is good news
		java.util.logging.Logger.getLogger("org.jaudiotagger").setLevel(Level.OFF);
		
		if (writeOperation) {
			Mp3TagRenamer renamer = new Mp3TagRenamer();
			TagWriter writer = new JAudioTagWriter();
			renamer.setWriter(writer);
			renamer.renameTags(mp3Listings);
			if (verbose) {
				Logger.INSTANCE.log(Severity.INFO, "Written " 
						+ renamer.getWrittenTags() + " tags.");
			}
		}
		else {
			Mp3Renamer renamer = new Mp3Renamer();
			TagRetriever retriever = new JAudioTagRetriever();
			renamer.setRetriever(retriever);
			renamer.renameFiles(mp3Listings);
			if (verbose) {
				Logger.INSTANCE.log(Severity.INFO, "Renamed " 
					+ renamer.getRenamedFiles()	+ " files, " 
					+ renamer.getRenamedDirs() + " folders.");
			}
		}
	}
	
	private static void printHelpMessage() {
		System.out.println("Usage: java mp3renamer [-vw] directory");
	}
	
}
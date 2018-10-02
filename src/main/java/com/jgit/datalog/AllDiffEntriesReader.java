package com.jgit.datalog;

import java.io.File;

public class AllDiffEntriesReader {
	// The file containing a set of DiffEntry of commits
	private File diffEntriesFile = null;

	public AllDiffEntriesReader() {
	}

	public void setDiffEntriesFile(File diffEntriesFile) {
		this.diffEntriesFile = diffEntriesFile;
	}

	public File getDiffEntriesFile() {
		return diffEntriesFile;
	}
}

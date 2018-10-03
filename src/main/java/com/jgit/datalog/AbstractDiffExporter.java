package com.jgit.datalog;

import java.io.File;
import java.util.List;

import com.jgit.comparison.object.MyDiffEntries;

public abstract class AbstractDiffExporter {
	final int MAXIMUM_NUMBER_OF_COMMITS_IN_A_FILE = 300;

	public abstract void exportAllDiffEntriesToFile(List<MyDiffEntries> allDiffEntries, File sampleOutputFolder);
}

package com.bitcoin.jgit.object;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.io.IOUtils;
import org.eclipse.jgit.diff.DiffEntry;
import org.eclipse.jgit.diff.DiffFormatter;

/**
 * Represent a changed file in a commit
 * 
 * @author Duc-Anh Nguyen
 *
 */
public class ChangedFile {
	private CommitJgit currentCommit;
	private CommitJgit comparedCommit;
	private DiffEntry diffEntry;

	/**
	 * Get the differences between two commits
	 * 
	 * @return
	 */
	public List<String> getDifferences() {
		List<String> differences = new ArrayList<String>();
		ByteArrayOutputStream stream = new ByteArrayOutputStream();
		DiffFormatter formatter = new DiffFormatter(stream);

		if (currentCommit != null && diffEntry != null)
			formatter.setRepository(currentCommit.getParent().getRepository());
		try {
			formatter.format(diffEntry);
			differences = IOUtils.readLines(new ByteArrayInputStream(stream.toByteArray()), "UTF-8");
		} catch (IOException e) {
			e.printStackTrace();
		}
		return differences;
	}

	public CommitJgit getCurrentCommit() {
		return currentCommit;
	}

	public void setCurrentCommit(CommitJgit currentCommit) {
		this.currentCommit = currentCommit;
	}

	public DiffEntry getDiffEntry() {
		return diffEntry;
	}

	public void setDiffEntry(DiffEntry diffEntry) {
		this.diffEntry = diffEntry;
	}

	public CommitJgit getComparedCommit() {
		return comparedCommit;
	}

	public void setComparedCommit(CommitJgit comparedCommit) {
		this.comparedCommit = comparedCommit;
	}

	@Override
	public String toString() {

		return diffEntry.getNewPath();
	}
}

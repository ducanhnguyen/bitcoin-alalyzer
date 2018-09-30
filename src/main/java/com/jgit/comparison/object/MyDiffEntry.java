package com.jgit.comparison.object;

import com.jgit.object.ChangedFile;
import com.utils.Utils;

/**
 * Represent differences in a file
 * 
 * @author Duc-Anh Nguyen
 *
 */
public class MyDiffEntry {
	private MyDiffEntries parent = null;

	private ChangedFile changedFile = null;

	public MyDiffEntry() {

	}

	public ChangedFile getChangedFile() {
		return changedFile;
	}

	public void setChangedFile(ChangedFile changedFile) {
		this.changedFile = changedFile;
	}

	public MyDiffEntries getParent() {
		return parent;
	}

	public void setParent(MyDiffEntries parent) {
		this.parent = parent;
	}

	@Override
	public String toString() {
		String output = "Changes in file " + getChangedFile().getNameFile() + "\n";
		output += "\"" + Utils.convertToString(getChangedFile().getChangedCodeSnippetBeforeBeingChanged()) + "\"";
		return output;
	}
}

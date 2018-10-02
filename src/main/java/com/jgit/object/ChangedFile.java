package com.jgit.object;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.io.IOUtils;
import org.eclipse.jgit.diff.DiffEntry;
import org.eclipse.jgit.diff.DiffFormatter;

/**
 * Represent a changed file in a commit (with the previous commit)
 * 
 * @author Duc-Anh Nguyen
 *
 */
public class ChangedFile {
	private CommitJgit currentCommit;
	private CommitJgit previousCommit;

	// Represent changes in the file
	private DiffEntry diffEntry;

	/**
	 * Get the differences between two commits
	 * 
	 * <br/>
	 * Example <br/>
	 * 
	 * <pre>
	diff --git a/test/functional/test_runner.py b/test/functional/test_runner.py
	index 28437f8..d996046 100755
	--- a/test/functional/test_runner.py
	+++ b/test/functional/test_runner.py
	&#64;@ -285,11 +285,13 @@
	
	 if args.exclude:
	-        exclude_tests = [re.sub("\.py$", "", test) + (".py" if ".py" not in test else "") for test in args.exclude.split(',')]
	+        exclude_tests = [test.split('.py')[0] for test in args.exclude.split(',')]
	 * </pre>
	 * 
	 * @return the differences between two commits in this file
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

	/**
	 * Get the minimize code snippet before being changed in the current commit.
	 * 
	 * The minimize code snippet is a part of the source code file before being
	 * changed. This minimize code snippet only contains changed lines.
	 * 
	 * @return
	 */
	public List<String> getMinimizeCodeSnippetBeforeBeingChanged() {
		List<String> src = new ArrayList<String>();

		List<String> differences = getDifferences();

		boolean sourcecodeFlag = false;
		for (String difference : differences) {
			if (difference.startsWith("@@"))
				sourcecodeFlag = true;
			if (sourcecodeFlag) {
				if (difference.startsWith("@@"))
					src.add(difference.substring(1));
				else if (difference.startsWith("-"))
					src.add(difference.substring(1));
				else if (difference.startsWith("+")) {

				} else {
					src.add(difference);
				}
			}
		}
		return src;
	}

	/**
	 * Get the minimize code snippet after being changed in the current commit.
	 * 
	 * The minimize code snippet is a part of the source code file before being
	 * changed. This minimize code snippet only contains changed lines.
	 * 
	 * @return
	 */
	public List<String> getMinimizeCodeSnippetAfterBeingChanged() {
		List<String> src = new ArrayList<String>();

		List<String> differences = getDifferences();

		boolean sourcecodeFlag = false;
		for (String difference : differences) {
			if (difference.startsWith("@@"))
				sourcecodeFlag = true;
			if (sourcecodeFlag) {
				if (difference.startsWith("@@"))
					src.add(difference.substring(1));
				else if (difference.startsWith("-")) {

				} else if (difference.startsWith("+")) {
					src.add(difference.substring(1));
				} else {
					src.add(difference);
				}
			}
		}
		return src;
	}

	/**
	 * Get code snippet which are changed in the current commit. These code snippet
	 * are in the old version.
	 * 
	 * @return
	 */
	public List<String> getChangedCodeSnippetBeforeBeingChanged() {
		List<String> src = new ArrayList<String>();

		List<String> differences = getDifferences();

		boolean sourcecodeFlag = false;
		for (String difference : differences) {
			if (difference.startsWith("@@"))
				sourcecodeFlag = true;
			if (sourcecodeFlag) {
				if (difference.startsWith("-") && !difference.startsWith("-//") && !difference.startsWith("- //")) {
					src.add(difference.substring(1));
				}
			}
		}
		return src;
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

	public CommitJgit getPreviousCommit() {
		return previousCommit;
	}

	public void setPreviousCommit(CommitJgit comparedCommit) {
		this.previousCommit = comparedCommit;
	}

	@Override
	public String toString() {
		return diffEntry.getNewPath();
	}

	public String getNameFile() {
		return diffEntry.getOldPath();
	}
}

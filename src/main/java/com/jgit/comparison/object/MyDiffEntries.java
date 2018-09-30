package com.jgit.comparison.object;

import java.io.File;
import java.util.ArrayList;

import com.jgit.object.CommitJgit;

/**
 * All of diff entries when comparing two commits
 * 
 * @author Duc-Anh Nguyen
 *
 */
public class MyDiffEntries extends ArrayList<MyDiffEntry> {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	// Two compared commits
	private CommitJgit commitA; // the newer commit
	private CommitJgit commitB; // the previous commit

	private File repositoryFolder;

	public CommitJgit getCommitA() {
		return commitA;
	}

	public void setCommitA(CommitJgit commitA) {
		this.commitA = commitA;
	}

	public CommitJgit getCommitB() {
		return commitB;
	}

	public void setCommitB(CommitJgit commitB) {
		this.commitB = commitB;
	}

	public void setRepositoryFolder(File repo) {
		this.repositoryFolder = repo;
	}

	public File getRepositoryFolder() {
		return repositoryFolder;
	}

	@Override
	public String toString() {
		String output = "Commit: " + getCommitA().getCommit().getName() + " ["
				+ getCommitA().getCommit().getShortMessage() + "]" + "\n";
		output += "Repo: " + getRepositoryFolder().getAbsolutePath() + "\n";
		for (MyDiffEntry entry : this) {
			output += entry + "\n\n";
		}
		return output;
	}
}

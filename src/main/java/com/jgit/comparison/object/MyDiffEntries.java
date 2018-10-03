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
	private CommitJgit newCommit; // the newer commit
	private CommitJgit oldCommit; // the previous commit

	private File repositoryFolder;

	private String branchName = "";

	public CommitJgit getNewCommit() {
		return newCommit;
	}

	public void setNewCommit(CommitJgit commitA) {
		this.newCommit = commitA;
	}

	public CommitJgit getOldCommit() {
		return oldCommit;
	}

	public void setOldCommit(CommitJgit commitB) {
		this.oldCommit = commitB;
	}

	public void setRepositoryFolder(File repo) {
		this.repositoryFolder = repo;
	}

	public File getRepositoryFolder() {
		return repositoryFolder;
	}

	public String getBranchName() {
		return branchName;
	}

	public void setBranchName(String branchName) {
		this.branchName = branchName;
	}

	@Override
	public String toString() {
		String output = "Commit: " + getNewCommit().getCommit().getName() + " ["
				+ getNewCommit().getCommit().getShortMessage() + "]" + "\n";
		output += "Repo: " + getRepositoryFolder().getAbsolutePath() + "\n";
		for (MyDiffEntry entry : this) {
			output += entry + "\n\n";
		}
		return output;
	}
}

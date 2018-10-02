package com.jgit.datalog;

import java.util.ArrayList;
import java.util.List;

public class CommitLog {
	private String repo;
	private String branch;
	private String commitID; // or commit id, commitA in my definition
	private List<DiffEntryLog> diffEntries = new ArrayList<DiffEntryLog>();

	public CommitLog() {

	}

	public String getCommitID() {
		return commitID;
	}

	public void setCommitID(String hash) {
		this.commitID = hash;
	}

	public List<DiffEntryLog> getDiffEntries() {
		return diffEntries;
	}

	public void setDiffEntries(List<DiffEntryLog> diffEntries) {
		this.diffEntries = diffEntries;
	}

	public String getRepo() {
		return repo;
	}

	public void setRepo(String repo) {
		this.repo = repo;
	}

	public String getBranch() {
		return branch;
	}

	public void setBranch(String branch) {
		this.branch = branch;
	}

	@Override
	public String toString() {
		String output = "";
		output += repo + "\n";
		output += branch + "\n";
		output += commitID + "\n";
		output += diffEntries;
		return output;
	}
}

package com.jgit.datalog.object;

import java.util.ArrayList;
import java.util.List;

public class DiffEntriesOfARepo {
	private String repository;
	private String branch;
	private List<DiffEntriesOfACommit> commits = new ArrayList<DiffEntriesOfACommit>();

	public void setRepository(String repository) {
		this.repository = repository;
	}

	public String getRepository() {
		return repository;
	}

	public void setBranch(String branch) {
		this.branch = branch;
	}

	public String getBranch() {
		return branch;
	}

	public void setCommits(List<DiffEntriesOfACommit> commits) {
		this.commits = commits;
	}

	public List<DiffEntriesOfACommit> getCommits() {
		return commits;
	}
}

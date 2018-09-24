package com.bitcoin.jgit.object;

import java.util.ArrayList;

import org.eclipse.jgit.lib.Repository;

/**
 * A set of commits in a branch
 * 
 * @author Duc-Anh Nguyen
 *
 */
public class CommitsJgit extends ArrayList<CommitJgit> {
	private String branch = new String();
	private Repository repository = null;

	public String getBranch() {
		return branch;
	}

	public void setBranch(String branch) {
		this.branch = branch;
	}

	public void addChild(CommitJgit commit) {
		if (!this.contains(commit)) {
			add(commit);
			commit.setParent(this);
		}
	}

	@Override
	@Deprecated
	public boolean add(CommitJgit arg0) {
		return super.add(arg0);
	}

	public void setRepository(Repository repository) {
		this.repository = repository;
	}

	public Repository getRepository() {
		return repository;
	}
}

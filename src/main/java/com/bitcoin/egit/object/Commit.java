package com.bitcoin.egit.object;

import java.text.MessageFormat;
import java.util.Date;

import org.eclipse.egit.github.core.RepositoryCommit;

public class Commit {
	// Represent a commit on github
	private RepositoryCommit githubCommit;

	public RepositoryCommit getGithubCommit() {
		return githubCommit;
	}

	public void setGithubCommit(RepositoryCommit githubCommit) {
		this.githubCommit = githubCommit;
	}

	public String getSha() {
		return githubCommit.getSha();
	}

	public String getAuthor() {
		return githubCommit.getCommit().getAuthor().getName();
	}

	public Date getDate() {
		return githubCommit.getCommit().getAuthor().getDate();
	}

	public String getURL() {
		return githubCommit.getUrl();
	}

	@Override
	public String toString() {
		final String message = "Commit {0} by {1} on {2}, url = {3}";
		return MessageFormat.format(message, getSha(), getAuthor(), getDate(), getURL());
	}
}

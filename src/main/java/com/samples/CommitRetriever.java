package com.samples;

import java.io.IOException;
import java.text.MessageFormat;
import java.util.Collection;
import java.util.Date;

import org.eclipse.egit.github.core.RepositoryCommit;
import org.eclipse.egit.github.core.RepositoryId;
import org.eclipse.egit.github.core.service.CommitService;

/**
 * Page commits in blocks of 25 and print author and date
 */
public class CommitRetriever {

	/**
	 * Print commit authors and dates paged in blocks of 25
	 *
	 * @param args
	 * @throws IOException
	 */
	public static void main(String[] args) throws IOException {
		final int size = 25;
		final RepositoryId repo = new RepositoryId("ducanhnguyen", "FastRevise");
		final String message = "Commit {0} by {1} on {2}, url = {3}";

		final CommitService service = new CommitService();
		for (Collection<RepositoryCommit> commits : service.pageCommits(repo, size))
			for (RepositoryCommit commit : commits) {
				String sha = commit.getSha().substring(0, 7);
				String author = commit.getCommit().getAuthor().getName();
				Date date = commit.getCommit().getAuthor().getDate();
				String url = commit.getUrl();

				System.out.println(MessageFormat.format(message, sha, author, date, url));
			}
	}
}
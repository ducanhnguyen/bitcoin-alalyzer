package com.bitcoin;

import java.util.Collection;

import org.eclipse.egit.github.core.RepositoryCommit;
import org.eclipse.egit.github.core.RepositoryId;
import org.eclipse.egit.github.core.service.CommitService;

import com.bitcoin.object.Commit;
import com.bitcoin.object.Commits;

/**
 * Crawl commits from bitcoin on Github
 *
 */
public class CommitRetriever {
	public String nameUser = new String();

	public String nameRepo = new String();

	public CommitRetriever() {
	}

	public Commits crawlCommits() {
		Commits commits = new Commits();

		if (nameUser.length() > 0 && nameRepo.length() > 0) {
			final int size = 25;
			final RepositoryId repo = new RepositoryId(nameUser, nameRepo);

			final CommitService service = new CommitService();
			for (Collection<RepositoryCommit> githubCommits : service.pageCommits(repo, size))
				for (RepositoryCommit commit : githubCommits) {
					Commit newCommit = new Commit();
					newCommit.setGithubCommit(commit);
					commits.add(newCommit);

					// Write to file

				}
		}
		return commits;
	}

	public static void main(String[] args) {
		CommitRetriever bitcoinCommitRetriever = new CommitRetriever();
		bitcoinCommitRetriever.setNameRepo("bitcoin");
		bitcoinCommitRetriever.setNameUser("bitcoin");
		Commits commits = bitcoinCommitRetriever.crawlCommits();
		System.out.println(commits.size());
	}

	public String getNameRepo() {
		return nameRepo;
	}

	public void setNameRepo(String nameRepo) {
		this.nameRepo = nameRepo;
	}

	public String getNameUser() {
		return nameUser;
	}

	public void setNameUser(String nameUser) {
		this.nameUser = nameUser;
	}
}

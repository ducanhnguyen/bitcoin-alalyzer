package com.egit;

import java.io.File;
import java.io.IOException;
import java.util.Collection;

import org.eclipse.egit.github.core.Repository;
import org.eclipse.egit.github.core.RepositoryCommit;
import org.eclipse.egit.github.core.client.GitHubClient;
import org.eclipse.egit.github.core.service.CommitService;
import org.eclipse.egit.github.core.service.RepositoryService;

import com.egit.object.Commit;
import com.egit.object.Commits;
import com.utils.CsvManager;
import com.utils.IConfiguration;
import com.utils.IPersonalInformation;

/**
 * Crawl commits from bitcoin on Github
 *
 */
public class CommitRetriever {
	public String bitcoinUser = new String();

	public String bitcoinRepo = new String();

	public File csvFile = null;

	public CommitRetriever() {
	}

	public Commits crawlCommits() {
		Commits commits = new Commits();

		if (bitcoinUser.length() > 0 && bitcoinRepo.length() > 0) {
			final int size = 25;

			// Basic authentication
			GitHubClient client = new GitHubClient();
			client.setCredentials(IPersonalInformation.USERNAME, IPersonalInformation.PASSWORD);
			RepositoryService repoService = new RepositoryService(client);

			// Crawl
			try {
				final Repository repo = repoService.getRepository(bitcoinUser, bitcoinRepo);
				final CommitService service = new CommitService(client);
				final CsvManager csvManager = new CsvManager();

				for (Collection<RepositoryCommit> githubCommits : service.pageCommits(repo, size))
					for (RepositoryCommit commit : githubCommits) {
						Commit newCommit = new Commit();
						newCommit.setGithubCommit(commit);
						commits.add(newCommit);

						// Write to file during crawling
						csvManager.appendARecordToCsv(csvFile, IConfiguration.COMMIT_FILE_HEADER,
								new String[] { newCommit.getSha(), newCommit.getAuthor(),
										newCommit.getDate().toString(), newCommit.getURL() });
						System.out.println(commits.size());
						csvManager.closeCSVWriter();
					}
			} catch (IOException e) {
				e.printStackTrace();
			}

		}
		return commits;
	}

	public static void main(String[] args) {
		CommitRetriever bitcoinCommitRetriever = new CommitRetriever();
		bitcoinCommitRetriever.setNameRepo(IConfiguration.Egit_Bitcoin.REPO_NAME);
		bitcoinCommitRetriever.setNameUser(IConfiguration.Egit_Bitcoin.USER_NAME);
		bitcoinCommitRetriever.setCsvFile(IConfiguration.Egit_Bitcoin.COMMITS_ON_MASTER_FILE);
		Commits commits = bitcoinCommitRetriever.crawlCommits();
		System.out.println(commits.size());
	}

	public String getNameRepo() {
		return bitcoinRepo;
	}

	public void setNameRepo(String nameRepo) {
		this.bitcoinRepo = nameRepo;
	}

	public String getNameUser() {
		return bitcoinUser;
	}

	public void setNameUser(String nameUser) {
		this.bitcoinUser = nameUser;
	}

	public void setCsvFile(File csvFile) {
		this.csvFile = csvFile;
	}

	public File getCsvFile() {
		return csvFile;
	}
}

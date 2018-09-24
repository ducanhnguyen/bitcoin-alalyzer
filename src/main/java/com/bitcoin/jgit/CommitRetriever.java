package com.bitcoin.jgit;

import java.io.File;
import java.io.IOException;

import org.eclipse.jgit.api.Git;
import org.eclipse.jgit.api.errors.GitAPIException;
import org.eclipse.jgit.api.errors.NoHeadException;
import org.eclipse.jgit.lib.Repository;
import org.eclipse.jgit.revwalk.RevCommit;
import org.eclipse.jgit.storage.file.FileRepositoryBuilder;

import com.bitcoin.jgit.object.CommitJgit;
import com.bitcoin.jgit.object.CommitsJgit;
import com.utils.IConfiguration;

public class CommitRetriever {

	private File repoFile = null;

	public CommitRetriever() {

	}

	public CommitsJgit getAllCommits() {
		CommitsJgit commits = new CommitsJgit();
		if (repoFile != null) {
			// get repository
			FileRepositoryBuilder repositoryBuilder = new FileRepositoryBuilder();
			Repository repository = null;
			try {
				repository = repositoryBuilder.setGitDir(repoFile).readEnvironment()
						// scan up the file system tree
						.findGitDir().setMustExist(true).build();
			} catch (IOException e) {
				e.printStackTrace();
			}

			if (repository != null) {
				// Update the output set
				commits.setRepository(repository);

				try {
					commits.setBranch(repository.getFullBranch());
				} catch (IOException e1) {
					// can not get the full branch
					e1.printStackTrace();
				}

				// Get revisions in the current repository
				Git git = new Git(repository);
				Iterable<RevCommit> revisions = null;
				try {
					revisions = git.log().call();
				} catch (NoHeadException e) {
					e.printStackTrace();
				} catch (GitAPIException e) {
					e.printStackTrace();
				}

				// Get all commits in this log
				if (revisions != null) {
					for (RevCommit rev : revisions) {
						CommitJgit commit = new CommitJgit();
						commit.setCommit(rev);
						commits.addChild(commit);
					}
				}
			}
		}

		return commits;
	}

	public static void main(String[] args) throws IOException, GitAPIException {
		// Get all commits in the current branch
		CommitRetriever retriever = new CommitRetriever();
		retriever.setRepoFile(IConfiguration.Jgit_Bitcoin.BITCOIN_REPO);
		CommitsJgit commits = retriever.getAllCommits();
		System.out.println("There are " + commits.size() + " commits in " + commits.getBranch());

		// Display the first commit
		CommitJgit firstCommit = commits.get(0);
		System.out.println("The first commit: " + firstCommit.getCommit().getName() + "\n\""
				+ firstCommit.getCommit().getFullMessage() + "\"");
	}

	public void setRepoFile(File repoFile) {
		this.repoFile = repoFile;
	}

	public File getRepoFile() {
		return repoFile;
	}
}

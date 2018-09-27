package com.jgit;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.eclipse.jgit.api.Git;
import org.eclipse.jgit.api.errors.GitAPIException;
import org.eclipse.jgit.api.errors.NoHeadException;
import org.eclipse.jgit.lib.Ref;
import org.eclipse.jgit.lib.Repository;
import org.eclipse.jgit.revwalk.RevCommit;
import org.eclipse.jgit.storage.file.FileRepositoryBuilder;

import com.jgit.object.CommitJgit;
import com.jgit.object.CommitsJgit;
import com.utils.IConfiguration;

public class CommitRetriever {
	private File repoFile = null;

	private String branchName = MASTER;

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

				// Find the branch
				Git git = new Git(repository);
				List<Ref> branches = new ArrayList<Ref>();
				try {
					branches = git.branchList().call();
				} catch (GitAPIException e1) {
					e1.printStackTrace();
				}

				for (Ref branch : branches) {
					String localBranchName = branch.getName();

					if (localBranchName.equals(branchName)) {
						// Get revisions in the current branch
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
						break;
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
		if (commits.size() > 0) {
			CommitJgit firstCommit = commits.get(0);
			System.out.println("The first commit: " + firstCommit.getCommit().getName() + "\n\""
					+ firstCommit.getCommit().getFullMessage() + "\"");
		} else
			System.out.println("Size of commits = 0");
	}

	public void setRepoFile(File repoFile) {
		this.repoFile = repoFile;
	}

	public File getRepoFile() {
		return repoFile;
	}

	public String getBranchName() {
		return branchName;
	}

	public void setBranchName(String branchName) {
		this.branchName = branchName;
	}

	public static final String MASTER = "refs/heads/master";
}

package com.jgit;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.eclipse.jgit.lib.Repository;
import org.eclipse.jgit.storage.file.FileRepositoryBuilder;

import com.jgit.object.ChangedFile;
import com.jgit.object.CommitJgit;
import com.jgit.object.CommitsJgit;
import com.utils.IConfiguration;

/**
 * Compare two commits in two different repositories
 * 
 * @author Duc-Anh Nguyen
 *
 */
public class CommitsComparer {
	private File repoA = null;
	private File repoB = null;

	private Repository repositoryA = null;
	private Repository repositoryB = null;

	public static void main(String[] args) {
		CommitsComparer comparer = new CommitsComparer();
		comparer.setRepoA(IConfiguration.Jgit_Bitcoin.BITCOIN_REPO);
		comparer.setRepoB(IConfiguration.Jgit_BitcoinABC.BITCOINABC_REPO);
		comparer.compare();
	}

	public CommitsComparer() {
	}

	public void compare() {
		if (isValid()) {
			// Get all commits of two repo
			CommitRetriever retrieverA = new CommitRetriever();
			retrieverA.setRepoFile(repoA);
			retrieverA.setBranchName(CommitRetriever.MASTER);
			CommitsJgit commitsA = retrieverA.getAllCommits();

			CommitRetriever retrieverB = new CommitRetriever();
			retrieverA.setRepoFile(repoB);
			retrieverA.setBranchName(CommitRetriever.MASTER);
			CommitsJgit commitsB = retrieverA.getAllCommits();

			/**
			 * Compare two first commits
			 */
			// Get all diff in the first commit in A
			List<List<String>> diffsInA = new ArrayList<List<String>>();
			int currentCommitInA = 0;
			int previousCommitInA = currentCommitInA + 1;
			CommitJgit commitA = commitsA.get(currentCommitInA);
			List<ChangedFile> changedFileInA = commitA.getChangedFiles(commitsA.get(previousCommitInA));
			for (ChangedFile changedFileItem : changedFileInA) {
				List<String> srcBeforeBeingChanged = changedFileItem.getSourcecodeBeforeBeingChanged();
				diffsInA.add(srcBeforeBeingChanged);
			}

			// Get all diff in the first commit in B
			List<List<String>> diffsInB = new ArrayList<List<String>>();
			int currentCommitInB = 0;
			int previousCommitInB = currentCommitInB + 1;
			CommitJgit commitB = commitsB.get(currentCommitInB);
			List<ChangedFile> changedFileInB = commitB.getChangedFiles(commitsB.get(previousCommitInB));
			for (ChangedFile changedFileItem : changedFileInB) {
				List<String> srcBeforeBeingChanged = changedFileItem.getSourcecodeBeforeBeingChanged();
				diffsInB.add(srcBeforeBeingChanged);
			}

			// Print for debug
			System.out.println("All diff in A [" + commitA.getCommit().getName() + "]");
			for (List<String> diffA : diffsInA)
				System.out.println("Diff:" + diffA.toString());

			System.out.println();
			System.out.println("All diff in B [" + commitB.getCommit().getName() + "]");
			for (List<String> diffB : diffsInB)
				System.out.println("Diff:" + diffB.toString());

			// Compare all diff in A with all diff in B
			List<Pair> diffPairs = new ArrayList<CommitsComparer.Pair>();

			for (List<String> diffA : diffsInA) {
				float maximumSimilarity = 0.0f;
				Pair p = null;
				for (List<String> diffB : diffsInB) {
					float similarity = compareTwoDiff(diffA, diffB);
					if (similarity > maximumSimilarity) {
						maximumSimilarity = similarity;
						p = new Pair();
						p.diffA = diffA;
						p.diffB = diffB;
						p.similarity = similarity;
					}
				}
				diffPairs.add(p);
			}
		}
	}

	public float compareTwoDiff(List<String> diffA, List<String> diffB) {
		float similarity = 0.0f;

		// TODO: code similarity
		return similarity;
	}

	private boolean isValid() {
		boolean isValid = true;

		if (repoA.exists() && repoB.exists()) {
			FileRepositoryBuilder repositoryBuilder = new FileRepositoryBuilder();

			try {
				repositoryA = repositoryBuilder.setGitDir(repoA).readEnvironment()
						// scan up the file system tree
						.findGitDir().setMustExist(true).build();
			} catch (IOException e) {
				e.printStackTrace();
				isValid = false;
			}

			try {
				repositoryB = repositoryBuilder.setGitDir(repoB).readEnvironment()
						// scan up the file system tree
						.findGitDir().setMustExist(true).build();
			} catch (IOException e) {
				e.printStackTrace();
				isValid = false;
			}
		} else
			isValid = false;
		return isValid;
	}

	public void setRepoA(File repoA) {
		this.repoA = repoA;
	}

	public File getRepoA() {
		return repoA;
	}

	public void setRepoB(File repoB) {
		this.repoB = repoB;
	}

	public File getRepoB() {
		return repoB;
	}

	class Pair {
		List<String> diffA;
		List<String> diffB;
		float similarity = 0.0f;
	}
}

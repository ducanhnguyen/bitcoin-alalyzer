package com.jgit.comparison;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import com.jgit.CommitRetriever;
import com.jgit.object.ChangedFile;
import com.jgit.object.CommitJgit;
import com.jgit.object.CommitsJgit;
import com.utils.IConfiguration;

/**
 * Get all diff in a branch of a repository
 * 
 * @author Duc-Anh Nguyen
 *
 */
public class DiffsRetriever {
	private File repo = null;
	private String branchName = new String();

	public static void main(String[] args) {
		DiffsRetriever retriever = new DiffsRetriever();

		retriever.setRepo(IConfiguration.Jgit_Bitcoin.BITCOIN_REPO);
		retriever.setBranchName(CommitRetriever.MASTER);

		List<MyDiff> diffs = retriever.retrieveAllDiffs();

		for (int i = 0; i < 100; i++) {
			MyDiff diff = diffs.get(i);
			System.out.println("File: " + diff.getNameChangeFile());
			System.out.println("Commit A: " + diff.getCommitA().getCommit().getName());
			System.out.println("Commit B: " + diff.getCommitB().getCommit().getName());
			System.out.println("Changed code snippets:" + diff.getChangedCodeSnippet());
		}
	}

	public DiffsRetriever() {
	}

	public List<MyDiff> retrieveAllDiffs() {
		List<MyDiff> diffs = new ArrayList<MyDiff>();
		if (repo.exists()) {
			/**
			 * Get all commits in the repository
			 */
			CommitRetriever retriever = new CommitRetriever();
			retriever.setRepoFile(repo);
			CommitsJgit commits = retriever.getAllCommits();

			/**
			 * Get all diff
			 */

			for (int i = 0; i < commits.size(); i++) {
				System.out.println("Parse commit " + i + "/" + commits.size());
				CommitJgit currentCommit = commits.get(i);
				CommitJgit previousCommit = commits.get(i + 1);

				// Compare two continuous commits
				List<ChangedFile> changedFiles = currentCommit.getChangedFiles(previousCommit);

				for (ChangedFile changedFile : changedFiles)

					if (isCOrCppLanguage(changedFile.getNameFile())) {

						MyDiff diff = new MyDiff();
//						diff.setSourcecodeBeforeBeingChanged(changedFile.getCurrentCommit()
//								.getSourcecodeFileBeforeBeingChanged(changedFile.getNameFile()));
//						diff.setSourcecodeAfterBeingChanged(changedFile.getCurrentCommit()
//								.getSourcecodeFileAfterBeingChanged(changedFile.getNameFile()));
						diff.setChangedCodeSnippet(changedFile.getLinesBeforeBeingChanged());
						diff.setDiff(changedFile.getDifferences());
						diff.setCommitA(currentCommit);
						diff.setCommitB(previousCommit);
						diff.setNameChangeFile(changedFile.getNameFile());
						diffs.add(diff);
					}
			}
		}
		return diffs;
	}

	private boolean isCOrCppLanguage(String name) {
		return name.endsWith(".c") || name.endsWith(".cpp") || name.endsWith(".cc") || name.endsWith(".h")
				|| name.endsWith(".hpp");
	}

	public void setRepo(File repoA) {
		this.repo = repoA;
	}

	public File getRepo() {
		return repo;
	}

	public void setBranchName(String branchNameA) {
		this.branchName = branchNameA;
	}

	public String getBranchName() {
		return branchName;
	}

}

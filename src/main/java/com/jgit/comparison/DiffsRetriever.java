package com.jgit.comparison;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import org.eclipse.jgit.diff.DiffEntry;

import com.jgit.CommitRetriever;
import com.jgit.comparison.object.MyDiffEntries;
import com.jgit.comparison.object.MyDiffEntry;
import com.jgit.object.ChangedFile;
import com.jgit.object.CommitJgit;
import com.jgit.object.CommitsJgit;
import com.utils.IConfiguration;

/**
 * Get all DiffEntry in a branch of a repository
 * 
 * @author Duc-Anh Nguyen
 *
 */
public class DiffsRetriever {
	private File repositoryFolder = null;
	private String branchName = new String();

	public static void main(String[] args) {
		DiffsRetriever retriever = new DiffsRetriever();

		retriever.setRepositoryFolder(IConfiguration.Jgit_Bitcoin.BITCOIN_REPO);
		retriever.setBranchName(CommitRetriever.MASTER);

		List<MyDiffEntries> allDiffEntries = retriever.retrieveAllDiffEntries();

		MyDiffEntries firstDiffEntries = allDiffEntries.get(0);
		MyDiffEntry firstDiff = firstDiffEntries.get(0);
		System.out.println("File: " + firstDiff.getChangedFile().getNameFile());
		System.out.println("Commit A: " + firstDiff.getParent().getCommitA().getCommit().getName());
		System.out.println("Commit B: " + firstDiff.getParent().getCommitB().getCommit().getName());
		System.out.println(
				"Changed code snippets:" + firstDiff.getChangedFile().getChangedCodeSnippetBeforeBeingChanged());
	}

	public DiffsRetriever() {
	}

	public List<MyDiffEntries> retrieveAllDiffEntries() {
		List<MyDiffEntries> allDiffEntries = new ArrayList<MyDiffEntries>();
		if (repositoryFolder.exists()) {
			/**
			 * Get all commits in the repository
			 */
			CommitRetriever retriever = new CommitRetriever();
			retriever.setRepoFile(repositoryFolder);
			CommitsJgit commits = retriever.getAllCommits();

			/**
			 * Get all diff entries. We do not parse the first commit of the repo.
			 */
			for (int i = 0; i < commits.size() - 1; i++) {
				System.out.println("[" + repositoryFolder.getParentFile().getName() + "] Parse commit " + i + "/"
						+ (commits.size() - 1) + " [" + commits.get(i).getCommit().getName() + "]");
				CommitJgit currentCommit = commits.get(i);
				CommitJgit previousCommit = commits.get(i + 1);

				MyDiffEntries diffEntries = new MyDiffEntries();
				diffEntries.setCommitA(currentCommit);
				diffEntries.setCommitB(previousCommit);
				diffEntries.setRepositoryFolder(repositoryFolder);
				
				// Compare two continuous commits
				List<ChangedFile> changedFiles = currentCommit.getChangedFiles(previousCommit);

				for (ChangedFile changedFile : changedFiles)

					if (isCOrCppOrJavaLanguage(changedFile.getNameFile())) {

						MyDiffEntry diffEntry = new MyDiffEntry();
						diffEntry.setChangedFile(changedFile);

						if (changedFile.getDiffEntry().getChangeType() == DiffEntry.ChangeType.DELETE
								|| changedFile.getDiffEntry().getChangeType() == DiffEntry.ChangeType.MODIFY) {
							diffEntries.add(diffEntry);
//							System.out.println(diffEntry.getChangedFile().getChangedCodeSnippetBeforeBeingChanged());
						}

					}

				if (diffEntries.size() > 0)
					allDiffEntries.add(diffEntries);
			}
		}
		return allDiffEntries;
	}

	private boolean isCOrCppOrJavaLanguage(String name) {
		return name.endsWith(".c") || name.endsWith(".cpp") || name.endsWith(".cc") || name.endsWith(".h")
				|| name.endsWith(".hpp") || name.endsWith(".java");
	}

	public void setRepositoryFolder(File repoA) {
		this.repositoryFolder = repoA;
	}

	public File getRepositoryFolder() {
		return repositoryFolder;
	}

	public void setBranchName(String branchNameA) {
		this.branchName = branchNameA;
	}

	public String getBranchName() {
		return branchName;
	}

}

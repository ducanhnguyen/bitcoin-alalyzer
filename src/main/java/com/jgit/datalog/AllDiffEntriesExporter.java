package com.jgit.datalog;

import java.io.File;
import java.util.List;

import com.jgit.CommitRetriever;
import com.jgit.comparison.DiffEntriesRetriever;
import com.jgit.comparison.identifier.AbstractIdentifierRetriever;
import com.jgit.comparison.identifier.RegularIdentifierRetriever;
import com.jgit.comparison.object.MyDiffEntries;
import com.jgit.comparison.object.MyDiffEntry;
import com.jgit.object.CommitJgit;
import com.utils.IConfiguration;
import com.utils.Utils;

/**
 * Export all diff entries of a branch in a repository to file.
 * 
 * 1 commit = n changed files <br/>
 * 1 changed file has 1 DiffEntry<br/>
 * 1 commit has 1 DiffEntries<br/>
 * 
 * A branch of a repo has n commits
 * 
 * @author adn0019
 *
 */
public class AllDiffEntriesExporter {
	public static void main(String[] args) {
		DiffEntriesRetriever retriever = new DiffEntriesRetriever();

		retriever.setRepositoryFolder(IConfiguration.Jgit_Bitcoin.BITCOIN_REPO);
		retriever.setBranchName(CommitRetriever.MASTER);

		List<MyDiffEntries> allDiffEntries = retriever.retrieveAllDiffEntries();

		AllDiffEntriesExporter exporter = new AllDiffEntriesExporter();
		exporter.exportAllDiffEntriesToFile(allDiffEntries, new File("./bitcoin/commit/master.txt"));
	}

	public void exportAllDiffEntriesToFile(List<MyDiffEntries> allDiffEntries, File sampleOutputFile) {
		String descriptionOfaRepo = "";

		int count = 0;
		for (MyDiffEntries diffEntries : allDiffEntries) {
			count++;
			String descriptionOfACommit = "";

			// Add general information
			String repo = diffEntries.getRepositoryFolder().getAbsolutePath();
			descriptionOfACommit += REPOSITORY + repo + "\n";

			String branchName = diffEntries.getBranchName();
			descriptionOfACommit += BRANCH + branchName + "\n";

			CommitJgit commitA = diffEntries.getCommitA();
			descriptionOfACommit += COMMITA + commitA.getCommit().getId().getName() + "\n";

			CommitJgit commitB = diffEntries.getCommitB();
			descriptionOfACommit += COMMITB + commitB.getCommit().getId().getName() + "\n";

			for (MyDiffEntry diffEntry : diffEntries) {
				// Add changed code snippet
				descriptionOfACommit += DELIMITER_BETWEEN_DIFF + "\n";

				String changedFile = diffEntry.getChangedFile().getNameFile();
				descriptionOfACommit += CHANGED_FILE + changedFile + "\n";

				String changedCodeSnippet = Utils
						.convertToString(diffEntry.getChangedFile().getChangedCodeSnippetBeforeBeingChanged());
				descriptionOfACommit += CHANGED_CODE_SNIPPET + "\n" + changedCodeSnippet + "\n";

				// Add identifiers
				AbstractIdentifierRetriever identifierAnalyzer = new RegularIdentifierRetriever();
				identifierAnalyzer.setCodeSnippet(changedCodeSnippet);
				String identifiers = "";
				for (String identifier : identifierAnalyzer.findIdentifiers())
					identifiers += identifier + ",";
				descriptionOfACommit += IDENTIFIER + identifiers + "\n";
			}

			//
			descriptionOfACommit += DELIMITER_BETWEEN_COMMIT + "\n";
			descriptionOfaRepo += descriptionOfACommit;

			if (count % MAXIMUM_NUMBER_OF_COMMITS_IN_A_FILE == 0 || count == allDiffEntries.size()) {
				// Generate file name
				String folder = sampleOutputFile.getParentFile().getAbsolutePath();
				String extensionOutputFile = sampleOutputFile.getName().split("\\.")[1];

				String nameOutputFile = "";
				if (count == allDiffEntries.size()) {
					int startCommitId = Math.round((allDiffEntries.size() * 1.0f / MAXIMUM_NUMBER_OF_COMMITS_IN_A_FILE))
							* MAXIMUM_NUMBER_OF_COMMITS_IN_A_FILE + 1;
					int endCommitId = allDiffEntries.size();
					nameOutputFile = sampleOutputFile.getName().split("\\.")[0] + startCommitId + "-" + endCommitId;
				} else {
					int startCommitId = ((Math.round(count * 1.0f / MAXIMUM_NUMBER_OF_COMMITS_IN_A_FILE) - 1)
							* MAXIMUM_NUMBER_OF_COMMITS_IN_A_FILE + 1);
					int endCommitId = count;
					nameOutputFile = sampleOutputFile.getName().split("\\.")[0] + startCommitId + "-" + endCommitId;
				}

				// Export
				System.out
						.println("Writing to " + folder + File.separator + nameOutputFile + "." + extensionOutputFile);
				Utils.writeToFile(new File(folder + File.separator + nameOutputFile + "." + extensionOutputFile),
						descriptionOfaRepo);

				descriptionOfaRepo = "";
			}
		}
	}

	public static final String REPOSITORY = "repository:";
	public static final String BRANCH = "branch:";
	public static final String COMMITA = "commit A:";
	public static final String COMMITB = "commit B:";
	public static final String CHANGED_FILE = "changed file:";
	public static final String CHANGED_CODE_SNIPPET = "changed code snippet:";
	public static final String DELIMITER_BETWEEN_COMMIT = "--@COMMIT SEPERATOR@--";
	public static final String DELIMITER_BETWEEN_DIFF = "++@DIFF ENTRY SEPERATOR@++";
	public static final String IDENTIFIER = "identifier:";

	final int MAXIMUM_NUMBER_OF_COMMITS_IN_A_FILE = 100;
}

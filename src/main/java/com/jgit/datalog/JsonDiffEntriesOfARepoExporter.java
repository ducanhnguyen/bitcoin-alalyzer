package com.jgit.datalog;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.core.JsonGenerationException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.jgit.CommitRetriever;
import com.jgit.comparison.DiffEntriesOfARepoRetriever;
import com.jgit.comparison.identifier.AbstractIdentifierRetriever;
import com.jgit.comparison.identifier.RegularIdentifierRetriever;
import com.jgit.comparison.object.MyDiffEntries;
import com.jgit.comparison.object.MyDiffEntry;
import com.jgit.datalog.object.ChangedFileOfACommit;
import com.jgit.datalog.object.DiffEntriesOfACommit;
import com.jgit.datalog.object.DiffEntriesOfARepo;
import com.jgit.object.CommitJgit;
import com.utils.IConfiguration;
import com.utils.Utils;

/**
 * Export all diff entries of a repository to a json file
 * 
 * @author adn0019
 *
 */
public class JsonDiffEntriesOfARepoExporter extends AbstractDiffExporter {

	public static void main(String[] args) {
		DiffEntriesOfARepoRetriever retriever = new DiffEntriesOfARepoRetriever();
		retriever.setRepositoryFolder(IConfiguration.Jgit_Bitcoin.BITCOIN_REPO);
		retriever.setBranchName(CommitRetriever.MASTER);
		List<MyDiffEntries> allDiffEntries = retriever.retrieveAllDiffEntriesOfRepo();

		AbstractDiffExporter exporter = new JsonDiffEntriesOfARepoExporter();
		exporter.exportAllDiffEntriesToFile(allDiffEntries, new File("./bitcoin/commit"));
	}

	public void exportAllDiffEntriesToFile(List<MyDiffEntries> allDiffEntriesOfARepo, File outputFolder) {
		ObjectMapper mapper = new ObjectMapper();

		DiffEntriesOfARepo output = new DiffEntriesOfARepo();
		output.setBranch(allDiffEntriesOfARepo.get(0).getBranchName());
		output.setRepository(allDiffEntriesOfARepo.get(0).getRepositoryFolder().getParentFile().getName());

		int count = 0;
		int outputNameId = 0;

		for (MyDiffEntries diffEntriesOfACommit : allDiffEntriesOfARepo) {
			count++;
			DiffEntriesOfACommit commit = new DiffEntriesOfACommit();

			CommitJgit newCommit = diffEntriesOfACommit.getNewCommit();
			commit.setCurrentCommit(newCommit.getCommit().getId().getName());
			commit.setMessageOfCurrentCommit(newCommit.getCommit().getShortMessage());

			CommitJgit oldCommit = diffEntriesOfACommit.getOldCommit();
			commit.setComparedCommit(oldCommit.getCommit().getId().getName());
			commit.setMessageOfComparedCommit(oldCommit.getCommit().getShortMessage());

			for (MyDiffEntry diffEntryOfACommit : diffEntriesOfACommit) {
				ChangedFileOfACommit changedFile = new ChangedFileOfACommit();
				changedFile.setNameFile(diffEntryOfACommit.getChangedFile().getNameFile());

				// Add identifiers
				String changedCodeSnippet = Utils
						.convertToString(diffEntryOfACommit.getChangedFile().getChangedCodeSnippetBeforeBeingChanged());
				AbstractIdentifierRetriever identifierAnalyzer = new RegularIdentifierRetriever();
				identifierAnalyzer.setCodeSnippet(changedCodeSnippet);
				changedFile.setIdentifiers(identifierAnalyzer.findIdentifiers());

				commit.getChangedFiles().add(changedFile);
			}

			output.getCommits().add(commit);

			/***
			 * Export to file
			 */
			if (count % MAXIMUM_NUMBER_OF_COMMITS_IN_A_FILE == 0 || count == allDiffEntriesOfARepo.size()) {
				try {
					// Convert object to JSON string and save into a file directly
					File outputFile = new File(
							outputFolder.getAbsolutePath() + File.separator + outputNameId + ".json");
					System.out.println("Exporting to file " + outputFile.getAbsolutePath());
					mapper.writeValue(outputFile, output);
				} catch (JsonGenerationException e) {
					e.printStackTrace();
				} catch (JsonMappingException e) {
					e.printStackTrace();
				} catch (IOException e) {
					e.printStackTrace();
				}

				outputNameId++;
				output.setCommits(new ArrayList<DiffEntriesOfACommit>());
			}
		}
	}

}

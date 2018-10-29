package com.jgit.datalog;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

import org.eclipse.jgit.lib.PersonIdent;

import com.fasterxml.jackson.core.JsonGenerationException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.jgit.CommitRetriever;
import com.jgit.comparison.DiffEntriesOfARepoRetriever;
import com.jgit.comparison.identifier.AbstractIdentifierRetriever;
import com.jgit.comparison.identifier.RegularIdentifierRetrieverForCpp;
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
		// BitcoinABC
		DiffEntriesOfARepoRetriever retriever = new DiffEntriesOfARepoRetriever();
		retriever.setRepositoryFolder(IConfiguration.Jgit_BitcoinABC.BITCOINABC_REPO);
		retriever.setBranchName(CommitRetriever.MASTER);
		List<MyDiffEntries> allDiffEntries = retriever.retrieveAllDiffEntriesOfRepo();

		AbstractDiffExporter exporter = new JsonDiffEntriesOfARepoExporter();
		exporter.exportAllDiffEntriesToFile(allDiffEntries, new File("./log/bitcoinabc"));

		// Bitcoin
		retriever = new DiffEntriesOfARepoRetriever();
		retriever.setRepositoryFolder(IConfiguration.Jgit_Bitcoin.BITCOIN_REPO);
		retriever.setBranchName(CommitRetriever.MASTER);
		allDiffEntries = retriever.retrieveAllDiffEntriesOfRepo();

		exporter = new JsonDiffEntriesOfARepoExporter();
		exporter.exportAllDiffEntriesToFile(allDiffEntries, new File("./log/bitcoin"));
	}

	public void exportAllDiffEntriesToFile(List<MyDiffEntries> allDiffEntriesOfARepo, File outputFolder) {
		DiffEntriesOfARepo output = new DiffEntriesOfARepo();
		output.setBranch(allDiffEntriesOfARepo.get(0).getBranchName());
		output.setRepository(allDiffEntriesOfARepo.get(0).getRepositoryFolder().getParentFile().getName());

		int count = 0;
		int outputNameId = 0;

		for (MyDiffEntries diffEntriesOfACommit : allDiffEntriesOfARepo) {
			count++;
			DiffEntriesOfACommit commit = new DiffEntriesOfACommit();

			// current commit
			CommitJgit newCommit = diffEntriesOfACommit.getNewCommit();
			commit.setCurrentCommit(newCommit.getCommit().getId().getName());
			commit.setMessage(newCommit.getCommit().getShortMessage());
			commit.setNumOfChangedFile(diffEntriesOfACommit.size());

			PersonIdent authorIdent = newCommit.getCommit().getAuthorIdent();
			Date authorDate = authorIdent.getWhen();
			commit.setDate(authorDate);

			for (MyDiffEntry diffEntryOfACommit : diffEntriesOfACommit) {

				String fileName = diffEntryOfACommit.getChangedFile().getNameFile();

				if (isCOrCppLanguage(fileName)) {
					ChangedFileOfACommit changedFile = new ChangedFileOfACommit();
					changedFile.setNameFileHash(fileName.hashCode());
					changedFile.setNameFileInString(diffEntryOfACommit.getChangedFile().getNameFile());

					// Add identifiers
					String changedCodeSnippet = Utils.convertToString(
							diffEntryOfACommit.getChangedFile().getChangedCodeSnippetBeforeBeingChanged());
					AbstractIdentifierRetriever identifierAnalyzer = new RegularIdentifierRetrieverForCpp();
					identifierAnalyzer.setCodeSnippet(changedCodeSnippet);
					List<String> identifiers = identifierAnalyzer.findIdentifiers();
					int[] hashIdentifiers = hash(identifiers);
					Arrays.sort(hashIdentifiers);
					changedFile.setIdentifiersHash(hashIdentifiers);

					commit.getChangedFiles().add(changedFile);
				}
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
					
					ObjectMapper mapper = new ObjectMapper();
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

	private boolean isCOrCppLanguage(String name) {
		return name.endsWith(".c") || name.endsWith(".cpp") || name.endsWith(".cc") || name.endsWith(".h")
				|| name.endsWith(".hpp");
	}

	private int[] hash(List<String> list) {
		int[] output = new int[list.size()];

		for (int i = 0; i < list.size(); i++)
			output[i] = list.get(i).hashCode();
		return output;
	}
}

package com.jgit.comparison;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.core.JsonGenerationException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.jgit.comparison.object.AllSimilarCommitPairs;
import com.jgit.comparison.object.SimilarChangedFilePair;
import com.jgit.comparison.object.SimilarCommitPair;
import com.jgit.datalog.JsonDiffEntriesOfARepoReader;
import com.jgit.datalog.object.ChangedFileOfACommit;
import com.jgit.datalog.object.DiffEntriesOfACommit;
import com.jgit.datalog.object.DiffEntriesOfARepo;
import com.utils.Utils;

/**
 * All diff entries of all commits in a repo are stored in external files.
 * 
 * @author Duc-Anh Nguyen
 *
 */
public class IndirectCommitsComparator {
	private DiffEntriesOfARepo commitLogOfRepoA = null;
	private DiffEntriesOfARepo commitLogOfRepoB = null;
	private File outputFile = null;

	public static void main(String[] args) {
		JsonDiffEntriesOfARepoReader reader = new JsonDiffEntriesOfARepoReader();
		reader.setDiffEntriesFolder(new File("./log/bitcoin/"));
		DiffEntriesOfARepo commitLogOfRepoA = reader.readData();

		reader.setDiffEntriesFolder(new File("./log/bitcoinabc/"));
		DiffEntriesOfARepo commitLogOfRepoB = reader.readData();

		IndirectCommitsComparator comparator = new IndirectCommitsComparator();
		comparator.setCommitLogOfRepoA(commitLogOfRepoA);
		comparator.setCommitLogOfRepoB(commitLogOfRepoB);
		comparator.setOutputFile(new File("./similarity.json"));
		AllSimilarCommitPairs pairs = comparator.compare();
	}

	public IndirectCommitsComparator() {
	}

	public AllSimilarCommitPairs compare() {
		AllSimilarCommitPairs commitPairs = new AllSimilarCommitPairs();

		if (commitLogOfRepoA != null && commitLogOfRepoB != null)
			for (DiffEntriesOfACommit commitA : commitLogOfRepoA.getCommits())
				for (DiffEntriesOfACommit commitB : commitLogOfRepoB.getCommits()) {

					final List<SimilarChangedFilePair> changedFilePairs = new ArrayList<SimilarChangedFilePair>();
					boolean[] visitedFileA = new boolean[commitA.getChangedFiles().size()]; // visited element = true
					for (boolean item : visitedFileA)
						item = false;

					boolean[] visitedFileB = new boolean[commitB.getChangedFiles().size()];// visited element = true
					for (boolean item : visitedFileB)
						item = false;

					for (int startA = 0; startA < commitA.getChangedFiles().size(); startA++)
						for (int startB = 0; startB < commitB.getChangedFiles().size(); startB++)
							if (!visitedFileA[startA] && !visitedFileB[startB]) {

								ChangedFileOfACommit changedFileA = commitA.getChangedFiles().get(startA);
								int hashA = changedFileA.getNameFileHash();

								ChangedFileOfACommit changedFileB = commitB.getChangedFiles().get(startB);
								int hashB = changedFileB.getNameFileHash();

								// Only comparing files having the same name
								if (hashA == hashB && !shouldBeIgnored(changedFileA.getNameFileInString())) {

									int sizeOfIntersectionSet = intersection(changedFileA.getIdentifiersHash(),
											changedFileB.getIdentifiersHash());
									int sizeOfUnionSet = changedFileA.getIdentifiersHash().length
											+ changedFileB.getIdentifiersHash().length - sizeOfIntersectionSet;
									float similarityOfTwoChangedFiles = sizeOfIntersectionSet * 1.0f / sizeOfUnionSet;

									if (similarityOfTwoChangedFiles >= THRESHOLD_BETWEEN_TWO_CHANGED_FILES) {
										visitedFileA[startA] = true;
										visitedFileB[startB] = true;

										//
										SimilarChangedFilePair changedFilePair = new SimilarChangedFilePair();
										changedFilePair.setNameOfChangedFile(changedFileA.getNameFileInString());
										changedFilePair.setIdentifiersSimilarity(similarityOfTwoChangedFiles);
										changedFilePair.setSizeOfIntersectionSet(sizeOfIntersectionSet);
										changedFilePair.setSizeOfUnionSet(sizeOfUnionSet);
										changedFilePairs.add(changedFilePair);

										break;
									}
								}
							}

					// Calculate commit similarity
					double sumOfSimilarity = 0.0f;
					for (SimilarChangedFilePair changedFilePair : changedFilePairs)
						sumOfSimilarity += changedFilePair.getIdentifiersSimilarity();

					double commitsSimilarity = 0.0f;
					commitsSimilarity = sumOfSimilarity * 1.0f / changedFilePairs.size();

					if (commitsSimilarity >= THRESHOLD_BETWEEN_TWO_COMMITS) {
						SimilarCommitPair commitPair = new SimilarCommitPair();

						commitPair.setCommitA(commitA.getCurrentCommit());
						commitPair.setMessageOfCommitA(commitA.getMessage());
						commitPair.setDateOfCommitA(commitA.getDate());
						commitPair.setNumOfChangedFileInA(commitA.getChangedFiles().size());

						commitPair.setCommitB(commitB.getCurrentCommit());
						commitPair.setMessageOfCommitB(commitB.getMessage());
						commitPair.setDateOfCommitB(commitB.getDate());
						commitPair.setNumOfChangedFileInB(commitB.getChangedFiles().size());

						commitPair.setCommitSimilarity(commitsSimilarity);
						commitPair.setChangedFile(changedFilePairs);
						commitPairs.add(commitPair);

						System.out.println("Size of commit pairs = " + commitPairs.size());
					}
				}

		// Append the new commit pair to file
		Utils.appendToFile(outputFile, commitPairs.toString());
		try {
			// Convert object to JSON string and save into a file directly
			ObjectMapper mapper = new ObjectMapper();
			mapper.writeValue(outputFile, commitPairs);
		} catch (JsonGenerationException e) {
			e.printStackTrace();
		} catch (JsonMappingException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return commitPairs;

	}

	private boolean shouldBeIgnored(String name) {
		for (String testingFolderSignal : TESTING_FOLDER_SIGNALS) {
			if (name.indexOf(testingFolderSignal) >= 0)
				return true;
		}

		for (String ignoredFile : IGNORED_FILES) {
			if (name.endsWith(ignoredFile))
				return true;
		}
		return false;
	}

	/**
	 * 
	 * @param indentifersA arrange in increasing order
	 * @param identifiersB arrange in increasing order
	 * @return
	 */
	private int intersection(int[] indentifersA, int[] identifiersB) {
		int sizeofIntersection = 0;
		int maxIterator = indentifersA.length > identifiersB.length ? identifiersB.length : indentifersA.length;

		for (int i = 0; i < maxIterator; i++) {
			if (indentifersA[i] == identifiersB[i])
				sizeofIntersection++;
			else
				break;
		}
		return sizeofIntersection;
	}

	public void setCommitLogOfRepoA(DiffEntriesOfARepo repoA) {
		this.commitLogOfRepoA = repoA;
	}

	public DiffEntriesOfARepo getCommitLogOfRepoA() {
		return commitLogOfRepoA;
	}

	public void setCommitLogOfRepoB(DiffEntriesOfARepo repoB) {
		this.commitLogOfRepoB = repoB;
	}

	public DiffEntriesOfARepo getCommitLogOfRepoB() {
		return commitLogOfRepoB;
	}

	public void setOutputFile(File outputFile) {
		if (outputFile.exists())
			outputFile.delete();

		this.outputFile = outputFile;
	}

	public File getOutputFile() {
		return outputFile;
	}

	// We ignore folder used for testing
	public static final String[] TESTING_FOLDER_SIGNALS = new String[] { "src/test/" };

	// We ignore some files
	public static final String[] IGNORED_FILES = new String[] { /*"src/init.cpp" */};

	public static final double THRESHOLD_BETWEEN_TWO_COMMITS = 0.7;
	public static final double THRESHOLD_BETWEEN_TWO_CHANGED_FILES = 0.7;
}

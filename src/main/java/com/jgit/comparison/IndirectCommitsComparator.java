package com.jgit.comparison;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

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
		comparator.setOutputFile(new File("./similarity.txt"));
		List<SimilarCommitPair> pairs = comparator.compare();
	}

	public IndirectCommitsComparator() {
	}

	public List<SimilarCommitPair> compare() {
		List<SimilarCommitPair> pairs = new ArrayList<SimilarCommitPair>();

		if (commitLogOfRepoA != null && commitLogOfRepoB != null) {
			for (DiffEntriesOfACommit commitA : commitLogOfRepoA.getCommits()) {

				for (DiffEntriesOfACommit commitB : commitLogOfRepoB.getCommits()) {

					double commitsSimilarity = 0.0f;
					double sumOfSimilarity = 0.0f;

					int maximumSize = commitA.getChangedFiles().size() > commitB.getChangedFiles().size()
							? commitB.getChangedFiles().size()
							: commitA.getChangedFiles().size();

					final List<SimilarChangedFilePair> changedFilePairs = new ArrayList<SimilarChangedFilePair>();

					int numOfDuplicatedFiles = 0;

					for (int i = 0; i < maximumSize; i++) {
						ChangedFileOfACommit changedFileA = commitA.getChangedFiles().get(i);
						ChangedFileOfACommit changedFileB = commitB.getChangedFiles().get(i);

						// Only comparing files having the same name
						if (!shouldBeIgnored(changedFileA.getNameFileInString())) {
							if (changedFileA.getNameFileHash() == changedFileB.getNameFileHash()) {
								int sizeOfIntersection = intersection(changedFileA.getIdentifiersHash(),
										changedFileB.getIdentifiersHash());
								int union = changedFileA.getIdentifiersHash().length
										+ changedFileB.getIdentifiersHash().length - sizeOfIntersection;
								float similarityOfTwoChangedFiles = sizeOfIntersection * 1.0f / union;

								if (similarityOfTwoChangedFiles >= THRESHOLD_BETWEEN_TWO_CHANGED_FILES) {
									sumOfSimilarity += similarityOfTwoChangedFiles;
									numOfDuplicatedFiles++;

									//
									SimilarChangedFilePair changedFilePair = new SimilarChangedFilePair();
									changedFilePair.setNameOfChangedFile(changedFileA.getNameFileInString());
									changedFilePair.setIdentifiersSimilarity(similarityOfTwoChangedFiles);
									changedFilePairs.add(changedFilePair);
								}

							} else {
								if (i == 0)
									break;
								else {
									// different names, we break!
									commitsSimilarity = sumOfSimilarity * 1.0f / numOfDuplicatedFiles;
									break;
								}
							}
						}
					}

					if (commitsSimilarity >= THRESHOLD_BETWEEN_TWO_COMMITS) {
						SimilarCommitPair pair = new SimilarCommitPair();

						pair.setCommitA(commitA.getCurrentCommit());
						pair.setMessageOfCommitA(commitA.getMessage());
						pair.setDateOfCommitA(commitA.getDate());
						pair.setNumOfChangedFileInA(commitA.getChangedFiles().size());

						pair.setCommitB(commitB.getCurrentCommit());
						pair.setMessageOfCommitB(commitB.getMessage());
						pair.setDateOfCommitB(commitB.getDate());
						pair.setNumOfChangedFileInB(commitB.getChangedFiles().size());

						pair.setCommitSimilarity(commitsSimilarity);
						pair.setChangedFile(changedFilePairs);
						pairs.add(pair);

						// Export to file
						String output = pair.toString();
						System.out.println(output);
						String oldContent = Utils.convertToString(Utils.readFileContent(outputFile));
						output = output + oldContent;
						Utils.writeToFile(outputFile, output);
					}
				}
			}
		}

		return pairs;
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
	public static final String[] IGNORED_FILES = new String[] { "src/init.cpp" };

	public static final double THRESHOLD_BETWEEN_TWO_COMMITS = 0.7;
	public static final double THRESHOLD_BETWEEN_TWO_CHANGED_FILES = 0.7;
}

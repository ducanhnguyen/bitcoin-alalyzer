package com.jgit.comparison;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import com.jgit.CommitRetriever;
import com.jgit.comparison.object.MyDiffEntries;
import com.jgit.comparison.object.MyDiffEntry;
import com.utils.IConfiguration;
import com.utils.Utils;

public class DirectCommitsComparator extends AbstractComparer {
	private MyDiffEntries diffEntriesOfCommitA = null;
	private MyDiffEntries diffEntriesOfCommitB = null;

	public DirectCommitsComparator() {
	}

	public static void main(String[] args) {
		System.out.println("Parse two repo");
		DiffEntriesOfARepoRetriever diffsRetrieverOfRepoA = new DiffEntriesOfARepoRetriever();
		diffsRetrieverOfRepoA.setRepositoryFolder(IConfiguration.Jgit_Bitcoin.BITCOIN_REPO);
		diffsRetrieverOfRepoA.setBranchName(CommitRetriever.MASTER);
		List<MyDiffEntries> diffEntriesOfRepoA = diffsRetrieverOfRepoA.retrieveAllDiffEntriesOfRepo();

		DiffEntriesOfARepoRetriever diffsRetrieverOfRepoB = new DiffEntriesOfARepoRetriever();
		diffsRetrieverOfRepoB.setRepositoryFolder(IConfiguration.Jgit_BitcoinABC.BITCOINABC_REPO);
		diffsRetrieverOfRepoB.setBranchName(CommitRetriever.MASTER);
		List<MyDiffEntries> diffEntriesOfRepoB = diffsRetrieverOfRepoB.retrieveAllDiffEntriesOfRepo();

		System.out.println("\n\nCompare two repo");
		int total = diffEntriesOfRepoA.size() * diffEntriesOfRepoB.size();
		int count = 0;
		for (int i = 0; i < diffEntriesOfRepoA.size(); i++)
			for (int j = 0; j < diffEntriesOfRepoB.size(); j++) {
				System.out.println("\n---------\n[Step " + (++count) + "/" + total + "]");
				System.out.println("We compare two commits:\n");
				DirectCommitsComparator comparer = new DirectCommitsComparator();
				MyDiffEntries A = diffEntriesOfRepoA.get(i);
				MyDiffEntries B = diffEntriesOfRepoB.get(j);
				System.out.println("1. " + A.toString());
				System.out.println("2. " + B.toString());

				// Calculate similarity between two commits
				comparer.setDiffEntriesOfCommitA(A);
				comparer.setDiffEntriesOfCommitB(B);
				List<SimilarityPair> pairs = comparer.compareTwoCommits();

				double sumSim = 0.0f;
				for (SimilarityPair p : pairs) {
					sumSim += p.sim;
				}
				double commitSimilarity = sumSim * 1.0f / pairs.size();

				if (commitSimilarity >= COMMIT_THRESHOLD_SIMILARITY) {
					String output = new String();
					output += "--------------------\nCommit A: " + A.getNewCommit().getCommit().getName() + "\n";
					output += "Commit B: " + B.getNewCommit().getCommit().getName() + "\n";
					output += "Commit similarity  = " + commitSimilarity + "\n";
					output += pairs.toString() + "\n\n";

					File outputFile = new File("./similarity.txt");
					String oldContent = Utils.convertToString(Utils.readFileContent(outputFile));
					output = output + oldContent;
					Utils.writeToFile(outputFile, output);

					System.out.println(output);
				}
			}
	}

	public List<SimilarityPair> compareTwoCommits() {
		List<SimilarityPair> pairs = new ArrayList<DirectCommitsComparator.SimilarityPair>();

		if (diffEntriesOfCommitA != null && diffEntriesOfCommitB != null) {

			for (MyDiffEntry entryA : diffEntriesOfCommitA) {

				double maximumSim = 0.0f;
				SimilarityPair pair = null;

				for (MyDiffEntry entryB : diffEntriesOfCommitB)

					if (entryA.getChangedFile().getNameFile().equals(entryB.getChangedFile().getNameFile())) {

						boolean isAddedBefore = false;
						for (SimilarityPair p : pairs) {
							if (p.entryA.equals(entryA) && p.entryB.equals(entryB)) {
								isAddedBefore = true;
								break;
							}
						}

						if (!isAddedBefore) {
							double sim = compareTwoDiffEntries(entryA, entryB);

							if (sim > maximumSim) {
								maximumSim = sim;
								pair = new SimilarityPair(entryA, entryB, sim);
							}
						}
					}

				if (pair != null && pair.sim >= DIFF_ENTRY_THRESHOLD_SIMILARITY)
					pairs.add(pair);
			}
		}

		return pairs;
	}

	/**
	 * Compare two diff entries
	 * 
	 * @param entryA
	 * @param entryB
	 * @return
	 */
	private double compareTwoDiffEntries(MyDiffEntry entryA, MyDiffEntry entryB) {
		double similarity = 0.0f;

		if (entryA != null && entryB != null) {

			AbstractSimilarity sim = new LineToLineSimilarity();
			sim.setSource(Utils.convertToString(entryA.getChangedFile().getChangedCodeSnippetBeforeBeingChanged()));
			sim.setTarget(Utils.convertToString(entryB.getChangedFile().getChangedCodeSnippetBeforeBeingChanged()));
			similarity = sim.compare();
			System.out.println("=> Similarity = " + similarity);
		}
		return similarity;
	}

	public MyDiffEntries getDiffEntriesOfCommitA() {
		return diffEntriesOfCommitA;
	}

	public void setDiffEntriesOfCommitA(MyDiffEntries diffA) {
		this.diffEntriesOfCommitA = diffA;
	}

	public MyDiffEntries getDiffEntriesOfCommitB() {
		return diffEntriesOfCommitB;
	}

	public void setDiffEntriesOfCommitB(MyDiffEntries diffB) {
		this.diffEntriesOfCommitB = diffB;
	}

	public static final float COMMIT_THRESHOLD_SIMILARITY = 0.4f;
	public static final float DIFF_ENTRY_THRESHOLD_SIMILARITY = 0.4f;

	class SimilarityPair {
		MyDiffEntry entryA;
		MyDiffEntry entryB;
		double sim = 0.0f;

		public SimilarityPair(MyDiffEntry entryA, MyDiffEntry entryB, double sim) {
			this.entryA = entryA;
			this.entryB = entryB;
			this.sim = sim;
		}

		@Override
		public String toString() {
			String output = "[Pair] sim = " + sim + "\n";
			output += "+ Entry A:\n" + entryA.toString() + "\n";
			output += "+ Entry B:\n" + entryB.toString() + "\n\n";
			return output;
		}
	}
}

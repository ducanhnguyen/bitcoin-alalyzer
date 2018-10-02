package com.jgit.comparison;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import com.jgit.CommitRetriever;
import com.jgit.comparison.object.MyDiffEntries;
import com.jgit.comparison.object.MyDiffEntry;
import com.utils.IConfiguration;
import com.utils.Utils;

public class TwoCommitsComparer {
	private MyDiffEntries diffEntriesOfCommitA = null;
	private MyDiffEntries diffEntriesOfCommitB = null;

	public TwoCommitsComparer() {
	}

	public static void main(String[] args) {
		System.out.println("Parse two repo");
		DiffEntriesRetriever diffsRetrieverOfRepoA = new DiffEntriesRetriever();
		diffsRetrieverOfRepoA.setRepositoryFolder(IConfiguration.Jgit_Bitcoin.BITCOIN_REPO);
		diffsRetrieverOfRepoA.setBranchName(CommitRetriever.MASTER);
		List<MyDiffEntries> diffEntriesOfRepoA = diffsRetrieverOfRepoA.retrieveAllDiffEntries();

		DiffEntriesRetriever diffsRetrieverOfRepoB = new DiffEntriesRetriever();
		diffsRetrieverOfRepoB.setRepositoryFolder(IConfiguration.Jgit_BitcoinABC.BITCOINABC_REPO);
		diffsRetrieverOfRepoB.setBranchName(CommitRetriever.MASTER);
		List<MyDiffEntries> diffEntriesOfRepoB = diffsRetrieverOfRepoB.retrieveAllDiffEntries();

		System.out.println("\n\nCompare two repo");
		int total = diffEntriesOfRepoA.size() * diffEntriesOfRepoB.size();
		int count = 0;
		for (int i = 0; i < diffEntriesOfRepoA.size(); i++)
			for (int j = 0; j < diffEntriesOfRepoB.size(); j++) {
				System.out.println("\n---------\n[Step " + (++count) + "/" + total + "]");
				System.out.println("We compare two commits:\n");
				TwoCommitsComparer comparer = new TwoCommitsComparer();
				MyDiffEntries A = diffEntriesOfRepoA.get(i);
				MyDiffEntries B = diffEntriesOfRepoB.get(j);
				System.out.println("1. " + A.toString());
				System.out.println("2. " + B.toString());

				comparer.setDiffEntriesOfCommitA(A);
				comparer.setDiffEntriesOfCommitB(B);
				double similarity = comparer.compareTwoCommitsByDiffEntries();

				if (similarity > THRESHOLD_SIMILARITY) {
					String output = new String();
					output += "Commit A: " + A.getCommitA().getCommit().getName() + "\n";
					output += "Commit B: " + B.getCommitA().getCommit().getName() + "\n";
					output += "The similarity of two commits (or two Diff Entries)  = " + similarity + "\n\n";

					File outputFile = new File("./similarity.txt");
					String oldContent = Utils.convertToString(Utils.readFileContent(outputFile));
					output = output + oldContent;
					Utils.writeToFile(outputFile, output);

					System.out.println(output);
				}
			}
	}

	public double compareTwoCommitsByDiffEntries() {
		double commitSimilarity = 0.0f;

		List<SimilarityPair> pairs = new ArrayList<TwoCommitsComparer.SimilarityPair>();

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

				if (pair != null)
					pairs.add(pair);
			}
		}
		// Calculate similarity between two commits
		double sumSim = 0.0f;
		for (SimilarityPair p : pairs) {
			sumSim += p.sim;
		}
		commitSimilarity = sumSim * 1.0f / pairs.size();

		return commitSimilarity;
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

	public static final float THRESHOLD_SIMILARITY = 0.6f;

	class SimilarityPair {
		MyDiffEntry entryA;
		MyDiffEntry entryB;
		double sim = 0.0f;

		public SimilarityPair(MyDiffEntry entryA, MyDiffEntry entryB, double sim) {
			this.entryA = entryA;
			this.entryB = entryB;
			this.sim = sim;
		}
	}
}

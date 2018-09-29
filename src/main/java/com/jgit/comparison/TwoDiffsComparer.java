package com.jgit.comparison;

import java.io.File;
import java.util.List;

import com.jgit.CommitRetriever;
import com.utils.IConfiguration;
import com.utils.Utils;

import net.ricecode.similarity.JaroWinklerStrategy;
import net.ricecode.similarity.SimilarityStrategy;
import net.ricecode.similarity.StringSimilarityService;
import net.ricecode.similarity.StringSimilarityServiceImpl;

public class TwoDiffsComparer {
	private MyDiff diffA = null;
	private MyDiff diffB = null;

	public TwoDiffsComparer() {
	}

	public static void main(String[] args) {
		DiffsRetriever diffsRetrieverA = new DiffsRetriever();
		diffsRetrieverA.setRepo(IConfiguration.Jgit_Bitcoin.BITCOIN_REPO);
		diffsRetrieverA.setBranchName(CommitRetriever.MASTER);
		List<MyDiff> diffsA = diffsRetrieverA.retrieveAllDiffs();

		DiffsRetriever diffsRetrieverB = new DiffsRetriever();
		diffsRetrieverB.setRepo(IConfiguration.Jgit_BitcoinABC.BITCOINABC_REPO);
		diffsRetrieverB.setBranchName(CommitRetriever.MASTER);
		List<MyDiff> diffsB = diffsRetrieverB.retrieveAllDiffs();

		// Compare two first DIFF
		TwoDiffsComparer comparer = new TwoDiffsComparer();

		int total = diffsA.size() * diffsB.size();
		int count = 0;
		for (int i = 0; i < diffsA.size(); i++)
			for (int j = 0; j < diffsB.size(); j++) {
				System.out.println(++count + "/" + total);
				comparer.setDiffA(diffsA.get(i));
				comparer.setDiffB(diffsB.get(j));
				double similarity = comparer.compareTwoDiff();

				if (similarity > THRESHOLD_SIMILARITY) {
					String output = new String();
					output += "Diff A:" + diffsA.get(i).getChangedCodeSnippet() + "\n";
					output += "Commit A:" + diffsA.get(i).getCommitA().getCommit().getName() + "\n";
					output += "Changed file A: " + diffsA.get(i).getNameChangeFile() + "\n";

					output += "Diff B:" + diffsB.get(j).getChangedCodeSnippet() + "\n";
					output += "Commit B:" + diffsB.get(j).getCommitA().getCommit().getName() + "\n";
					output += "Changed file B: " + diffsB.get(j).getNameChangeFile() + "\n";

					output += "The similarity of two DIFF  = " + similarity + "\n";

					File outputFile = new File("./similarity.txt");
					String oldContent = Utils.convertToString(Utils.readFileContent(outputFile));
					output = output + oldContent;
					Utils.writeToFile(outputFile, output);

					System.out.println(output);
				}
			}
	}

	public double compareTwoDiff() {
		double similarity = 0.0f;

		if (diffA != null && diffB != null) {
			SimilarityStrategy strategy = new JaroWinklerStrategy();
			String target = Utils.convertToString(diffA.getChangedCodeSnippet());
			String source = Utils.convertToString(diffB.getChangedCodeSnippet());
			StringSimilarityService service = new StringSimilarityServiceImpl(strategy);

			if (source.length() > 0 && target.length() > 0)
				similarity = service.score(source, target);
		}
		return similarity;
	}

	public MyDiff getDiffA() {
		return diffA;
	}

	public void setDiffA(MyDiff diffA) {
		this.diffA = diffA;
	}

	public MyDiff getDiffB() {
		return diffB;
	}

	public void setDiffB(MyDiff diffB) {
		this.diffB = diffB;
	}

	public static final float THRESHOLD_SIMILARITY = 0.5f;
}

package com.jgit.comparison;

import java.util.List;

import com.jgit.CommitRetriever;
import com.utils.IConfiguration;

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
		comparer.setDiffA(diffsA.get(0));
		comparer.setDiffB(diffsB.get(0));
		float similarity = comparer.compareTwoDiff();
		System.out.println("The similarity of two DIFF  = " + similarity);
	}

	public float compareTwoDiff() {
		float similarity = 0.0f;

		if (diffA != null && diffB != null) {
			// TODO: code similarity
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

}

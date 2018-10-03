package com.jgit.comparison;

import java.io.File;

import com.jgit.datalog.JsonDiffEntriesOfARepoReader;
import com.jgit.datalog.object.ChangedFileOfACommit;
import com.jgit.datalog.object.DiffEntriesOfACommit;
import com.jgit.datalog.object.DiffEntriesOfARepo;

public class IndirectCommitsComparator {
	private DiffEntriesOfARepo repoA = null;
	private DiffEntriesOfARepo repoB = null;

	public static void main(String[] args) {
		JsonDiffEntriesOfARepoReader reader = new JsonDiffEntriesOfARepoReader();
		reader.setDiffEntriesFolder(new File("./bitcoin/"));
		DiffEntriesOfARepo repoA = reader.readData();

		reader.setDiffEntriesFolder(new File("./bitcoinabc/"));
		DiffEntriesOfARepo repoB = reader.readData();

		IndirectCommitsComparator comparator = new IndirectCommitsComparator();
		comparator.setRepoA(repoA);
		comparator.setRepoB(repoB);
		comparator.compare();
	}

	public IndirectCommitsComparator() {
	}

	public void compare() {
		if (repoA != null && repoB != null) {
			for (DiffEntriesOfACommit commitA : repoA.getCommits())
				for (DiffEntriesOfACommit commitB : repoB.getCommits()) {

					for (ChangedFileOfACommit changedFileA : commitA.getChangedFiles())
						for (ChangedFileOfACommit changedFileB : commitB.getChangedFiles()) {

						}

				}

		}
	}

	private void convertIdentifierToInteger() {

	}

	public void setRepoA(DiffEntriesOfARepo repoA) {
		this.repoA = repoA;
	}

	public DiffEntriesOfARepo getRepoA() {
		return repoA;
	}

	public void setRepoB(DiffEntriesOfARepo repoB) {
		this.repoB = repoB;
	}

	public DiffEntriesOfARepo getRepoB() {
		return repoB;
	}

	class SimilarityPair {

	}
}

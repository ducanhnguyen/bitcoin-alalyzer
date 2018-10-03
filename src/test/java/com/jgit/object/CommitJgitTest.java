package com.jgit.object;

import static org.junit.Assert.assertArrayEquals;

import java.util.List;

import org.junit.Test;

import com.jgit.CommitRetriever;
import com.utils.IConfiguration;

public class CommitJgitTest {

	@Test
	public void test1() {
		CommitRetriever retriever = new CommitRetriever();
		retriever.setRepoFile(IConfiguration.Jgit_Bitcoin.BITCOIN_REPO);
		retriever.setBranchName(CommitRetriever.MASTER);
		CommitsJgit commits = retriever.getAllCommits();

		// https://github.com/bitcoin/bitcoin/commit/011c39c2969420d7ca8b40fbf6f3364fe72da2d0
		CommitJgit currentCommit = commits.findCommitById("011c39c2969420d7ca8b40fbf6f3364fe72da2d0");

		List<ChangedFile> changedFiles = currentCommit.compareWithPreviousCommit();

		assertArrayEquals(
				new String[] { "src/net_processing.h", "src/netbase.cpp", "src/policy/fees.h",
						"src/primitives/transaction.h", "src/script/ismine.cpp", "src/txmempool.h" },
				getNamesOfChangedFiles(changedFiles));
	}

	@Test
	public void test2() {
		CommitRetriever retriever = new CommitRetriever();
		retriever.setRepoFile(IConfiguration.Jgit_Bitcoin.BITCOIN_REPO);
		CommitsJgit commits = retriever.getAllCommits();

		// https://github.com/bitcoin/bitcoin/commit/430bf6c7a1a24a59050e7c9dac56b64b820edb43
		CommitJgit currentCommit = commits.findCommitById("430bf6c7a1a24a59050e7c9dac56b64b820edb43");

		List<ChangedFile> changedFiles = currentCommit.compareWithPreviousCommit();

		assertArrayEquals(new String[] { "depends/packages/qt.mk" }, getNamesOfChangedFiles(changedFiles));
	}

	private String[] getNamesOfChangedFiles(List<ChangedFile> changedFiles) {
		String[] nameOfChangedFiles = new String[changedFiles.size()];
		for (int i = 0; i < changedFiles.size(); i++)
			nameOfChangedFiles[i] = changedFiles.get(i).getNameFile();
		return nameOfChangedFiles;
	}
}

package com.utils;

import java.io.File;

public interface IConfiguration {
	interface Egit_Bitcoin {
		String REPO_NAME = "bitcoin";
		String USER_NAME = "bitcoin";
		File COMMITS_ON_MASTER_FILE = new File("W:/bitcoin/master/commits.csv");
		String BASE_COMMIT_URL = "https://api.github.com/repos/bitcoin/bitcoin/commits/";
		File BASE_PATCHES_URL = new File("W:/bitcoin/master/patches/");
	}

	// for testing
	interface JGit_SIMILARITY_REPO1 {
		File BITCOIN_REPO = new File("F:\\workspace\\java\\similarity-repo1\\.git");
	}

	// for testing
	interface JGit_SIMILARITY_REPO2 {
		File BITCOIN_REPO = new File("F:\\workspace\\java\\similarity-repo2\\.git");
	}

	interface Jgit_Bitcoin {
//		File BITCOIN_REPO = new File("C:\\Users\\adn0019\\WORK\\bitcoin\\bitcoin\\.git");
		File BITCOIN_REPO = new File("G:\\workspace\\bitcoin\\.git");
	}

	interface Jgit_BitcoinABC {
//		File BITCOINABC_REPO = new File("C:\\Users\\adn0019\\WORK\\bitcoin\\bitcoin-abc\\.git");
		File BITCOINABC_REPO = new File("F:\\workspace\\bitcoin\\bitcoin-abc\\.git");
	}

	String[] COMMIT_FILE_HEADER = new String[] { "commit", "author", "date", "url" };
	int COMMIT_HEADER_ID = 0;
}

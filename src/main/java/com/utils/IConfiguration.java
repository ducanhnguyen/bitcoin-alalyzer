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

	interface Jgit_Bitcoin {
		File BITCOIN_REPO = new File("W:\\bitcoin\\bitcoin\\.git");
	}

	String[] COMMIT_FILE_HEADER = new String[] { "commit", "author", "date", "url" };
	int COMMIT_HEADER_ID = 0;
}

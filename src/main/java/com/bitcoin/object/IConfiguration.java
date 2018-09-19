package com.bitcoin.object;

import java.io.File;

public interface IConfiguration {
	String BITCOIN_REPO_NAME = "bitcoin";
	String BITCOIN_USER_NAME = "bitcoin";
	File COMMITS_FILE = new File("./bitcoin_commits_master.csv");

	String BASE_COMMIT_URL = "https://api.github.com/repos/bitcoin/bitcoin/commits/";

	String BASE_PATCHES_URL = "./patches/";

	String[] COMMIT_FILE_HEADER = new String[] { "commit", "author", "date", "url" };
	int COMMIT_HEADER_ID = 0;
}

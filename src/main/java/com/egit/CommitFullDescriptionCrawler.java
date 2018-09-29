package com.egit;

import java.io.File;
import java.util.List;

import com.utils.CsvManager;
import com.utils.DescriptionOfACommitRetriever;
import com.utils.IConfiguration;
import com.utils.Utils;

/**
 * Read the commit file to get the full description of each commit
 * 
 * @author Duc-Anh Nguyen
 *
 */
public class CommitFullDescriptionCrawler {

	public CommitFullDescriptionCrawler() {
	}

	public static void main(String[] args) {
		CsvManager csvManager = new CsvManager();
		List<String[]> commits = csvManager.readRecordsFromCsv(IConfiguration.Egit_Bitcoin.COMMITS_ON_MASTER_FILE);

		// We ignore the first element because it is the header of the commit file
		for (int i = 1; i < commits.size(); i++) {
			String[] commit = commits.get(i);

			String sha = commit[IConfiguration.COMMIT_HEADER_ID];
			File descriptionFile = new File(
					IConfiguration.Egit_Bitcoin.BASE_PATCHES_URL.getAbsolutePath() + File.separator + sha + ".json");

			if (!descriptionFile.exists()) {
				DescriptionOfACommitRetriever retriever = new DescriptionOfACommitRetriever();
				retriever.setSha(sha);
				retriever.retrieveDescription();
				String response = retriever.getResponse();
				Utils.writeToFile(descriptionFile, response);
				System.out.println("Crawling " + sha);
			} else {
				// The description of the current commit is retrieved before
				System.out.println("Crawled before, ignoring");
			}
		}
	}
}

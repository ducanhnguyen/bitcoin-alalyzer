package com.jgit.datalog;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import com.utils.Utils;

public class AllDiffEntriesReader {
	// The file containing a set of DiffEntry of commits
	private File diffEntriesFile = null;

	List<CommitLog> commitsLog = new ArrayList<CommitLog>();

	public static void main(String[] args) {
		AllDiffEntriesReader reader = new AllDiffEntriesReader();
		reader.setDiffEntriesFile(new File("./bitcoin/commit/master1-100.txt"));
		List<CommitLog> commitsLog = reader.loadCommitsLog();
		System.out.println(commitsLog.get(0));
	}

	public AllDiffEntriesReader() {
	}

	public List<CommitLog> loadCommitsLog() {
		commitsLog = new ArrayList<CommitLog>();

		if (diffEntriesFile.exists()) {
			String content = Utils.convertToString(Utils.readFileContent(diffEntriesFile));
			String[] commits = content.split(AllDiffEntriesExporter.DELIMITER_BETWEEN_COMMIT);

			for (String commit : commits) {
				CommitLog commitLog = new CommitLog();

				int posDescription = commit.indexOf(AllDiffEntriesExporter.DELIMITER_BETWEEN_DIFF);

				// Get repo, branch, and commit ID
				String description = commit.substring(0, posDescription - 1);
				{
					String[] lines = description.split("\n");
					for (String line : lines) {
						int pos = line.indexOf(":");
						if (line.startsWith(AllDiffEntriesExporter.REPOSITORY)) {
							commitLog.setRepo(line.substring(pos + 1));

						} else if (line.startsWith(AllDiffEntriesExporter.BRANCH)) {
							commitLog.setBranch(line.substring(pos + 1));

						} else if (line.startsWith(AllDiffEntriesExporter.COMMITA)) {
							commitLog.setCommitID(line.substring(pos + 1));
						}
					}
				}
				// Get diff entries
				String[] diffEntries = commit
						.substring(posDescription + AllDiffEntriesExporter.DELIMITER_BETWEEN_DIFF.length())
						.split(AllDiffEntriesExporter.DELIMITER_BETWEEN_DIFF);

				for (String diffEntry : diffEntries) {
					DiffEntryLog diffEntryLog = new DiffEntryLog();

					String[] lines = diffEntry.split("\n");

					for (String line : lines) {
						int pos = line.indexOf(":");

						if (line.startsWith(AllDiffEntriesExporter.CHANGED_FILE)) {
							diffEntryLog.setNameOfChangedFile(line.substring(pos + 1));

						} else if (line.startsWith(AllDiffEntriesExporter.IDENTIFIER)) {
							String[] identifiers = line.substring(pos + 1).split(",");
							diffEntryLog.setIdentifier(Utils.convertToList(identifiers));
						}
					}
				}
			}
		}
		return commitsLog;
	}

	public void setDiffEntriesFile(File diffEntriesFile) {
		this.diffEntriesFile = diffEntriesFile;
	}

	public File getDiffEntriesFile() {
		return diffEntriesFile;
	}
}

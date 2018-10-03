package com.jgit.datalog;

import java.io.File;
import java.io.IOException;

import com.fasterxml.jackson.core.JsonGenerationException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.jgit.datalog.object.DiffEntriesOfARepo;

public class JsonDiffEntriesOfARepoReader {
	// The file containing a set of DiffEntry of commits
	private File diffEntriesFolder = null;

	public static void main(String[] args) {
		JsonDiffEntriesOfARepoReader reader = new JsonDiffEntriesOfARepoReader();
		reader.setDiffEntriesFolder(new File("./bitcoin/commit"));
		DiffEntriesOfARepo diffEntriesOfARepo = reader.readData();
		System.out.println(diffEntriesOfARepo.getBranch());
		System.out.println(diffEntriesOfARepo.getCommits().size());
	}

	public JsonDiffEntriesOfARepoReader() {
	}

	public DiffEntriesOfARepo readData() {
		DiffEntriesOfARepo output = null;

		ObjectMapper mapper = new ObjectMapper();

		if (getDiffEntriesFolder().exists()) {
			File[] listOfFiles = getDiffEntriesFolder().listFiles();

			for (File file : listOfFiles)

				if (file.getName().endsWith(".json")) {

					DiffEntriesOfARepo outputItem = null;
					try {
						// Convert JSON string from file to Object
						outputItem = mapper.readValue(file, DiffEntriesOfARepo.class);

					} catch (JsonGenerationException e) {
						e.printStackTrace();
					} catch (JsonMappingException e) {
						e.printStackTrace();
					} catch (IOException e) {
						e.printStackTrace();
					}

					if (output == null)
						output = outputItem;
					else if (outputItem != null)
						output.getCommits().addAll(outputItem.getCommits());
				}

		}
		return output;
	}

	public void setDiffEntriesFolder(File diffEntriesFile) {
		this.diffEntriesFolder = diffEntriesFile;
	}

	public File getDiffEntriesFolder() {
		return diffEntriesFolder;
	}
}

package com.jgit.datalog;

import java.io.File;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.concurrent.TimeUnit;

import com.fasterxml.jackson.core.JsonGenerationException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.jgit.comparison.object.AllSimilarCommitPairs;
import com.jgit.comparison.object.SimilarCommitPair;

public class JsonSimilarCommitReader {

	private File jsonFile = null;

	public static void main(String[] args) {
		JsonSimilarCommitReader reader = new JsonSimilarCommitReader();
		reader.setJsonFile(new File("./similarity.json"));
		AllSimilarCommitPairs commitPairs = reader.readData();

//		System.out.println("Size of commit pairs = "+commitPairs.size());
		System.out.println("maximum num of changed files = " + reader.getMaximumNumofChangedFiles(commitPairs));
		reader.displaySummary(commitPairs);
//		System.out.println(reader.filterCommitPairsByMinumSizeOfChangedFiles(commitPairs, 150));
//		System.out.println(reader.filterCommitPairsByMinumDays(commitPairs, 2000).size());
	}

	public JsonSimilarCommitReader() {
	}

	public AllSimilarCommitPairs readData() {
		AllSimilarCommitPairs commitPairs = new AllSimilarCommitPairs();

		if (getJsonFile().exists()) {

			ObjectMapper mapper = new ObjectMapper();
			try {
				// Convert JSON string from file to Object
				commitPairs = mapper.readValue(jsonFile, AllSimilarCommitPairs.class);

			} catch (JsonGenerationException e) {
				e.printStackTrace();
			} catch (JsonMappingException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}

		return commitPairs;
	}

	public void displaySummary(AllSimilarCommitPairs commitPairs) {
		for (int sizeOfChangedFile = 0; sizeOfChangedFile <= this
				.getMaximumNumofChangedFiles(commitPairs); sizeOfChangedFile++) {
			int numofPairs = 0;
			for (SimilarCommitPair commitPair : commitPairs) {
				if (commitPair.getChangedFile().size() == sizeOfChangedFile)
					numofPairs++;
			}

			if (numofPairs != 0)
				System.out.println(
						"Size of similar changed file = " + sizeOfChangedFile + ". Found in " + numofPairs + " pairs");
		}

	}

	public AllSimilarCommitPairs filterCommitPairsByMinumSizeOfChangedFiles(AllSimilarCommitPairs commitPairs,
			int minimumSizeOfChangedFiles) {
		AllSimilarCommitPairs output = new AllSimilarCommitPairs();
		for (SimilarCommitPair pair : commitPairs)
			if (pair.getChangedFile().size() >= minimumSizeOfChangedFiles) {
				output.add(pair);
			}
		return output;
	}

	public AllSimilarCommitPairs filterCommitPairsByMinumDays(AllSimilarCommitPairs commitPairs, int minimumDay) {
		AllSimilarCommitPairs output = new AllSimilarCommitPairs();
		for (SimilarCommitPair pair : commitPairs)
			if (daysBetweenUsingJoda(pair.getDateOfCommitA(), pair.getDateOfCommitB()) >= minimumDay) {
				output.add(pair);
				System.out.println(pair);
			}
		return output;
	}

	public long daysBetweenUsingJoda(Date d1, Date d2) {
		long day = 0;
		SimpleDateFormat myFormat = new SimpleDateFormat("dd MM yyyy");
		String inputString1 = String.format("%s %s %s", d1.getDay(), d1.getMonth(), d1.getYear());
		String inputString2 = String.format("%s %s %s", d2.getDay(), d2.getMonth(), d2.getYear());

		try {
			Date date1 = myFormat.parse(inputString1);
			Date date2 = myFormat.parse(inputString2);
			long diff = date2.getTime() - date1.getTime();
			day = TimeUnit.DAYS.convert(diff, TimeUnit.MILLISECONDS);
		} catch (ParseException e) {
			e.printStackTrace();
		}
		return day;
	}

	public int getMaximumNumofChangedFiles(AllSimilarCommitPairs commitPairs) {
		int maximumNumofChangedFiles = 0;
		for (SimilarCommitPair commitPair : commitPairs) {
			if (commitPair.getChangedFile().size() > maximumNumofChangedFiles)
				maximumNumofChangedFiles = commitPair.getChangedFile().size();
		}
		return maximumNumofChangedFiles;
	}

	public void setJsonFile(File diffEntriesFile) {
		this.jsonFile = diffEntriesFile;
	}

	public File getJsonFile() {
		return jsonFile;
	}

}

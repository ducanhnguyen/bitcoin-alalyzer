package com.jgit.comparison.object;

public class SimilarChangedFilePair {
	private String nameOfChangedFile = "";// in both commit A and commit B
	private double identifiersSimilarity = 0.0f;

	@Override
	public String toString() {
		return String.format("+ %s;\tsimilarity: %s\n", nameOfChangedFile, identifiersSimilarity);
	}

	public String getNameOfChangedFile() {
		return nameOfChangedFile;
	}

	public void setNameOfChangedFile(String nameOfChangedFile) {
		this.nameOfChangedFile = nameOfChangedFile;
	}

	public double getIdentifiersSimilarity() {
		return identifiersSimilarity;
	}

	public void setIdentifiersSimilarity(double similarity) {
		this.identifiersSimilarity = similarity;
	}
}
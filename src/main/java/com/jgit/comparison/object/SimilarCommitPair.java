package com.jgit.comparison.object;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class SimilarCommitPair {
	private String commitA = ""; // a commit in repo A
	private String messageOfCommitA = "";
	private Date DateOfCommitA = null;
	private int numOfChangedFileInA = 0;

	private String commitB = "";// a commit in a different repo
	private String messageOfCommitB = "";
	private Date DateOfCommitB = null;
	private int numOfChangedFileInB = 0;

	private List<SimilarChangedFilePair> changedFile = new ArrayList<SimilarChangedFilePair>();

	private double commitSimilarity = 0.0f;

	public String getCommitA() {
		return commitA;
	}

	public void setCommitA(String commitA) {
		this.commitA = commitA;
	}

	public String getCommitB() {
		return commitB;
	}

	public void setCommitB(String commitB) {
		this.commitB = commitB;
	}

	@Override
	public String toString() {
		return String.format("\n-------------------------\n" + /**/
				"commit A: %s\t(%s changed files, %s)\n" + /**/
				"commit B: %s\t(%s changed files, %s)\n" + /**/
				"commit similarity: %s\n" + /**/
				"similar files:\n" + "%s", /**/
				commitA, numOfChangedFileInA, DateOfCommitA.toString(), /**/
				commitB, numOfChangedFileInB, DateOfCommitB.toString(), /**/
				commitSimilarity, changedFile);
	}

	public List<SimilarChangedFilePair> getChangedFile() {
		return changedFile;
	}

	public void setChangedFile(List<SimilarChangedFilePair> changedFile) {
		this.changedFile = changedFile;
	}

	public double getCommitSimilarity() {
		return commitSimilarity;
	}

	public void setCommitSimilarity(double similarity) {
		this.commitSimilarity = similarity;
	}

	public String getMessageOfCommitA() {
		return messageOfCommitA;
	}

	public void setMessageOfCommitA(String messageOfCommitA) {
		this.messageOfCommitA = messageOfCommitA;
	}

	public Date getDateOfCommitA() {
		return DateOfCommitA;
	}

	public void setDateOfCommitA(Date dateOfCommitA) {
		DateOfCommitA = dateOfCommitA;
	}

	public int getNumOfChangedFileInA() {
		return numOfChangedFileInA;
	}

	public void setNumOfChangedFileInA(int numOfChangedFileInA) {
		this.numOfChangedFileInA = numOfChangedFileInA;
	}

	public String getMessageOfCommitB() {
		return messageOfCommitB;
	}

	public void setMessageOfCommitB(String messageOfCommitB) {
		this.messageOfCommitB = messageOfCommitB;
	}

	public Date getDateOfCommitB() {
		return DateOfCommitB;
	}

	public void setDateOfCommitB(Date dateOfCommitB) {
		DateOfCommitB = dateOfCommitB;
	}

	public int getNumOfChangedFileInB() {
		return numOfChangedFileInB;
	}

	public void setNumOfChangedFileInB(int numOfChangedFileInB) {
		this.numOfChangedFileInB = numOfChangedFileInB;
	}
}
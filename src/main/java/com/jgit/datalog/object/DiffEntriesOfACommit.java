package com.jgit.datalog.object;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class DiffEntriesOfACommit {
	private String currentCommit;
	private int numOfChangedFile = 0;
	private Date date;
	private String message;
	private List<ChangedFileOfACommit> changedFiles = new ArrayList<ChangedFileOfACommit>();

	public String getCurrentCommit() {
		return currentCommit;
	}

	public void setCurrentCommit(String currentCommit) {
		this.currentCommit = currentCommit;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String messageOfCurrentCommit) {
		this.message = messageOfCurrentCommit;
	}

	public List<ChangedFileOfACommit> getChangedFiles() {
		return changedFiles;
	}

	public void setChangedFiles(List<ChangedFileOfACommit> changedFiles) {
		this.changedFiles = changedFiles;
	}

	public Date getDate() {
		return date;
	}

	public void setDate(Date date) {
		this.date = date;
	}

	public void setNumOfChangedFile(int numOfChangedFile) {
		this.numOfChangedFile = numOfChangedFile;
	}

	public int getNumOfChangedFile() {
		return numOfChangedFile;
	}
}

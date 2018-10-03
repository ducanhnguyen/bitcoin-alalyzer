package com.jgit.datalog.object;

import java.util.ArrayList;
import java.util.List;

public class DiffEntriesOfACommit {
	private String currentCommit;
	private String messageOfCurrentCommit;

	private String comparedCommit;
	private String messageOfComparedCommit;

	private List<ChangedFileOfACommit> changedFiles = new ArrayList<ChangedFileOfACommit>();

	public String getCurrentCommit() {
		return currentCommit;
	}

	public void setCurrentCommit(String currentCommit) {
		this.currentCommit = currentCommit;
	}

	public String getMessageOfCurrentCommit() {
		return messageOfCurrentCommit;
	}

	public void setMessageOfCurrentCommit(String messageOfCurrentCommit) {
		this.messageOfCurrentCommit = messageOfCurrentCommit;
	}

	public List<ChangedFileOfACommit> getChangedFiles() {
		return changedFiles;
	}

	public void setChangedFiles(List<ChangedFileOfACommit> changedFiles) {
		this.changedFiles = changedFiles;
	}

	public String getComparedCommit() {
		return comparedCommit;
	}

	public void setComparedCommit(String comparedCommit) {
		this.comparedCommit = comparedCommit;
	}

	public String getMessageOfComparedCommit() {
		return messageOfComparedCommit;
	}

	public void setMessageOfComparedCommit(String messageOfComparedCommit) {
		this.messageOfComparedCommit = messageOfComparedCommit;
	}
}

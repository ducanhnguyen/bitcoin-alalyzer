package com.jgit.datalog.object;

public class ChangedFileOfACommit {
	private int nameFileHash;
	private int[] identifiersHash = null; // present in an increase order
	private String nameFileInString = "";

	public int getNameFileHash() {
		return nameFileHash;
	}

	public void setNameFileHash(int nameFile) {
		this.nameFileHash = nameFile;
	}

	public int[] getIdentifiersHash() {
		return identifiersHash;
	}

	public void setIdentifiersHash(int[] identifiers) {
		this.identifiersHash = identifiers;
	}

	public String getNameFileInString() {
		return nameFileInString;
	}

	public void setNameFileInString(String nameFileInString) {
		this.nameFileInString = nameFileInString;
	}
}

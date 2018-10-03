package com.jgit.datalog.object;

import java.util.ArrayList;
import java.util.List;

public class ChangedFileOfACommit {
	private String nameFile;
	private List<String> identifiers = new ArrayList<String>();

	public String getNameFile() {
		return nameFile;
	}

	public void setNameFile(String nameFile) {
		this.nameFile = nameFile;
	}

	public List<String> getIdentifiers() {
		return identifiers;
	}

	public void setIdentifiers(List<String> identifiers) {
		this.identifiers = identifiers;
	}
}

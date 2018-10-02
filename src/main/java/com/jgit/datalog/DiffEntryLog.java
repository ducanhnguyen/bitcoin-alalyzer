package com.jgit.datalog;

import java.util.ArrayList;
import java.util.List;

/**
 * 
 * Also present a changed file as well
 * 
 * @author adn0019
 *
 */

public class DiffEntryLog {
	private String codeSnippet = "";
	private List<String> identifier = new ArrayList<String>();
	private String nameOfChangedFile = "";

	public static void main(String[] args) {

	}

	public DiffEntryLog() {

	}

	public String getCodeSnippet() {
		return codeSnippet;
	}

	public void setCodeSnippet(String codeSnippet) {
		this.codeSnippet = codeSnippet;
	}

	public List<String> getIdentifier() {
		return identifier;
	}

	public void setIdentifier(List<String> identifier) {
		this.identifier = identifier;
	}

	public String getNameOfChangedFile() {
		return nameOfChangedFile;
	}

	public void setNameOfChangedFile(String nameOfChangedFile) {
		this.nameOfChangedFile = nameOfChangedFile;
	}

	@Override
	public String toString() {
		String output = "";
		output += nameOfChangedFile + "\n";
		output += identifier + "\n";
		return output;
	}
}

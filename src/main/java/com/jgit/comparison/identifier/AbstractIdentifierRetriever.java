package com.jgit.comparison.identifier;

import java.util.ArrayList;
import java.util.List;

import com.utils.Utils;

public abstract class AbstractIdentifierRetriever {
	protected List<String> identifiers = new ArrayList<String>();
	protected String codeSnippet = new String();

	public abstract List<String> findIdentifiers();

	public List<String> getIdentifiers() {
		return identifiers;
	}

	public void setIdentifiers(List<String> identifiers) {
		this.identifiers = identifiers;
	}

	public void setCodeSnippet(String src) {
		this.codeSnippet = src;
	}

	public String getCodeSnippet() {
		return codeSnippet;
	}

}

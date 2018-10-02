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

	public static final List<String> KEYWORDS = Utils.convertToList(new String[] {
			// from C/C++ description
			"auto", "break", "case", "char", "const", "continue", "default", "do", "double", "else", "enum", "extern",
			"float", "for", "goto", "if", "int", "long", "register", "return", "short", "signed", "sizeof", "static",
			"struct", "switch", "typedef", "union", "unsigned", "void", "volatile", "while",
			// add more - data types
			"std", "vector", "bool", "string", "false", "true",
			// exception
			"try", "catch", "exception",
			// console
			"printf", "cin", "cout" });
	public static final List<String> PREFIX_KEYWORDS = Utils.convertToList(new String[] { "std::" });
}

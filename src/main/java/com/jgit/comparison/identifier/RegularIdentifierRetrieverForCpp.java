package com.jgit.comparison.identifier;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.lang3.StringUtils;

import com.utils.Utils;

public class RegularIdentifierRetrieverForCpp extends AbstractIdentifierRetriever {

	public static void main(String[] args) {
		RegularIdentifierRetrieverForCpp splitter = new RegularIdentifierRetrieverForCpp();
		splitter.setCodeSnippet(
				"for (auto chain : { CBaseChainParams::MAIN, CBaseChainParams::TESTNET, CBaseChainParams::REGTEST }) {");
		System.out.println(splitter.findIdentifiers());
	}

	public List<String> findIdentifiers() {
		identifiers = new ArrayList<String>();

		String normalizedCodeSnippet = Utils.removeComments(getCodeSnippet());
		normalizedCodeSnippet = Utils.removeQuote(normalizedCodeSnippet);

		if (normalizedCodeSnippet.length() > 0) {
			Pattern p = Pattern.compile("[a-zA-Z0-9_]+(::[a-zA-Z0-9_]+)*"); // Example: std::ios_base::failure, abc,
																			// ab_c
			Matcher m1 = p.matcher(normalizedCodeSnippet);

			while (m1.find()) {
				String candidateToken = m1.group();

				boolean isValid = true;

				if (CPP_KEYWORDS.contains(candidateToken))
					isValid = false;

				if (isValid && StringUtils.isNumeric(candidateToken))
					isValid = false;

				if (isValid)
					for (String prefixKeyword : PREFIX_KEYWORDS)
						if (candidateToken.startsWith(prefixKeyword)) {
							isValid = false;
							break;
						}

				if (isValid)
					identifiers.add(candidateToken);

			}
		}

		return identifiers;
	}

	public static final List<String> CPP_KEYWORDS = Utils.convertToList(new String[] {
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

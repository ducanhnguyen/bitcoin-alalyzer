package com.jgit.comparison.identifier;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.lang3.StringUtils;

import com.utils.Utils;

public class RegularIdentifierRetrieverForCommitMessage extends AbstractIdentifierRetriever {

	public static void main(String[] args) {
		RegularIdentifierRetrieverForCommitMessage splitter = new RegularIdentifierRetrieverForCommitMessage();
		splitter.setCodeSnippet("unused variables in shell scripts.");
		System.out.println(splitter.findIdentifiers());
	}

	public List<String> findIdentifiers() {
		identifiers = new ArrayList<String>();

		if (getCodeSnippet().length() > 0) {
			Pattern p = Pattern.compile("[a-zA-Z0-9_-]+"); // Example: std::ios_base::failure, abc,
																			// ab_c
			Matcher m1 = p.matcher(getCodeSnippet());

			while (m1.find()) {
				String candidateToken = m1.group();

				boolean isValid = true;

				if (MESSAGE_KEYWORDS.contains(candidateToken))
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

	public static final List<String> MESSAGE_KEYWORDS = Utils
			.convertToList(new String[] { "a", "an", "the", "it", ".", "to","for" });
	public static final List<String> PREFIX_KEYWORDS = Utils.convertToList(new String[] { "std::" });
}

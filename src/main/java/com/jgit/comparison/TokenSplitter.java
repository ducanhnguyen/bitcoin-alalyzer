package com.jgit.comparison;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.lang3.StringUtils;

import com.utils.Utils;

public class TokenSplitter {
	private String src = new String();
	private List<String> tokens = new ArrayList<String>();

	public static void main(String[] args) {
		TokenSplitter splitter = new TokenSplitter();
		splitter.setSrc("SetupDummyInputs(CBasicKeyStore &keystoreRet, CCoinsViewCache &coinsRet) { std::vector<CMutableTransaction> dummyTransactions; dummyTransactions.resize(2);");
		System.out.println(splitter.getTokens());
	}

	public List<String> getTokens() {
		List<String> tokens = new ArrayList<String>();
		if (src.length() > 0) {
			Pattern p = Pattern.compile("[a-zA-Z0-9_]+");
			Matcher m1 = p.matcher(src);

			while (m1.find()) {
				String candidateToken = m1.group();

				if (!keywords.contains(candidateToken) && !StringUtils.isNumeric(candidateToken)) {

					tokens.add(candidateToken);
				}
			}
		}
		return tokens;
	}

	public void setSrc(String src) {
		this.src = src;
	}

	public String getSrc() {
		return src;
	}

	public static final List<String> keywords = Utils.convertToList(new String[] { "auto", "break", "case", "char",
			"const", "continue", "default", "do", "double", "else", "enum", "extern", "float", "for", "goto", "if",
			"int", "long", "register", "return", "short", "signed", "sizeof", "static", "struct", "switch", "typedef",
			"union", "unsigned", "void", "volatile", "while",
			// add
			"std", "vector" });
}

package com.jgit.comparison.identifier;

import static org.junit.Assert.assertArrayEquals;

import org.junit.Test;

import com.utils.Utils;

public class RegularIdentifierRetrieverTest {

	@Test
	public void test1() {
		AbstractIdentifierRetriever splitter = new RegularIdentifierRetriever();
		splitter.setCodeSnippet(
				"for (auto chain : { CBaseChainParams::MAIN, CBaseChainParams::TESTNET, CBaseChainParams::REGTEST }) {");
		splitter.findIdentifiers();
		assertArrayEquals(new String[] { "chain", "CBaseChainParams::MAIN", "CBaseChainParams::TESTNET",
				"CBaseChainParams::REGTEST" }, Utils.convertToStringArray(splitter.getIdentifiers()));

	}

	@Test
	public void test2() {
		AbstractIdentifierRetriever splitter = new RegularIdentifierRetriever();
		splitter.setCodeSnippet("for (int seek_start : {0, 5}) {");
		splitter.findIdentifiers();
		assertArrayEquals(new String[] { "seek_start" }, Utils.convertToStringArray(splitter.getIdentifiers()));

	}

	@Test
	public void test3() {
		AbstractIdentifierRetriever splitter = new RegularIdentifierRetriever();
		splitter.setCodeSnippet("try{} catch (const std::ios_base::failure& e) {}");
		splitter.findIdentifiers();
		assertArrayEquals(new String[] { "e" }, Utils.convertToStringArray(splitter.getIdentifiers()));

	}
}

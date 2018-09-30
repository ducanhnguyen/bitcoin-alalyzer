package com.sample.similarity;

import java.util.LinkedList;

/**
 * https://github.com/google/diff-match-patch/wiki/Language:-Java
 * 
 * @author Duc-Anh Nguyen
 *
 */
public class CharacterDiff {
	public static void main(String args[]) {
		diff_match_patch dmp = new diff_match_patch();
		LinkedList<diff_match_patch.Diff> diff = dmp.diff_main("H2ello World.", "Hello1 World.", false);
		dmp.diff_cleanupSemantic(diff);
		System.out.println(diff);
	}
}

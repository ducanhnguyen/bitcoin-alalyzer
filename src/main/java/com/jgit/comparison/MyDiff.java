package com.jgit.comparison;

import java.util.ArrayList;
import java.util.List;

import com.jgit.object.CommitJgit;

/**
 * Represent a diff when comparing two commits
 * 
 * @author Duc-Anh Nguyen
 *
 */
public class MyDiff {
	// Two compared commits
	private CommitJgit commitA;
	private CommitJgit commitB;

	private List<String> sourcecodeBeforeBeingChanged = new ArrayList<String>();
	private List<String> sourcecodeAfterBeingChanged = new ArrayList<String>();

	// The code snippet are changed in two commits. These code snippet are before
	// being changed.
	private List<String> changedCodeSnippet = new ArrayList<String>();

	private String nameChangeFile = new String();

	/**
	 * 
	 * Example <br/>
	 * 
	 * <pre>
	diff --git a/test/functional/test_runner.py b/test/functional/test_runner.py
	index 28437f8..d996046 100755
	--- a/test/functional/test_runner.py
	+++ b/test/functional/test_runner.py
	&#64;@ -285,11 +285,13 @@
	
	 if args.exclude:
	-        exclude_tests = [re.sub("\.py$", "", test) + (".py" if ".py" not in test else "") for test in args.exclude.split(',')]
	+        exclude_tests = [test.split('.py')[0] for test in args.exclude.split(',')]
	 * </pre>
	 */
	private List<String> diff = new ArrayList<String>();

	public MyDiff() {

	}

	public List<String> getSourcecodeAfterBeingChanged() {
		return sourcecodeAfterBeingChanged;
	}

	public void setSourcecodeAfterBeingChanged(List<String> sourcecodeAfterBeingChanged) {
		this.sourcecodeAfterBeingChanged = sourcecodeAfterBeingChanged;
	}

	public List<String> getSourcecodeBeforeBeingChanged() {
		return sourcecodeBeforeBeingChanged;
	}

	public void setSourcecodeBeforeBeingChanged(List<String> sourcecodeBeforeBeingChanged) {
		this.sourcecodeBeforeBeingChanged = sourcecodeBeforeBeingChanged;
	}

	public List<String> getDiff() {
		return diff;
	}

	public void setDiff(List<String> diff) {
		this.diff = diff;
	}

	public CommitJgit getCommitA() {
		return commitA;
	}

	public void setCommitA(CommitJgit commitA) {
		this.commitA = commitA;
	}

	public CommitJgit getCommitB() {
		return commitB;
	}

	public void setCommitB(CommitJgit commitB) {
		this.commitB = commitB;
	}

	public String getNameChangeFile() {
		return nameChangeFile;
	}

	public void setNameChangeFile(String nameChangeFile) {
		this.nameChangeFile = nameChangeFile;
	}

	public List<String> getChangedCodeSnippet() {
		return changedCodeSnippet;
	}

	public void setChangedCodeSnippet(List<String> changedCodeSnippet) {
		this.changedCodeSnippet = changedCodeSnippet;
	}
}

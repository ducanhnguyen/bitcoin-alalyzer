package com.samples.jgit;

import java.io.File;
import java.io.IOException;

import org.eclipse.jgit.api.Git;
import org.eclipse.jgit.api.errors.GitAPIException;
import org.eclipse.jgit.lib.Repository;
import org.eclipse.jgit.revwalk.RevCommit;
import org.eclipse.jgit.storage.file.FileRepositoryBuilder;

/**
 * References:
 * https://github.com/centic9/jgit-cookbook/blob/master/src/main/java/org/dstadler/jgit/porcelain/ShowLog.java
 * 
 * @author Duc-Anh Nguyen
 *
 */
public class CommitRetriever {
	public static void main(String[] args) throws IOException, GitAPIException {
		FileRepositoryBuilder repositoryBuilder = new FileRepositoryBuilder();
		Repository repository = repositoryBuilder.setGitDir(new File("W:/repo/.git")).readEnvironment()
				// scan up the file system tree
				.findGitDir().setMustExist(true).build();
		Git git = new Git(repository);
		Iterable<RevCommit> logs = git.log().call();
		for (RevCommit rev : logs) {
			System.out.println("Commit: " + rev.getName() + ". message=" + rev.getFullMessage());
		}
	}
}

package com.samples.jgit;

import java.io.File;
import java.io.IOException;
import java.util.List;

import org.eclipse.jgit.api.Git;
import org.eclipse.jgit.api.ListBranchCommand.ListMode;
import org.eclipse.jgit.api.errors.GitAPIException;
import org.eclipse.jgit.lib.Ref;
import org.eclipse.jgit.lib.Repository;
import org.eclipse.jgit.storage.file.FileRepositoryBuilder;

/**
 * Reference:
 * https://github.com/centic9/jgit-cookbook/blob/master/src/main/java/org/dstadler/jgit/porcelain/ListBranches.java
 * 
 * @author Duc-Anh Nguyen
 *
 */
public class BranchRetriever {
	public static void main(String[] args) throws IOException, GitAPIException {
		FileRepositoryBuilder repositoryBuilder = new FileRepositoryBuilder();
		Repository repository = repositoryBuilder.setGitDir(new File("W:/repo/.git")).readEnvironment()
				// scan up the file system tree
				.findGitDir().setMustExist(true).build();
		System.out.println("Listing local branches:");

		Git git = new Git(repository);
		List<Ref> call = git.branchList().call();
		for (Ref ref : call) {
			System.out.println("Branch: " + ref + " " + ref.getName() + " " + ref.getObjectId().getName());
		}

		System.out.println("Now including remote branches:");
		call = git.branchList().setListMode(ListMode.ALL).call();
		for (Ref ref : call) {
			System.out.println("Branch: " + ref + " " + ref.getName() + " " + ref.getObjectId().getName());
		}
	}
}

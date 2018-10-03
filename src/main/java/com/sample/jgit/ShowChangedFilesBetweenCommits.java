package com.sample.jgit;

import java.io.File;
import java.io.IOException;
import java.util.List;

import org.eclipse.jgit.api.Git;
import org.eclipse.jgit.api.errors.GitAPIException;
import org.eclipse.jgit.diff.DiffEntry;
import org.eclipse.jgit.lib.ObjectId;
import org.eclipse.jgit.lib.ObjectReader;
import org.eclipse.jgit.lib.Repository;
import org.eclipse.jgit.storage.file.FileRepositoryBuilder;
import org.eclipse.jgit.treewalk.CanonicalTreeParser;

import com.utils.IConfiguration;

/**
 * https://github.com/centic9/jgit-cookbook/blob/master/src/main/java/org/dstadler/jgit/porcelain/ShowChangedFilesBetweenCommits.java
 * 
 * @author adn0019
 *
 */
public class ShowChangedFilesBetweenCommits {

	public static void main(String[] args) throws IOException, GitAPIException {
		// get repository
		File repoFile = IConfiguration.Jgit_Bitcoin.BITCOIN_REPO;
		FileRepositoryBuilder repositoryBuilder = new FileRepositoryBuilder();
		Repository repository = null;
		try {
			repository = repositoryBuilder.setGitDir(repoFile).readEnvironment()
					// scan up the file system tree
					.findGitDir().setMustExist(true).build();
		} catch (IOException e) {
			e.printStackTrace();
		}

		// The {tree} will return the underlying tree-id instead of the commit-id
		// itself!
		// For a description of what the carets do see e.g.
		// http://www.paulboxley.com/blog/2011/06/git-caret-and-tilde
		// This means we are selecting the parent of the parent of the parent of the
		// parent of current HEAD and
		// take the tree-ish of it
		
		ObjectId oldHead = repository.resolve("47cc478ab82120835f3e851f6781bad267cb2890");
		ObjectId head = repository.resolve("6f95c58ab52f67d557c541c08b84a58b91e81a56");

		System.out.println("Printing diff between tree: " + oldHead + " and " + head);

		// prepare the two iterators to compute the diff between
		ObjectReader reader = repository.newObjectReader();
		CanonicalTreeParser oldTreeIter = new CanonicalTreeParser();
		oldTreeIter.reset(reader, oldHead);
		CanonicalTreeParser newTreeIter = new CanonicalTreeParser();
		newTreeIter.reset(reader, head);

		// finally get the list of changed files
		Git git = new Git(repository);
		List<DiffEntry> diffs = git.diff().setNewTree(newTreeIter).setOldTree(oldTreeIter).call();
		for (DiffEntry entry : diffs) {
			System.out.println("Entry: " + entry);
		}

		System.out.println("Done");
	}
}

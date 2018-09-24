package com.samples.jgit;

import java.io.File;
import java.io.IOException;

import org.eclipse.jgit.lib.ObjectId;
import org.eclipse.jgit.lib.ObjectLoader;
import org.eclipse.jgit.lib.Repository;
import org.eclipse.jgit.revwalk.RevCommit;
import org.eclipse.jgit.revwalk.RevTree;
import org.eclipse.jgit.revwalk.RevWalk;
import org.eclipse.jgit.storage.file.FileRepositoryBuilder;
import org.eclipse.jgit.treewalk.TreeWalk;
import org.eclipse.jgit.treewalk.filter.PathFilter;

public class LocalRepoOpenner {
	public LocalRepoOpenner() {
	}

	public static void main(String[] args) throws IOException {
		FileRepositoryBuilder repositoryBuilder = new FileRepositoryBuilder();
		Repository repository = repositoryBuilder.setGitDir(new File("W:/repo/.git")).readEnvironment()
				// scan up the file system tree
				.findGitDir().setMustExist(true).build();

		// Analyze commit
		String commitSha = "2ebc4cb9caa10a1bf5c17670cedf1be33aaa355a";
		ObjectId commitId = repository.resolve(commitSha);

		RevWalk revWalk = new RevWalk(repository);
		RevCommit commit = revWalk.parseCommit(commitId);
		RevTree tree = commit.getTree();

		// now try to find a specific file
		TreeWalk treeWalk = new TreeWalk(repository);
		treeWalk.addTree(tree);
		treeWalk.setRecursive(true);

		treeWalk.setFilter(PathFilter.create("README.md"));
		if (!treeWalk.next()) {
			throw new IllegalStateException("Did not find expected file 'README.md'");
		}

		ObjectId objectId = treeWalk.getObjectId(0);
		ObjectLoader loader = repository.open(objectId);
		// and then one can the loader to read the file
		loader.copyTo(System.out);

		revWalk.close();
	}
}

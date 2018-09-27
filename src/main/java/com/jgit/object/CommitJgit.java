package com.jgit.object;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.io.IOUtils;
import org.eclipse.jgit.api.Git;
import org.eclipse.jgit.api.errors.GitAPIException;
import org.eclipse.jgit.diff.DiffEntry;
import org.eclipse.jgit.errors.IncorrectObjectTypeException;
import org.eclipse.jgit.lib.ObjectId;
import org.eclipse.jgit.lib.ObjectLoader;
import org.eclipse.jgit.lib.ObjectReader;
import org.eclipse.jgit.lib.Repository;
import org.eclipse.jgit.revwalk.RevCommit;
import org.eclipse.jgit.revwalk.RevTree;
import org.eclipse.jgit.revwalk.RevWalk;
import org.eclipse.jgit.treewalk.CanonicalTreeParser;
import org.eclipse.jgit.treewalk.TreeWalk;
import org.eclipse.jgit.treewalk.filter.PathFilter;

import com.jgit.CommitRetriever;
import com.utils.IConfiguration;

/**
 * Represent a RevCommit in JGit
 * 
 * @author Duc-Anh Nguyen
 *
 */
public class CommitJgit {
	private CommitsJgit parent = null;
	private RevCommit commit = null;

	public static void main(String[] args) throws IOException, GitAPIException {
		/**
		 * Get all commits in the current branch
		 */
		CommitRetriever retriever = new CommitRetriever();
		retriever.setRepoFile(IConfiguration.Jgit_Bitcoin.BITCOIN_REPO);
		CommitsJgit commits = retriever.getAllCommits();
		System.out.println("There are " + commits.size() + " commits in " + commits.getBranch());

		/**
		 * Display the first commit
		 */
		System.out.println("------------------\n");
		CommitJgit firstCommit = commits.get(0);
		System.out.println("The first commit: " + firstCommit.getCommit().getName() + "\n\""
				+ firstCommit.getCommit().getFullMessage() + "\"");

		/**
		 * Get all changed files
		 */
		System.out.println("------------------\n");
		CommitJgit previousCommit = commits.get(1);
		List<ChangedFile> changedFiles = firstCommit.getChangedFiles(previousCommit);
		System.out.println("changed files: " + changedFiles.toString());

		/**
		 * Get differences between two commits of a changed file
		 */
		System.out.println("------------------\n");
		ChangedFile firstChanegdFile = changedFiles.get(1);
		System.out.println("after being changed:");
		for (String diff : firstChanegdFile.getSourcecodeAfterBeingChanged())
			System.out.println(diff);
		System.out.println("before being changed:");
		for (String diff : firstChanegdFile.getDifferences())
			System.out.println(diff);

		/**
		 * Get source code of a changed file
		 */
//		System.out.println("------------------\n");
//		String changedFile = "src/Makefile.am";
//		List<String> content = firstCommit.getSourcecodeFile(changedFile);
//		System.out.println("Content of " + changedFile + ":\n");
//		for (String s : content)
//			System.out.println(s);
	}

	/**
	 * Get all of the changed file of the current commits compared to a previous
	 * commit
	 * 
	 * @param previousCommit A previous commit you want to compare
	 * @return
	 */
	public List<ChangedFile> getChangedFiles(CommitJgit previousCommit) {
		List<ChangedFile> changedFiles = new ArrayList<ChangedFile>();
		Repository repository = getParent().getRepository();

		if (repository != null) {
			ObjectReader reader = repository.newObjectReader();
			// Create the parser of the current commit
			CanonicalTreeParser newTreeIter = new CanonicalTreeParser();
			try {
				newTreeIter.reset(reader, this.getCommit().getTree());
			} catch (IncorrectObjectTypeException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			}

			// Create the parser of a previous commit
			CanonicalTreeParser oldTreeIter = new CanonicalTreeParser();
			try {
				oldTreeIter.reset(reader, previousCommit.getCommit().getTree());
			} catch (IncorrectObjectTypeException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			}

			// Get the list of changed files
			Git git = new Git(repository);
			List<DiffEntry> diffs = new ArrayList<DiffEntry>();
			try {
				diffs = git.diff().setNewTree(newTreeIter).setOldTree(oldTreeIter).call();
			} catch (GitAPIException e) {
				e.printStackTrace();
			}

			for (DiffEntry diff : diffs) {
				ChangedFile changedFile = new ChangedFile();
				changedFile.setComparedCommit(previousCommit);
				changedFile.setCurrentCommit(this);
				changedFile.setDiffEntry(diff);
				changedFiles.add(changedFile);
			}
		}
		return changedFiles;
	}

	/**
	 * Get source code of a changed file in the current commit
	 * 
	 * @param changedFile the changed file you want to get its source code
	 * @return
	 * @throws IOException
	 */
	public List<String> getSourcecodeFile(String changedFile) throws IOException {
		List<String> sourcecodeFile = new ArrayList<String>();

		if (this.getParent() != null) {
			Repository repo = this.getParent().getRepository();

			if (changedFile != null && changedFile.length() > 0 && repo != null) {
				RevWalk revWalk = new RevWalk(repo);
				RevCommit commit = revWalk.parseCommit(this.getCommit().getId());
				RevTree tree = commit.getTree();

				// now try to find a specific file
				TreeWalk treeWalk = new TreeWalk(repo);
				treeWalk.addTree(tree);
				treeWalk.setRecursive(true);
				treeWalk.setFilter(PathFilter.create(changedFile));
				if (!treeWalk.next()) {
					throw new IllegalStateException("Did not find expected file");
				}

				ObjectId objectId = treeWalk.getObjectId(0);
				ObjectLoader loader = repo.open(objectId);

				// load the content of the file into a stream
				ByteArrayOutputStream stream = new ByteArrayOutputStream();
				loader.copyTo(stream);

				sourcecodeFile = IOUtils.readLines(new ByteArrayInputStream(stream.toByteArray()), "UTF-8");
				revWalk.dispose();
			}
		}
		return sourcecodeFile;
	}

	public RevCommit getCommit() {
		return commit;
	}

	public void setCommit(RevCommit commit) {
		this.commit = commit;
	}

	public void setParent(CommitsJgit parent) {
		this.parent = parent;
	}

	public CommitsJgit getParent() {
		return parent;
	}

}

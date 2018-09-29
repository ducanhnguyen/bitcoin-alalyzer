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
import org.eclipse.jgit.errors.MissingObjectException;
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
		System.out.println("------------------");
		int commitID = 1;
		CommitJgit aCommit = commits.get(commitID);
		System.out.println(
				"The commit: " + aCommit.getCommit().getName() + "\n\"" + aCommit.getCommit().getFullMessage() + "\"");

		/**
		 * Get all changed files
		 */
		System.out.println("------------------");
		CommitJgit previousCommit = commits.get(commitID + 1);
		List<ChangedFile> changedFiles = aCommit.getChangedFiles(previousCommit);
		System.out.println("changed files: " + changedFiles.toString());

		/**
		 * Get differences between two commits of a changed file
		 */
		System.out.println("------------------");
		ChangedFile aChangedFile = changedFiles.get(1);

		System.out.println(aChangedFile.getNameFile() + " after being changed:");
		for (String diff : aChangedFile.getMinimizeCodeSnippetAfterBeingChanged())
			System.out.println(diff);

		System.out.println("------------------");
		System.out.println(aChangedFile.getNameFile() + " before being changed:");
		for (String diff : aChangedFile.getMinimizeCodeSnippetBeforeBeingChanged())
			System.out.println(diff);

		System.out.println("------------------");
		System.out.println("All changes in " + aChangedFile.getNameFile() + " in summary");
		for (String diff : aChangedFile.getDifferences())
			System.out.println(diff);

		/**
		 * Get source code of a changed file
		 */
		System.out.println("------------------\n");
		System.out.println("Content of " + aChangedFile.getNameFile() + " after being changed:\n");
		List<String> content = aCommit.getSourcecodeFileAfterBeingChanged(aChangedFile.getNameFile());
		for (String s : content)
			System.out.println(s);

		System.out.println("------------------\n");
		System.out.println("Content of " + aChangedFile.getNameFile() + " before being changed:\n");
		List<String> content2 = aCommit.getSourcecodeFileBeforeBeingChanged(aChangedFile.getNameFile());
		for (String s : content2)
			System.out.println(s);

		/**
		 * 
		 */
		System.out.println("------------------\n");
		System.out.println("Here are code snippets changed in the current commit");
		List<String> content3 = aChangedFile.getLinesBeforeBeingChanged();
		for (String s : content3)
			System.out.println(s);

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
			CanonicalTreeParser currentTree = new CanonicalTreeParser();
			try {
				currentTree.reset(reader, this.getCommit().getTree());
			} catch (IncorrectObjectTypeException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			}

			// Create the parser of a previous commit
			CanonicalTreeParser previousTree = new CanonicalTreeParser();
			try {
				previousTree.reset(reader, previousCommit.getCommit().getTree());
			} catch (IncorrectObjectTypeException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			}

			// Get the list of changed files
			Git git = new Git(repository);
			List<DiffEntry> diffEntries = new ArrayList<DiffEntry>();
			try {
				diffEntries = git.diff().setNewTree(currentTree).setOldTree(previousTree).call();
			} catch (GitAPIException e) {
				e.printStackTrace();
			}

			for (DiffEntry diffEntry : diffEntries) {
				ChangedFile changedFile = new ChangedFile();
				changedFile.setPreviousCommit(previousCommit);
				changedFile.setCurrentCommit(this);
				changedFile.setDiffEntry(diffEntry);

				changedFiles.add(changedFile);
			}
		}
		return changedFiles;
	}

	/**
	 * Get source code of a changed file in the current commit after it is modified.
	 * 
	 * @param changedFile the changed file you want to get its source code
	 * @return
	 * @throws IOException
	 */
	public List<String> getSourcecodeFileAfterBeingChanged(String changedFile) {
		List<String> sourcecodeFile = new ArrayList<String>();

		if (this.getParent() != null) {
			Repository repo = this.getParent().getRepository();

			if (changedFile != null && changedFile.length() > 0 && repo != null) {
				try {
					RevWalk revWalk = new RevWalk(repo);
					RevCommit commit = revWalk.parseCommit(this.getCommit().getId());
					RevTree tree = commit.getTree();

					// now try to find a specific file
					TreeWalk treeWalk = new TreeWalk(repo);
					treeWalk.addTree(tree);
					treeWalk.setRecursive(true);
					treeWalk.setFilter(PathFilter.create(changedFile));
					if (!treeWalk.next()) {
						System.out.println("Did not find expected file ");
						System.out.println("\t" + commit.getId().getName());
						System.out.println("\t" + changedFile);
						throw new IllegalStateException("Did not find expected file");
					}

					ObjectId objectId = treeWalk.getObjectId(0);
					ObjectLoader loader = repo.open(objectId);

					// load the content of the file into a stream
					ByteArrayOutputStream stream = new ByteArrayOutputStream();
					loader.copyTo(stream);

					sourcecodeFile = IOUtils.readLines(new ByteArrayInputStream(stream.toByteArray()), "UTF-8");
					revWalk.dispose();
				} catch (Exception e) {
//					e.printStackTrace();
				}
			}
		}
		return sourcecodeFile;
	}

	/**
	 * Get source code of a changed file in the current commit after it is modified.
	 * 
	 * @param changedFile the changed file you want to get its source code
	 * @return
	 * @throws IOException
	 */
	public List<String> getSourcecodeFileBeforeBeingChanged(String changedFile) {
		List<String> sourcecodeFile = new ArrayList<String>();

		if (this.getParent() != null) {
			Repository repo = this.getParent().getRepository();

			if (changedFile != null && changedFile.length() > 0 && repo != null) {
				try {
					RevWalk revWalk = new RevWalk(repo);
					RevCommit commit = null;
					try {
						commit = revWalk.parseCommit(this.getCommit().getParent(0).getId()/* the previous commit */);
					} catch (MissingObjectException e) {
						e.printStackTrace();
					} catch (IncorrectObjectTypeException e) {
						e.printStackTrace();
					} catch (IOException e) {
						e.printStackTrace();
					}
					RevTree tree = commit.getTree();

					// now try to find a specific file
					TreeWalk treeWalk = new TreeWalk(repo);
					treeWalk.addTree(tree);
					treeWalk.setRecursive(true);
					treeWalk.setFilter(PathFilter.create(changedFile));
					if (!treeWalk.next()) {
						System.out.println("Did not find expected file ");
						System.out.println("\t" + commit.getId().getName());
						System.out.println("\t" + changedFile);
						throw new IllegalStateException("Did not find expected file");
					}

					ObjectId objectId = treeWalk.getObjectId(0);
					ObjectLoader loader = repo.open(objectId);

					// load the content of the file into a stream
					ByteArrayOutputStream stream = new ByteArrayOutputStream();
					loader.copyTo(stream);

					sourcecodeFile = IOUtils.readLines(new ByteArrayInputStream(stream.toByteArray()), "UTF-8");
					revWalk.dispose();
				} catch (Exception e) {
//					e.printStackTrace();
				}
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

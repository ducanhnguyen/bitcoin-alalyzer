package com.samples.jgit;

import java.io.File;

import org.eclipse.jgit.api.Git;
import org.eclipse.jgit.api.errors.GitAPIException;
import org.eclipse.jgit.api.errors.InvalidRemoteException;
import org.eclipse.jgit.api.errors.TransportException;

public class PublicRepositoryCloner {
	public PublicRepositoryCloner() {

	}

	public static void main(String[] args) {
		final String REPO = "https://github.com/ducanhnguyen/CDT-HelloWorld.git";
		final File outputFolder = new File("W:/repo");

		if (outputFolder.exists())
			System.out.println("The folder exists!");
		else {
			// Clone to a completely new folder
			try {
				Git.cloneRepository().setURI(REPO).setDirectory(outputFolder)
						// clone all branches
						.setCloneAllBranches(true).call();
			} catch (InvalidRemoteException e) {
				e.printStackTrace();

			} catch (TransportException e) {
				e.printStackTrace();

			} catch (GitAPIException e) {
				e.printStackTrace();
			}
		}
	}
}

package com.samples;

import java.io.IOException;
import java.text.MessageFormat;

import org.eclipse.egit.github.core.Repository;
import org.eclipse.egit.github.core.client.GitHubClient;
import org.eclipse.egit.github.core.service.RepositoryService;

import com.bitcoin.object.IPersonalInformation;

/**
 * Print a user's repositories
 */
public class Authentication {

	/**
	 * Prints a user's repositories
	 *
	 * @param args
	 * @throws IOException
	 */
	public static void main(String[] args) throws IOException {
		// Basic authentication
		GitHubClient client = new GitHubClient();
		client.setCredentials(IPersonalInformation.USERNAME, IPersonalInformation.PASSWORD);
		RepositoryService repoService = new RepositoryService(client);

		for (Repository repo : repoService.getRepositories())
			System.out.println(MessageFormat.format("{0}- created on {1}", repo.getName(), repo.getCreatedAt()));
	}
}
package featureextraction;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.eclipse.jgit.api.errors.GitAPIException;

import com.jgit.CommitRetriever;
import com.jgit.comparison.JaccardSimilarityForMessageCommit;
import com.jgit.object.CommitJgit;
import com.jgit.object.CommitsJgit;
import com.utils.IConfiguration;

public class FeatureExtracter extends CommitRetriever {
	public static void main(String[] args) throws IOException, GitAPIException {
		// Get all commits in the current branch
		FeatureExtracter instance = new FeatureExtracter();
		instance.setRepoFile(IConfiguration.Jgit_Bitcoin.BITCOIN_REPO);
		CommitsJgit commits = instance.getAllCommits();
		System.out.println("There are " + commits.size() + " commits in " + commits.getBranch());

		// Get the commits adding new features
		if (commits.size() > 0) {
			Features addedFeatures = instance.getAddedFeatures(commits);
			addedFeatures.removeRedundantFeatures();
			System.out.println(addedFeatures.size());

			Features removedFeatures = instance.getRemovedFeatures(commits);
			removedFeatures.removeRedundantFeatures();
			System.out.println(removedFeatures.size());

			// Find duplicated features
			for (Feature addedFeature : addedFeatures)
				for (Feature removedFeature : removedFeatures)
					if (addedFeature.getCommit().getCommit().getId() != removedFeature.getCommit().getCommit()
							.getId()) {
						JaccardSimilarityForMessageCommit sim = new JaccardSimilarityForMessageCommit();
						sim.setSource(addedFeature.getFeature());
						sim.setTarget(removedFeature.getFeature());

						double similarity = sim.compare();
						final double THRESHOLD = 0.3f;
						if (similarity >= THRESHOLD) {
							System.out.println("-------\nPair:\n" + addedFeature + "\n" + removedFeature);
						}
					}
		}
	}

	public Features getAddedFeatures(CommitsJgit commits) {
		Features addedFeatures = new Features();

		for (CommitJgit commit : commits)
			if (commit.getCommit().getFullMessage().contains("Add ")) {
				boolean isMergeCommit = false;

				if (commit.getCommit().getFullMessage().startsWith("Merge "))
					isMergeCommit = true;

				List<String> featureDescriptions = getAddedFeatureDescription(commit.getCommit().getFullMessage());

				for (String featureDescription : featureDescriptions) {
					AddedFeature feature = new AddedFeature();
					feature.setCommit(commit);
					feature.setFeature(featureDescription);
					feature.setMergeCommit(isMergeCommit);
					addedFeatures.add(feature);
				}
			}
		return addedFeatures;
	}

	public Features getRemovedFeatures(CommitsJgit commits) {
		Features removedFeatures = new Features();
		for (CommitJgit commit : commits)
			if (commit.getCommit().getFullMessage().contains("Remove ")
					|| commit.getCommit().getFullMessage().contains("Delete ")) {
				boolean isMergeCommit = false;
				if (commit.getCommit().getFullMessage().startsWith("Merge "))
					isMergeCommit = true;

				List<String> featureDescriptions = getRemovedFeatureDescription(commit.getCommit().getFullMessage());

				for (String featureDescription : featureDescriptions) {
					RemovedFeature feature = new RemovedFeature();
					feature.setCommit(commit);
					feature.setFeature(featureDescription);
					feature.setMergeCommit(isMergeCommit);
					removedFeatures.add(feature);
				}
			}
		// Analyze features set
		return removedFeatures;

	}

	private List<String> getAddedFeatureDescription(String fullMessageInCommit) {
		List<String> addedFeatureDescription = new ArrayList<String>();
		// Example:" Add 'about' information to `-version` output"
		Pattern pattern = Pattern.compile("[aA]dd[s]*\\s+(.+)");

		Matcher matcher = pattern.matcher(fullMessageInCommit);

		while (matcher.find()) {
			addedFeatureDescription.add(matcher.group(1));
		}

		Collections.sort(addedFeatureDescription);
		return addedFeatureDescription;
	}

	private List<String> getRemovedFeatureDescription(String fullMessageInCommit) {
		List<String> removedFeatureDescription = new ArrayList<String>();

		// Example:" Remove 'about' information"
		Pattern pattern = Pattern.compile("[rR]emove[s]*\\s+(.+)");
		Matcher matcher = pattern.matcher(fullMessageInCommit);

		while (matcher.find()) {
			removedFeatureDescription.add(matcher.group(1));
		}
		// Example:" Delete 'about' information"
		pattern = Pattern.compile("[dD]elete[s]*\\s+(.+)");
		matcher = pattern.matcher(fullMessageInCommit);

		while (matcher.find()) {
			removedFeatureDescription.add("Remove " + matcher.group(1));
		}
		Collections.sort(removedFeatureDescription);
		return removedFeatureDescription;
	}
}

package featureextraction;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class Features extends ArrayList<Feature> {
	public static final String[] NON_FEATURES_TOKENS = new String[] { "Add test", "Add a test", "Add testcase",
			"Add a testcase", "Add test case", "Add a test case", "Add comment", "Add a comment", "Add documentation",
			"Add note", "Add a note", "Add -benchmark", "Add benchmark" };

	/**
	 * Remove the merge commit. Remove the commits not adding new features
	 */
	public void removeRedundantFeatures() {
		for (int i = this.size() - 1; i >= 0; i--) {
			Feature feature = this.get(i);
			if (feature.isMergeCommit())
				this.remove(i);
			else
				for (String nonfeaturesToken : NON_FEATURES_TOKENS)
					if (feature.getFeature().startsWith(nonfeaturesToken)) {
						this.remove(i);
						continue;
					}
		}
	}

	@Deprecated
	public Map<String, Features> getDuplicatedFeatures(boolean ignoreNonFeatureFeatures) {
		Map<String, Features> duplicatedFeatures = new HashMap<String, Features>();

		for (int i = 0; i < this.size() - 1; i++) {
			Feature featureA = this.get(i);

			// The current commit is Merge commit, ignore!
			if (featureA.isMergeCommit()) {
				// nothing to do
				continue;
			} else {
				// should be ignored
				boolean ignore = false;
				if (ignoreNonFeatureFeatures)
					for (String nonfeaturesToken : NON_FEATURES_TOKENS)
						if (featureA.getFeature().startsWith(nonfeaturesToken)) {
							ignore = true;
							break;
						}

				// if the current commit is not ignored,
				if (!ignore)
					for (int j = i + 1; j < this.size(); j++) {

						Feature featureB = this.get(j);

						if (featureA.equals(featureB) && !featureA.getCommit().getCommit().getId()
								.equals(featureB.getCommit().getCommit().getId())) {
							Features duplicatedPair = new Features();
							duplicatedPair.add(featureA);
							duplicatedPair.add(featureB);

							boolean duplicatedBefore = duplicatedFeatures.containsKey(featureA.getFeature());

							if (duplicatedBefore) {
								duplicatedFeatures.get(featureA.getFeature()).add(featureA);
								duplicatedFeatures.get(featureA.getFeature()).add(featureB);
							} else {
								duplicatedFeatures.put(featureA.getFeature(), duplicatedPair);
							}

						}
					}
			}
		}
		return duplicatedFeatures;
	}

}

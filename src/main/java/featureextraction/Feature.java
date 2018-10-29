package featureextraction;

import com.jgit.object.CommitJgit;

/**
 * A developer may add new features to the software. This feature is described
 * in commit messages. <br/>
 * For example: <br/>
 * "Add paytxfee to getinfo output
 * 
 * git-svn-id: https://bitcoin.svn.sourceforge.net/svnroot/bitcoin/trunk@178
 * 1a98c847-1fd6-4fd8-948a-caf3550aa51b"
 * 
 * In this case, the new feature is paytxfee.
 * 
 * @author Duc Anh Nguyen
 *
 */
public class Feature {
	private CommitJgit commit;

	private String feature;

	private boolean isMergeCommit = false;

	public void setFeature(String feature) {
		this.feature = feature;
	}

	public String getFeature() {
		return feature;
	}

	public void setCommit(CommitJgit commit) {
		this.commit = commit;
	}

	public CommitJgit getCommit() {
		return commit;
	}

	@Override
	public boolean equals(Object arg0) {
		if (arg0 instanceof Feature) {
			Feature cast = (Feature) arg0;
			if (cast.getFeature().equals(this.getFeature()))
				return true;
			else
				return false;
		} else
			return false;
	}

	public boolean isMergeCommit() {
		return isMergeCommit;
	}

	public void setMergeCommit(boolean isMergeCommit) {
		this.isMergeCommit = isMergeCommit;
	}

}

package featureextraction;

public class RemovedFeature extends Feature {
	@Override
	public String toString() {
		return "[Removed] Commit id: " + this.getCommit().getCommit().getId().getName() + "; Feature: " + getFeature()
				+ "\n";
	}
}

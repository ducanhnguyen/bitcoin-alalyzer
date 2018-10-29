package featureextraction;

public class AddedFeature extends Feature {
	@Override
	public String toString() {
		return "[Added] Commit id: " + this.getCommit().getCommit().getId().getName() + "; Feature: " + getFeature() + "\n";
	}
}

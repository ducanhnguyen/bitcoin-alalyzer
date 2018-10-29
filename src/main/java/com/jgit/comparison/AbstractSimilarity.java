package com.jgit.comparison;

public abstract class AbstractSimilarity {

	private String source = new String();
	private String target = new String();

	public static void main(String[] args) {
		JaccardSimilarityForCpp sim = new JaccardSimilarityForCpp();
		sim.setSource("xxx yyy zzz zzzk");
		sim.setTarget("xxx yyy zzz");
		System.out.println(sim.compare());
	}

	public abstract double compare();

	public void setSource(String source) {
		this.source = source;
	}

	public String getSource() {
		return source;
	}

	public void setTarget(String target) {
		this.target = target;
	}

	public String getTarget() {
		return target;
	}

}

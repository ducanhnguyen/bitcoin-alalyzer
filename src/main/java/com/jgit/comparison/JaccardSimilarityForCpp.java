package com.jgit.comparison;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import com.jgit.comparison.identifier.RegularIdentifierRetrieverForCpp;

public class JaccardSimilarityForCpp extends AbstractSimilarity {
	public static void main(String[] args) {
		JaccardSimilarityForCpp sim = new JaccardSimilarityForCpp();
		sim.setSource("xxx yyy zzz zzzk");
		sim.setTarget("xxx yyy zzz");
		System.out.println(sim.compare());
	}

	public JaccardSimilarityForCpp() {

	}

	public double compare() {
		double sim = 0.0f;
		if (getSource().length() > 0 && getTarget().length() > 0) {
			RegularIdentifierRetrieverForCpp tokenSplitter = new RegularIdentifierRetrieverForCpp();

			tokenSplitter.setCodeSnippet(getSource());
			
			List<String> tokensinSource = tokenSplitter.findIdentifiers();
//			System.out.println("Tokens A: " + tokensinSource);

			tokenSplitter.setCodeSnippet(getTarget());
			List<String> tokensinTarget = tokenSplitter.findIdentifiers();
//			System.out.println("Tokens B: " + tokensinTarget);

			Set<String> unionSet = new HashSet<String>();
			unionSet.addAll(tokensinTarget);
			unionSet.addAll(tokensinSource);

			Set<String> intersectionSet = new HashSet<String>();
			for (String token : tokensinSource)
				if (tokensinTarget.contains(token)) {
					intersectionSet.add(token);
				}

//			System.out.println("Intersection set = " + intersectionSet.toString());
//			System.out.println("Union set = " + unionSet.toString());
			sim = intersectionSet.size() * 1.0f / unionSet.size();
		}
		return sim;
	}
}
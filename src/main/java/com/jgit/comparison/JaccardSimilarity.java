package com.jgit.comparison;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import com.jgit.comparison.identifier.RegularIdentifierRetriever;

public class JaccardSimilarity extends AbstractSimilarity {
	public static void main(String[] args) {
		JaccardSimilarity sim = new JaccardSimilarity();
//	sim.setSource("x[0].vout[0].scriptPubKey << ToByteVector(key[0].GetPubKey()) << OP_CHECKSIG;");
//	sim.setTarget("y[0].vout[1].scriptPubKey << ToByteVector(key[1].GetPubKey()) << OP_CHECKSIG;");

		sim.setSource("xxx yyy zzz zzzk");
		sim.setTarget("xxx yyy zzz");
		System.out.println(sim.compare());
	}

	public JaccardSimilarity() {

	}

	public double compare() {
		double sim = 0.0f;
		if (getSource().length() > 0 && getTarget().length() > 0) {
			RegularIdentifierRetriever tokenSplitter = new RegularIdentifierRetriever();

			tokenSplitter.setCodeSnippet(getSource());
			List<String> tokensinSource = tokenSplitter.getIdentifiers();
			System.out.println("Tokens A: " + tokensinSource);

			tokenSplitter.setCodeSnippet(getTarget());
			List<String> tokensinTarget = tokenSplitter.getIdentifiers();
			System.out.println("Tokens B: " + tokensinTarget);

			Set<String> unionSet = new HashSet<String>();
			unionSet.addAll(tokensinTarget);
			unionSet.addAll(tokensinSource);

			Set<String> intersectionSet = new HashSet<String>();
			for (String token : tokensinSource)
				if (tokensinTarget.contains(token)) {
					intersectionSet.add(token);
				}

			System.out.println("Intersection set = " + intersectionSet.toString());
			System.out.println("Union set = " + unionSet.toString());
			sim = intersectionSet.size() * 1.0f / unionSet.size();
		}
		return sim;
	}
}

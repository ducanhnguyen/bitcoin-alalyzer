package com.sample.similarity;

import net.ricecode.similarity.JaroWinklerStrategy;
import net.ricecode.similarity.SimilarityStrategy;
import net.ricecode.similarity.StringSimilarityService;
import net.ricecode.similarity.StringSimilarityServiceImpl;

public class JaroWinkletStrategy {

	public static void main(String[] args) {
		SimilarityStrategy strategy = new JaroWinklerStrategy();
		String target = "abc xyz";
		String source = "abcd xyz";
		StringSimilarityService service = new StringSimilarityServiceImpl(strategy);
		System.out.println(service.score(source, target));

	}
}

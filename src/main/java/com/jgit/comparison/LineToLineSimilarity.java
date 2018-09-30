package com.jgit.comparison;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import com.utils.Utils;

public class LineToLineSimilarity extends AbstractSimilarity {
	public static void main(String[] args) {
		LineToLineSimilarity sim = new LineToLineSimilarity();
		sim.setSource("xxx\nyyy\nzzz\nttt");
		sim.setTarget("xxx\nyyy\nzzz");
		System.out.println(sim.compare());
	}

	public LineToLineSimilarity() {

	}

	public double compare() {
		double sim = 0.0f;

		if (getSource().length() > 0 && getTarget().length() > 0) {
			List<String> src = Utils.convertToList(getSource().split("\n"));
			List<String> target = Utils.convertToList(getTarget().split("\n"));

			Set<String> union = new HashSet();
			union.addAll(target);
			union.addAll(src);

			Set<String> intersection = new HashSet();

			for (int i = src.size() - 1; i >= 0; i--)
				for (int j = target.size() - 1; j >= 0; j--) {

					if (target.get(j).equals(src.get(i))) {
						intersection.add(target.get(j));
						src.remove(i);
						target.remove(j);
						break;
					}
				}

			System.out.println("intersection:" + intersection);
			System.out.println("union:" + union);
			sim = intersection.size() * 1.0f / union.size();
		}

		return sim;
	}
}

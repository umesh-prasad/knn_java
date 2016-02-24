package com.flipkart.knn;

import java.util.Comparator;

class ResultRecord {
	ResultRecord() {
	}

	ResultRecord(String id, double distance) {
		this.id = id;
		this.distance = distance;
	}

	public String id;
	public double distance;

	static class RRComparator implements Comparator<ResultRecord> {
		@Override
		public int compare(ResultRecord a, ResultRecord b) {
			if (a.distance == b.distance)
				return 0;
			else {
				if (a.distance > b.distance)
					return 1;
				else
					return -1;
			}
		}

	}
}

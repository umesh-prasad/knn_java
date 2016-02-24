package com.flipkart.knn;

import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;

import org.apache.lucene.util.PriorityQueue;

import com.opencsv.CSVReader;

public class Shard {

	Shard(int threadCount, int embeddingSize) {
		this.threadCount = threadCount;
		this.embeddingSize = embeddingSize;
	}

	public void insert(String id, float[] embedding) {
		ids.add(id);
		embeddings.put(id, embedding);
		countDataPoints++;
	}

	public int getCountDataPoints() {
		return countDataPoints;
	}

	public ArrayList<ResultRecord> knn(float[] query, int k) throws InterruptedException {
		int totalWorkload = ids.size();
		int workload = totalWorkload / threadCount;
		int assignedWorkload = 0;
		ArrayList<Worker> workers = new ArrayList<Worker>();
		ArrayList<Thread> threads = new ArrayList<Thread>();
		for (int i = 0; i < threadCount; i++) {
			int endpoint = assignedWorkload + workload;
			if (assignedWorkload + workload > totalWorkload)
				endpoint = totalWorkload;
			Worker currWorker = new Worker(assignedWorkload, endpoint, flatEmbeddings, flatIds, query, k);
			Thread currThread = new Thread(currWorker);
			workers.add(currWorker);
			threads.add(currThread);
			assignedWorkload += workload;
		}

		for (Thread thread : threads) {
			thread.start();
		}
		for (Thread thread : threads) {
			thread.join();
		}

		MyQueue minGlobal = new MyQueue(k);
		for (Worker worker : workers) {
			while (worker.localMin.size() > 0) {
				minGlobal.insertWithOverflow(worker.localMin.pop());
			}
		}

		ArrayList<ResultRecord> result = new ArrayList<ResultRecord>();
		while (minGlobal.size() > 0) {
			result.add(minGlobal.pop());
		}
		return result;
	}

	public void loadFromFile(String path) throws IOException {
		CSVReader reader = null;
		try {
			reader = new CSVReader(new FileReader(path));
			String[] nextLine;
			while ((nextLine = reader.readNext()) != null) {
				String id = null;
				float[] currEmbedding = new float[this.embeddingSize];
				for (int i = 0; i < nextLine.length; i++) {
					if (i == 0) {
						id = nextLine[i];
					} else {
						currEmbedding[i - 1] = Float.parseFloat(nextLine[i]);
					}
				}
				insert(id, currEmbedding);
			}
		} finally {
			reader.close();
		}
	}

	public void index() {
		int countEmbeddings = embeddings.size();
		flatEmbeddings = new float[countEmbeddings][];
		flatIds = new String[countEmbeddings];
		int counter = 0;
		for (Map.Entry<String, float[]> entry : embeddings.entrySet()) {
			flatIds[counter] = entry.getKey();
			flatEmbeddings[counter] = entry.getValue();
			counter++;
		}
	}

	public ArrayList<ResultRecord> loadTest(float[] embedding, int k) throws InterruptedException {
		return knn(embedding, k);
	}

	public float[] getRandomEmbedding() {
		Random randomGenerator = new Random();
		randomGenerator.setSeed(42);
		int index = randomGenerator.nextInt(ids.size());
		String id = ids.get(index);
		float[] embedding = embeddings.get(id);
		return embedding;
	}

	class MyQueue extends PriorityQueue<ResultRecord> {

		public MyQueue(int maxSize) {
			super(maxSize);
		}

		@Override
		protected boolean lessThan(ResultRecord a, ResultRecord b) {
			if (a.distance == b.distance)
				return false;
			else {
				if (a.distance > b.distance)
					return true;
				else
					return false;
			}
		}

	}

	class Worker implements Runnable {
		Worker(int start, int end, float[][] flatEmbeddings, String[] flatIds, float[] query, int k) {
			this.start = start;
			this.end = end;
			this.flatEmbeddings = flatEmbeddings;
			this.flatIds = flatIds;
			this.query = query;
			this.k = k;
			localMin = new MyQueue(k);
		}

		public void run() {
			for (int i = start; i < end; i++) {
				String currId = flatIds[i];
				float[] currEmbedding = flatEmbeddings[i];
				double currDistance = euclidianDistance(currEmbedding);
				if (localMin.size() < k) {
					ResultRecord currResultRecord = new ResultRecord(currId, currDistance);
					localMin.add(currResultRecord);
				} else {
					ResultRecord last = localMin.top();
					if (last.distance > currDistance) {
						last.distance = currDistance;
						last.id = currId;
						localMin.updateTop();
					}
				}
			}
		}

		public double getMin() {
			return min;
		}

		private double euclidianDistance(float[] given) {
			float total = 0;
			for (int i = 0; i < given.length; i++) {
				float a = given[i];
				float b = query[i];
				float c = a - b;
				total += c * c;
			}
			return Math.sqrt(total);
		}

		private int start;
		private int end;
		private float[][] flatEmbeddings;
		private String[] flatIds;
		private float[] query;
		private double min;
		public org.apache.lucene.util.PriorityQueue<ResultRecord> localMin;
		public int k;
	}

	private int countDataPoints = 0;
	private ArrayList<String> ids = new ArrayList<String>();
	private HashMap<String, float[]> embeddings = new HashMap<String, float[]>();
	private float[][] flatEmbeddings;
	private String[] flatIds;
	private int threadCount;
	private int embeddingSize;
}

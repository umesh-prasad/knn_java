package com.flipkart.knn;

import org.apache.commons.math3.stat.descriptive.DescriptiveStatistics;

import java.io.IOException;
import java.util.ArrayList;

public class KNN
{
    public static void main( String[] args )
    {
        int vectorSize = Integer.parseInt(args[0]);
        int k = Integer.parseInt(args[1]);
        int callsToMake = Integer.parseInt(args[2]);
        int threadCount = Integer.parseInt(args[3]);
        String inputFilePath = args[4];

        DescriptiveStatistics descriptiveStatistics = new DescriptiveStatistics();

        Shard shard = new Shard(threadCount, vectorSize);
        try {
            System.out.println("Reading Data.. ");
            shard.loadFromFile(inputFilePath);
            shard.index();
            System.out.println("Finished Reading.");
            System.out.println("Running (a dot implies a call got completed)");
            for(int i = 0; i < callsToMake; i++) {
                System.out.print(".");
                float[] embedding = shard.getRandomEmbedding();

                long startTime = System.currentTimeMillis();
                ArrayList<ResultRecord> result = shard.loadTest(embedding, k);
                long stopTime = System.currentTimeMillis();

                long elapsedTime = stopTime - startTime;
                descriptiveStatistics.addValue(elapsedTime);
            }
            System.out.println("");
            System.out.println("Number of Data Points:" + shard.getCountDataPoints());
            System.out.println("Number of Invocations:" + descriptiveStatistics.getN());
            System.out.println("P95:" + descriptiveStatistics.getPercentile(0.95));
            System.out.println("Mean:" + descriptiveStatistics.getMean());
            System.out.println("SD:" + descriptiveStatistics.getStandardDeviation());
            System.out.println("Max:" + descriptiveStatistics.getMax());
            System.out.println("Min:" + descriptiveStatistics.getMin());
            System.out.println("P95:" + descriptiveStatistics.getPercentile(0.95));

        }
        catch (IOException | InterruptedException e) {
            System.out.println(e);
        }
    }
}
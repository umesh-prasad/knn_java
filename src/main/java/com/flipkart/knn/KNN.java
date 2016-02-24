package com.flipkart.knn;

import java.io.IOException;
import java.util.ArrayList;

public class KNN
{
    public static void main( String[] args )
    {
        int threads = Integer.parseInt(args[0]);
        Shard shard = new Shard(threads, 512);
        try {
            shard.loadFromFile(args[1]);
            shard.index();
            System.out.println("Finished Reading.. ");
            int iterations = Integer.parseInt(args[2]);
            for(int i = 0; i < iterations; i++) {
                float[] embedding = shard.getRandomEmbedding();

                long startTime = System.currentTimeMillis();
                ArrayList<ResultRecord> result = shard.loadTest(embedding, 1000);
                long stopTime = System.currentTimeMillis();

                long elapsedTime = stopTime - startTime;
                System.out.println(elapsedTime);
            }
        }
        catch (IOException | InterruptedException e) {
            System.out.println(e);
        }
    }
}
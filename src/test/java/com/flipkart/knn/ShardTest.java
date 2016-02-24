package com.flipkart.knn;

import org.junit.Test;

import java.util.ArrayList;

import static org.junit.Assert.*;

public class ShardTest {

    @Test
    public void testAll() throws InterruptedException{
        int threads = 3;
        int vectorSize = 10;
        int dataPoints = 100;
        float baseValue = 0;
        float nearValue = 0.1f;
        float farValue = 0.2f;
        Shard shard = new Shard(threads, vectorSize);
        ArrayList<String> ids = new ArrayList<String>();
        ArrayList<float[]> embeddings = new ArrayList<float[]>();
        for(int i = 0; i < dataPoints; i++) {
            String currid = Integer.toString(i);
            float [] currEmbedding = new float[vectorSize];
            for(int j = 0; j < vectorSize; j++) {
                currEmbedding[j] = farValue;
            }
            ids.add(currid);
            embeddings.add(currEmbedding);
        }
        String currid = "result";
        float [] currEmbedding = new float[vectorSize];
        for(int j = 0; j < vectorSize; j++) {
            currEmbedding[j] = nearValue;
        }
        ids.add(currid);
        embeddings.add(currEmbedding);

        for(int i = 0; i < ids.size(); i++) {
            shard.insert(ids.get(i),embeddings.get(i));
        }

        float [] query = new float[vectorSize];
        for(int j = 0; j < vectorSize; j++) {
            query[j] = baseValue;
        }

        shard.index();
        ArrayList<ResultRecord> result = shard.knn(query, 1);
        assertEquals("result", result.get(0).id);

    }

}
package com.flipkart.knn;


import java.util.Comparator;

class ResultRecord implements Comparator<ResultRecord> {
    ResultRecord() {}
    ResultRecord(String id, double distance) {
        this.id = id;
        this.distance = distance;
    }

    public String id;
    public double distance;

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

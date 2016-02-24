# knn_java

# Build
mvn package

# Usage Example
java -cp knn-1.0-SNAPSHOT-jar-with-dependencies.jar com.flipkart.knn.KNN 512 1000 50 5 /Users/nikhil.ketkar/Desktop/embeddings.csv

int vectorSize = Integer.parseInt(args[0]);

int k = Integer.parseInt(args[1]);

int callsToMake = Integer.parseInt(args[2]);

int threadCount = Integer.parseInt(args[3]);

String inputFilePath = args[4];

# Format of data.csv
Comma delimited file with first column being the identifier, rest being the vector of floats

# knn_java

# Build
mvn package

# Usage Example

```
java -cp knn-1.0-SNAPSHOT-jar-with-dependencies.jar com.flipkart.knn.KNN 512 1000 50 5 /Users/nikhil.ketkar/Desktop/embeddings.csv
```

```
int vectorSize = Integer.parseInt(args[0]);
int k = Integer.parseInt(args[1]);
int callsToMake = Integer.parseInt(args[2]);
int threadCount = Integer.parseInt(args[3]);
String inputFilePath = args[4];
```

# Format of data.csv
Comma delimited file with first column being the identifier, rest being the vector of floats

# Sample Output

```
nikhil.ketkar@prod-fdp-mlp-devops-none-387431:~$ java -cp knn-1.0-SNAPSHOT-jar-with-dependencies.jar com.flipkart.knn.KNN 512 1000 50 15 data.csv
Reading Data..
Finished Reading.
Running (a dot implies a call got completed)
..................................................
Number of Data Points:650000
Number of Invocations:50
P95:61.0
Mean:87.83999999999999
SD:78.4995411399562
Max:600.0
Min:61.0
P95:61.0

```

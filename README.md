# knn_java

# Build
mvn package

# Usage
java -cp knn-1.0-SNAPSHOT-jar-with-dependencies.jar com.flipkart.knn.KNN 18 data.csv 5000

# Format of data.csv
Comma delimited file with first column being the identifier, rest being the vector of floats

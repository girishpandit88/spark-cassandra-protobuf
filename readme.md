Spark Cassandra Protobuf demo
-----------------------------


1. Use the `PrepareInput` object to generate test data
2. Copy the generated `input.txt` to `src/main/resources`, overwrite if already present
3. Run the `SparkCassandraJob`
    
    3.1 It loads the `input.txt` as `textfile`
    3.2 Converts into dataSet
    3.3 Save to Cassandra `student` table
    3.4 Loads it from Cassandra `student` table
    3.5 Deserialize it Cassandra blob column 


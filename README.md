The analyzer takes sequence of an algorithm execution time measurements and returns big O complexity of the algorithm.

To build run: sbt pack

To run analyzer: 
1. Target/pack/bin/complexity-analyzer(or target/pack/bin/complexity-analyzer.bat on windows).
2. Input line of numbers and press Enter. 
Or run: echo "1 2 3 4 " | ./complexity-analyzer

Input numbers should go from left to right for input arguments number.
For example sequence 1 4 8 17 24 36 52 61 83 100 means that for N=1 measured time is 1 and for N=10 measured time is 10 and complexity is O(N^2).

The algorithm considers anomalies like 1 4 9 10000 25 36. In the case it returns O(N^2) in spite of 10000 anomaly. 
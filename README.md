# Instructions

System Requirements:
1. Java version 1.8
2. Maven version 3

_Both Java and Maven needs to be added to the system path to execute below commands. 
Or provide path to mvn or java during each command execution._ 


Maven Install for Dependencies and Running Tests.

```
mvn clean install
```

For single executable Jar.

```
mvn clean compile assembly:single
```

Maven execution of the _Main_ class file.
```
### For Console Execution 
mvn exec:java

### For File Execution. file_path example. "D:\Work\parking_lot\target\classes\file_input.txt"
mvn exec:java -Dexec.args=<file_path> 
```

The program can read input from the console or from a file, or from a network socket, and will write the result to the console.

Program Arguments to run the program are as as follows.
1. To provide input from console: `CONSOLE/console?(optional)`
2. To provide input from file: `FILE/file?(Optional) <absolute_file_path>`
3. To provide input from a network socket: `NETWORK/network?(Optional) <hostname_to_listen_to> <port>`

The program can be executed as a jar from the **_bin_** folder with following two approaches.

Example to read input from a file is below. Replace the file path placeholder with actual path.
```
mvn clean install
java -cp ./../target/parkinglot-1.0-SNAPSHOT.jar com.gojek.Main <input_file_absolute_path> 
```
Or create a executable jar and then run the program. 

```
mvn clean compile assembly:single
java -jar ./../target/parkinglot-1.0-SNAPSHOT.jar <input_file_absolute_path>
```

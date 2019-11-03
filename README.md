# Instructions

Maven Install for Dependencies and Running Tests

```$xslt
mvn install
```

Maven execute
```$xslt
### For Console Execution 
mvn exec:java

### For File Execution. file_path example. "D:\Work\parking_lot\target\classes\file_input.txt"
mvn exec:java -Dexec.args=<file_path> 
```
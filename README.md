# Instructions

Maven Install for Dependencies

```$xslt
mvn install
```

Maven execute
```$xslt
### _For Console Execution_ 
mvn exec:java

### _For File Execution. file_path example. "D:\Work\parking_lot\target\classes\file_input.txt"_
mvn exec:java -Dexec.args=<file_path> 
```
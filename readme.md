# Kafka console reader
Connect to Kafka and beging reading right after start. In one thread and one instance of consumer.
## Build
```
./gradlew build
```

## Run
### From IDE
Just run main method from readerMain.kt
### From console
After build done find jar file in `rootDir/lib` (ex: `kafka-console-reader-0.0.1.jar`)
Run it by command:
with default parameters
```
java -jar pathToFile/kafka-console-reader-0.0.1.jar
```
or override part of them
```
java -jar kafka-console-reader-0.0.1.jar -Dtopic=logs -DgroupId=reader2
```
### Accepted parameters
All parameters must specify by follow pattern:
```
-DparameterName=parameterValue
```
accepted parameters:
* `topic` - topic for reading (ex: -Dtopic=application_logs). Default value = `logs`
* `groupId` - consumer's groupId (ex: -DgroupId=consoleReader). Default value = `logs-reader`
* `server` - bootstrap server address (ex: -Dserver=localhost:9092). Default value = `localhost:9092`
export MAVEN_OPTS=-Djava.library.path=target/natives
mvn clean compile exec:java -Dexec.mainClass="com.enter4ward.physics.Main" -Dexec.cleanupDaemonThreads=false

#mvn clean package
#java -Djava.library.path=target/natives -jar target/physics-jar-with-dependencies.jar

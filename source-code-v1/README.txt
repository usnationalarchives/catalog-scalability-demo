2017-10-25

to compile:
0. compile das-common first
1. cd das-common
2. mvn clean install
3. then cd into each other project and do
4. mvn clean package

to run each component
1. cd to the project 
2. ./run.sh 
*note there shoud be a soft link in each project to bin/run.sh. if not create it.
** before running, set the following properties in each application:
1. db.url 
2. db.username
3. db.password
*note db.url has this format jdbc:postgresql://my-server.com/das_dev_db?ssl=false


files:
1. bin - scripts directory
2. bin/run.sh - the run script for running any of the components
3. bin/makeclasspath.pl - used by run.sh
4. api - the api component
5. ChunkingService - the chunking console application
6. ProcessingService- the processing service console application
7. JobMonitor - the job monitor console application. 
8. das-common - the common classes
9. myconfig/applications.properties - the spring properties for each project
10. local-exports.sh - this is local and unique to each component and is sourced by run.sh
it sets the executable name and env variables like queue url etc.
11. export-override.sh - this is sourced by run.sh right before the application is launched, allowing the user
to override env settings.

the run script , bin/run.sh does the following:
1. sources local-exports.sh which is unique to each project
2. explodes the target executables into jars folder
3. generates the classpath to all the required jars and classes in the jars folder
4. sources the export-override.sh
5. runs the main spring class 



Considerations for running locally or on a server:
1. change the application.properties appropriatelly for each application.
   For example, in ProcessingService, 10,000 is a valid value for workers.concurrent.count.
   However locally, on one's laptop, this may eventualy produce OutOfMemoryException.
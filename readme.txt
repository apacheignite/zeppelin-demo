Install and start Spark
-----------------------
1. Download and unzip Spark: http://www.apache.org/dyn/closer.cgi/spark/spark-1.4.0/spark-1.4.0-bin-hadoop2.6.tgz
2. Go to Spark home folder.
3. Create env script from template: cp conf/spark-env.sh.template conf/spark-env.sh
4. Edit conf/spark-env.sh and add this line: export SPARK_JAVA_OPTS="-Dspark.executor.memory=12g"
5. Start master process: ./sbin/start-master.sh
6. Open Spark console (http://localhost:8080/) in browser and find master URL there.
7. Start worker process: ./bin/spark-class org.apache.spark.deploy.worker.Worker MASTER_URL
8. Reload console - worker should appear in the list.

Install and start Zeppelin
--------------------------
1. Clone GitHub project: https://github.com/apache/incubator-zeppelin
2. Go to Zeppelin home folder.
3. Build: mvn clean install -DskipTests
4. Define custom port not to conflict with Spark: export ZEPPELIN_PORT=9090
4. Start daemon: ./bin/zeppelin-daemon.sh start
5. Open in browser: http://localhost:9090/

Generate data
-------------
To generate the data open JsonGenerator class, customize ORG_CNT and PERSON_PER_ORG_CNT constants
if needed and run the class.
Two JSON files (organizations.json and persons.json) will be created in the root folder of this project.

Execute Spark SQL in Zeppelin
-----------------------------
Create new notebook and run the commands below.

Create organizations data frame:
    %spark val orgDf = sqlContext.read.json("PATH_TO_THIS_PROJECT/organizations.json")

Create persons data frame:
    %spark val personDf = sqlContext.read.json("PATH_TO_THIS_PROJECT/persons.json")

Register table names for data frames:
    %spark
    orgDf.registerTempTable("Organization")
    personDf.registerTempTable("Person")

Execute query:
    %sql SELECT p.name as Person, o.name as Organization FROM Person p, Organization o WHERE p.orgId = o.id AND o.id = 10

Execute Ignite SQL in Zeppelin
------------------------------
1. Start several data nodes using Node class.
2. Run LoadData class to load the data.
3. Create new notebook and execute the query:

%ignite.ignitesql SELECT p.name as Person, o.name as Organization FROM Person p, "Organizations".Organization o WHERE p.orgId = o.id AND o.id = 10

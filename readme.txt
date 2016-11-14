Install and start Spark
-----------------------
1. Download and unzip Spark: http://www-us.apache.org/dist/spark/spark-1.6.3/spark-1.6.3-bin-hadoop2.6.tgz
2. Go to Spark home folder
3. Create env script from template: cp conf/spark-env.sh.template conf/spark-env.sh
4. Edit conf/spark-env.sh and add this line: export SPARK_JAVA_OPTS="-Dspark.executor.memory=8g"
5. Start master process: ./sbin/start-master.sh
6. Open Spark console (http://localhost:8080/) in browser and find master URL there
7. Start worker process: ./bin/spark-class org.apache.spark.deploy.worker.Worker MASTER_URL
8. Reload console - worker should appear in the list

Install and start Zeppelin
--------------------------
1. Download and unzip Zeppelin: http://www.us.apache.org/dist/incubator/zeppelin/0.5.6-incubating/zeppelin-0.5.6-incubating-bin-all.tgz
2. Go to Zeppelin home folder
3. Define custom port not to conflict with Spark: export ZEPPELIN_PORT=9090
4. Start daemon: ./bin/zeppelin-daemon.sh start
5. Open in browser: http://localhost:9090/
6. Find 'anonymous' dropdown in the top right corner, click on it and select 'Interpreter'
7. Locate 'ignite' section and set JDBC URL: jdbc:ignite://localhost:11211/
8. Click 'Save' and then 'restart' buttons to restart the interpreter
9. Locate 'spark' section and set master property to your local Spark master URL
10. Click 'Save' and then 'restart' buttons to restart the interpreter

Generate data
-------------
To generate the data open JsonGenerator class, customize ORG_CNT and PERSON_PER_ORG_CNT constants
if needed and run the class.
Two JSON files (organizations.json and persons.json) will be created in the root folder of this project

Execute Spark SQL in Zeppelin
-----------------------------
1. Create new notebook
2. Create data frames:
    %spark
    val orgDf = sqlContext.read.json("PATH_TO_THIS_PROJECT/organizations.json")
    val personDf = sqlContext.read.json("PATH_TO_THIS_PROJECT/persons.json")
3. Register table names for data frames:
    %spark
    orgDf.registerTempTable("Organization")
    personDf.registerTempTable("Person")
4. Execute queries using ''%sql' prefix (see samples below)

Execute Ignite SQL in Zeppelin
------------------------------
1. Start one or more data nodes using Node class
2. Run LoadData class to load the data
3. Create new notebook and execute queries using ''%ignite.ignitesql' prefix (see samples below)

Spark sample queries
--------------------
%sql
SELECT o.name as organization, avg(p.salary) as salary
FROM Person p, Organization o
WHERE p.orgId = o.id
AND o.id IN (12653, 444144, 5673)
GROUP BY o.name
ORDER BY salary DESC

%sql
SELECT o.name as organization, e.name as employee, m.name as manager
FROM Person e, Person m, Organization o
WHERE e.managerId = m.id
AND e.orgId = o.id
AND o.name = 'Organization567890'

Ignite sample queries
--------------------
%ignite.ignitesql
SELECT o.name as Organization, avg(p.salary) as Salary
FROM Person p, "Organizations".Organization o
WHERE p.orgId = o.id
AND o.id IN (12553, 444134, 5173)
GROUP BY o.name
ORDER BY Salary ASC

%ignite.ignitesql
SELECT o.name as organization, e.name as employee, m.name as manager
FROM Person e, Person m, "Organizations".Organization o
WHERE e.managerId = m.id
AND e.orgId = o.id
AND o.name = 'Organization567856'

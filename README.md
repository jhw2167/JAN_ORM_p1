# JAN ORM

## JAN ORM Description
JAN ORM is a Java based lightweight Object Relational Mapper used for connection to a PostGreSQL Database without the need for SQL or Connection Management. 

**Authors:** *Jack Welsh, Aron Jang, Nate Opsal*<br/>
**Date Last Updated:** *06/27/2021*

## TechStack
* Java - _version 8.0_
* JUnit - _version 42.2.12_
* Mockito - _version 1.10.19_
* Apache Maven - _version 3.8.1_
* PostGreSQL - _deployed on AWS RDS_
* Git Source Code Management (on GitHub) 

## Features
### Ready to Use
* Drop then create a table from on an annotated class
* Save an instance of an object from the table as an object
* Update an entry in the database
* Delete an entry from the database
* Find entries in the database that satisfy given columns, operands, and values
* Add given constraint on columns using annotations
  * Primary Key
  * Foreign Key
  * Check
  * Unique
  * Default Value
  * Not Null
* Basic Caching
* The Following SQL Aggregate Functions:
  * Count
* Transaction Control

### Future Devlopement
* Second-Level Caching
* Implementing Complex Queries
* Implementing Stored Procedures
* The Following SQL Aggregate Functions:
  * Sum
  * Min
  * Max
  * Avg

## Getting Started  
### Clone the Application
```shell
  git clone <this-repo>
  cd JAN_ORM_p1
  mvn install
```
### Place Dependency Inside Your Project pom.xml File
```XML
  <dependency>
    <groupId>com.revature</groupId>
    <artifactId>JAN_ORM_p1</artifactId>
    <version>0.0.1-SNAPSHOT</version>
  </dependency>

```
### Add connection.properties file
Ensure that you have created this file in **src/main/resources/** and have the following lines of code:
 ``` 
  url=path/to/database
  username=username_for_database
  password=password_for_database  
  ```
## Usage
### Annotating Classes to Persist to Database
For a class to be persisted to the database, it must be a **JavaBean** and **annotated** using the following annotations:
<table>
<thead>
<tr>
<th>Annotation</th>
<th>Purpose</th>
</tr>
</thead>
<tbody>
<tr>
<td><pre lang="JAVA"><code>@Table(name = &quot;table_name&quot;)</code></td>
<td>Indicates to the ORM that this class is associated with table &#39;table_name&#39;</td>
</tr>
<tr>
<td><pre lang="JAVA"><code>@Column()</code></td>
<td>Indicates to the ORM that this field is a column in the table.<br/>The column name is automatically the field name.</td>
</tr>
<tr>
<td><pre lang="JAVA"><code>@PrimaryKey</code></td>
<td>Indicates to the ORM that this field is a Prmary key <br/>Each class can only have one primary key.</td>
</tr>
<tr>
<td><pre lang="JAVA"><code>@ForeignKey(refColumn = &quot;ref_field&quot;, refClass = &quot;ref_class&quot;)</code></td>
<td>Indicates to the ORM that this field is a Foreign Key that references class &#39;ref_class&#39; and its field &#39;ref_field&#39;</td>
</tr>
<tr>
<td><pre lang="JAVA"><code>@CheckColumn(checkStmt = &quot;check_statement&quot;)</code></td>
<td>Indicates to the ORM that this field is must undergo the &quot;check_statement&quot; before the record can be added to the table</td>
</tr>
<tr>
<td><pre lang="JAVA"><code>@NotNull()</code></td>
<td>Indicates to the ORM that this field cannot be null</td>
</tr>
<tr>
<td><pre lang="JAVA"><code>@DefaultValue(defaultValue = &quot;default_value&quot;)</code></td>
<td>Indicates to the ORM that if no value is given for this field, then the default value will populate the field</td>
</tr>
<tr>
<td><pre lang="JAVA"><code>@Unique()</code></td>
<td>Indicates to the ORM that this field has to be unique and cannot contain multiples of the same value in the table</td>
</tr>
</tbody>
</table>

### User API
#### ObjectMapper Class
*Used to Create Tables from Classes*
<table>
<thead>
<tr>
<th>ObjectMapper Class Method</th>
<th>Purpose</th>
</tr>
</thead>
<tbody>
<tr>
<td><pre lang="JAVA"><code>public static void addToModel(Class&lt;?&gt; c)</code></td>
<td>Adds the annotated class to the a local model of classes</td>
</tr>
<tr>
<td><pre lang="JAVA"><code>public static boolean removeFromModel(Class&lt;?&gt; c)</code></td>
<td>Removes the annotated class from the local model of classes</td>
</tr>
<tr>
<td><pre lang="JAVA"><code>public static boolean removeFromDB(Class&lt;?&gt; c, 
        boolean cascadeDelete)</code></td>
<td>Removes the annotated class from the local model of classes and from the database</td>
</tr>
<tr>
<td><pre lang="JAVA"><code>public static void buildDBFromModel()</code></td>
<td>Creates tables in the database from the local model of classes</td>
</tr>
</tbody>
</table>



#### ObjectQuery Class
*Used to query the database*
<table>
<thead>
<tr>
<th>ObjectQuery Class Method</th>
<th>Purpose</th>
</tr>
</thead>
<tbody>
<tr>
<td><pre lang="JAVA"><code>public static void addObjectToTable(Object obj)</code></td>
<td>Adds the object to the database table.<br/> <em>Must use the ObjectMapper Methods first to create the table before adding Object to Database</em></td>
</tr>
<tr>
<td><pre lang="JAVA"><code>public static List&lt;Object&gt; returnAllObjectsFromTable(
        String tableName)</code></td>
<td>Returns a List of All Objects From the Table<br/><em>Query: SELECT </em> FROM tableName;*</td>
</tr>
<tr>
<td><pre lang="JAVA"><code>public static List&lt;Object&gt; returnObjectsWhereColumnIs(
        String tableName, String ColumnName,
        String Value)</code></td>
<td>Returns a List of Objects where the condition is met<br/><em>Query: SELECT </em> FROM tableName WHERE ColumnName = Value;*</td>
</tr>
<tr>
<td><pre lang="JAVA"><code>public static List&lt;Object&gt; returnObjectsWhereColumnIsLessThan(
        String tableName, String ColumnName,
        String Value)</code></td>
<td>Returns a List of Objects where the condition is met<br/><em>Query: SELECT </em> FROM tableName WHERE ColumnName &lt; Value;*</td>
</tr>
<tr>
<td><pre lang="JAVA"><code>public static List&lt;Object&gt; returnObjectsWhereColumnIsGreaterThan(
        String tableName, String ColumnName,
        String Value)</code></td>
<td>Returns a List of Objects where the condition is met<br/><em>Query: SELECT </em> FROM tableName WHERE ColumnName &gt; Value;*</td>
</tr>
<tr>
<td><pre lang="JAVA"><code>public static List&lt;Object&gt; returnObjectsWhere2ColumnsAre(
        String tableName, String ColumnName,
        String Value, String ColumnName2,
        String Value2)</code></td>
<td>Returns a List of Objects where two condition are met<br/><em>Query: SELECT </em> FROM tableName WHERE ColumnName = Value AND ColumnName2 = Value2;*</td>
</tr>
<tr>
<td><pre lang="JAVA"><code>public static List&lt;Object&gt; returnObjectsWhereColumnsAre(
        String tableName, String[] ColumnNames,
        String[] Operands, String[] Values)</code></td>
<td>Returns a List of Objects where columnNames.length conditions are met<br/><em>Query: SELECT </em> FROM tableName WHERE ColumnName[0] Operand[0] Value[0] AND ColumnName[1] Operand[1] Value[1] AND ... ColumnName[n] Operand[n] Value[n];* </td>
</tr>
<tr>
<td><pre lang="JAVA"><code>public static int returnCountOfObjectsWhereColumnIs(
        String tableName, String ColumnName,
        String Value)</code></td>
<td>Returns Count of the number of objects in the database where the condition is met<br/><em>Query: SELECT COUNT(ColumnName) FROM tableName WHERE ColumnName = Value;</em></td>
</tr>
<tr>
<td><pre lang="JAVA"><code>public static int returnCountOfObjectsWhereColumnIsLessThan(
        String tableName, String ColumnName,
        String Value)</code></td>
<td>Returns Count of the number of objects in the database where the condition is met<br/><em>Query: SELECT COUNT(ColumnName) FROM tableName WHERE ColumnName &lt;&gt; Value;</em></td>
</tr>
<tr>
<td><pre lang="JAVA"><code>public static int returnCountOfObjectsWhereColumnIsGreaterThan(
        String tableName, String ColumnName,
        String Value)</code></td>
<td>Returns Count of the number of objects in the database where the condition is met<br/><em>Query: SELECT COUNT(ColumnName) FROM tableName WHERE ColumnName &gt; Value;</em></td>
</tr>
<tr>
<td><pre lang="JAVA"><code>public static void removeObjectFromTable(
        Object obj, String tableName)</code></td>
<td>Removes given object from the database <br/><em>Query: DELETE FROM tableName WHERE id = obj.Id;</em></td>
</tr>
<tr>
<td><pre lang="JAVA"><code>public static void truncateTable(String tableName)</code></td>
<td>Truncates given table in the database <br/><em>Query: TRUNCATE TABLE tableName;</em></td>
</tr>
<tr>
<td><pre lang="JAVA"><code>public static void udpateAllWhere (
        String tableName, String columnName,
        String oldValue, String newValue)</code></td>
<td>Updates all values in the given column that match oldValue to newValue <br/><em>Query: UPDATE tableName SET columnName = newValue WHERE columnName = oldValue;</em></td>
</tr>
<tr>
<td><pre lang="JAVA"><code>public static void updateObjectToTable(Object newObj)</code></td>
<td>Updates the object in the database to the values passed in the the newObj. Object to be updated is based on the primary key value. <br/><em>Query: UPDATE tableName SET column1 = value1, column2 = value2,... WHERE id = newObj.Id;</em></td>
</tr>
<tr>
<td><pre lang="JAVA"><code>public static ObjectCache getCache()</code></td>
<td>Used to get an ObjectCache object to perform ObjectCache methods</td>
</tr>
</tbody>
</table>



#### ObjectCache Class
*Used to query the Cache*<br/>
*To get a cache Object, use ObjectQuery.getCache()*
<table>
<thead>
<tr>
<th>ObjectCache Class Method</th>
<th>Purpose</th>
</tr>
</thead>
<tbody>
<tr>
<td><pre lang="JAVA"><code>public void addObjectToCache(Object obj)</code></td>
<td>Adds Object obj to the Cache</td>
</tr>
<tr>
<td><pre lang="JAVA"><code>public void deleteObjectFromCache(Object obj)</code></td>
<td>Deletes Object obj from the Cache</td>
</tr>
<tr>
<td><pre lang="JAVA"><code>public HashSet&lt;Object&gt; getAllObjectsInCache(Class&lt;?&gt; clazz)</code></td>
<td>Gets all objects of Class clazz from the Cache and returns in a HashSet</td>
</tr>
<tr>
<td><pre lang="JAVA"><code>public void deleteAllObjectsInCache(Class&lt;?&gt; clazz)</code></td>
<td>Deletes all objects of Class clazz fromthe Cache</td>
</tr>
</tbody>
</table>


#### DatabaseTransaction Class
*Used to set Transactions the Database<br/>A Transaction must start with **beginTransaction()** and end with **commitTransaction()***
<table>
<thead>
<tr>
<th>DatabaseTransaction Class Method</th>
<th>Purpose</th>
</tr>
</thead>
<tbody>
<tr>
<td><pre lang="JAVA"><code>public static void beginTransaction()</code></td>
<td>Used to begin a Transaction<br/><em>Query: BEGIN;</em></td>
</tr>
<tr>
<td><pre lang="JAVA"><code>public static void commitTransaction()</code></td>
<td>Used to commit a Transaction<br/><em>Query: COMMIT;</em></td>
</tr>
<tr>
<td><pre lang="JAVA"><code>public static void setSavepoint(String SavePointName)</code></td>
<td>Used to set a Transaction Save Point<br/><em>Query: SAVEPOINT SavePointName;</em></td>
</tr>
<tr>
<td><pre lang="JAVA"><code>public static void rollbackToSavepoint(String SavePointName)</code></td>
<td>Used to begin a Transaction<br/><em>Query: ROLLBACK TO SAVEPOINT SavePointName;</em></td>
</tr>
<tr>
<td><pre lang="JAVA"><code>public static void rollbackTransaction()</code></td>
<td>Used to begin a Transaction<br/><em>Query: ROLLBACK;</em></td>
</tr>
</tbody>
</table>


### User API Examples
#### Annotations Example
![Annotations](https://user-images.githubusercontent.com/82348878/123563837-87d91700-d77c-11eb-88f5-db3c68d2a9cf.png)</br>
#### ObjectMapper Example
![ObjectMapper](https://user-images.githubusercontent.com/82348878/123563835-87d91700-d77c-11eb-8bc1-db6e3f5480aa.png)</br>
#### ObjectQuery Example
![ObjectQuery](https://user-images.githubusercontent.com/82348878/123563836-87d91700-d77c-11eb-8b97-431503658c72.png)</br>
#### ObjectCache Example
![ObjectCache](https://user-images.githubusercontent.com/82348878/123563839-8871ad80-d77c-11eb-9ead-8bfdf195ed59.png)</br>
#### DatabaseTrasaction Example
![DatabaseTransaction](https://user-images.githubusercontent.com/82348878/123563838-8871ad80-d77c-11eb-8210-0d9874cc3ff4.png)</br>


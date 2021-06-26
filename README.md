# JAN ORM

## JAN ORM Description
JAN ORM is a java based lightweight Object Relational Mapper used for connection to a PostGreSQL Databse without the need for SQL or Connection Management. 

Authors: Jack Welsh, Aron Jang, Nate Opsal.

Date Last Updated: 6/26/21

## TechStack
* Java - _version 8.0_
* JUnit - _version 42.2.12_
* Apache Maven - _version 3.8.1_
* PostGreSQL - _deployed on AWS RDS_
* Git SCM (on GitHub) 

## Featues
### Ready to Use
* Drop then create a table from on an annotated class
* Save an instance of an object as an object
* Update an entry in the database
* Delete an entry from the database
* Find entries in the database that statisfy given columns, operands, and values
* Add given constriant on table using annotations
  * Primary Key
  * Foreign Key
  * Check
  * Unique
  * Default Value
  * Not Null
* First Level Caching

### Future Devlopement
* Second-Level Caching
* Implementing Complex Queries
* Implementing Aggregate Functions

## Getting Started  
### Cloning the Application
```shell
  git clone <this-repo>
  cd GreatestSQL
  mvn install
```
### Place Dependency Inside Your Project pom.xml File
```XML
  <dependency>
    <groupId>com.revature</groupId>
    <artifactId>JAN_ORM_p1</artifactId>
    <version>1.0-SNAPSHOT</version>
  </dependency>

```
### Application.properties file
Ensure that you have created this file in **src/main/resource/** and have the following lines of code:
 ``` 
  url=path/to/database
  admin-usr=username/of/database
  admin-pw=password/of/database  
  ```
## Usage
### Annotating Classes to Persist to Database
For a class to be persisted to the database, it must be annoted using the following annotations:
Annotation | Purpose
-----------|--------
@Table(name = "table_name") | Indicates to the ORM that this class is associated with table 'table_name'
@Column() | Indicates to the ORM that this field is a column in the table.<p>The column name is automatically the field name.
@PrimaryKey | Indicates to the ORM that this field is a Prmary key <p>Each class can only have one primary key.
@ForeignKey(refColumn = "ref_field", refClass = "ref_class") | Indicates to the ORM that this field is a Foreign Key that references class 'ref_class' and its field 'ref_field'
@CheckColumn(checkStmt = "check_statement") | Indicates to the ORM that this field is must undergo the "check_statement" before the record can be added to the table
@NotNull() | Indicates to the ORM that this field cannot be null
@DefaultValue(defaultValue = "default_value") | Indicates to the ORM that if no value is given for this field, then the default value will populate the field
@Unique() | Indicates to the ORM that this field has to be unique and cannot contain multiples of the same value in the table

### User API

#DevBricks

[![License](https://poser.pugx.org/dreamfactory/dreamfactory/license.svg)](http://www.apache.org/licenses/LICENSE-2.0) [![API](https://img.shields.io/badge/API-13%2B-green.svg?style=flat)](https://android-arsenal.com/api?level=8) [![Maven Central](https://maven-badges.herokuapp.com/maven-central/com.github.dailystudio/devbricks/badge.svg)](https://maven-badges.herokuapp.com/maven-central/com.github.dailystudio/devbricks)


DevBricks provides several classes which will be usually  used in daily Android development. With these "bricks", your development will become:

- **Efficient** : The classes provided by DevBricks almost cover all of the aspect in daily devevoplment, from low-end databaes to user interface. You do not need to waste your time on those repeating work.
- **Reliable** :  More than 60% code has related Unit test. Your work will stand on stable foundation. 
- **Consistency** : DevBricks includes unified logging system, database accessing, UI elements and styles. This make all of your applications has consistency at primary impression.

## Database
Database facilities in DevBricks provides a efficient way to convert between **In-Memory Data Structures** and **SQLite Database Records**. 

- ***DatabaseObject*** represents object in memory which could be easily store in permanent database through Database read/write facility classes.
- ***Column*** describe how to map a field of a In-Memory class to a column of database record.
- ***Template*** contains a set of *Column* which is usually used to describe how to convert a *DatabaseObject* to database record.
- ***Query*** is used to describe query parameters when loading objects from databases. It converts most parts of common SQL select statement into Java language. 
- ***DatabaseReader*** is a shortcut class to reading obejcts from database.
- ***DatabaseWriter*** is a shortcut class to saving objects into database.

With these classes, even you do not have any knowledge about SQL or Androiud Content Provider, you can easily bind data in your application with permanent database storage.

### Define an Object
For example, if you have a class named ***People***, which represent a people data structure in memory. It is defined as below:
```java
public class People {
	private String mName;
	private int mAge;
	private float mWeight;
	private int mHeight;
	private boolean mMarried;
}
```
You want each people will be stored as one record in database, like this:

ID   | Name    | Age  | Weight | Height | Married 
:--- | :-------| :--: | :--:   | :--:   | :--:   
1    | David   | 34   | 69     | 175    | 1       
2    | Lucy    | 33   | 48.5   | 165    | 0
...  | ...     | ..   | ..     | ...    | .

To map a ***People*** to a database record, you need to derive ***People*** from ***DatabaseObject*** firstly,  then define a template and bind them together:

```java
public class People extends DatabaseObject {

	public static final Column COLUMN_ID = new IntegerColumn("_id", false, true);
	public static final Column COLUMN_NAME = new StringColumn("name");
	public static final Column COLUMN_AGE = new IntegerColumn("age");
	public static final Column COLUMN_WEIGHT = new DoubleColumn("weight");
	public static final Column COLUMN_HEIGHT = new IntegerColumn("height");
	public static final Column COLUMN_MARRIED = new IntegerColumn("married");

	private final static Column[] COLUMNS = {
		COLUMN_ID,
		COLUMN_AGE,
		COLUMN_WEIGHT,
		COLUMN_HEIGHT,
		COLUMN_MARRIED,
	};
	
	private String mName;
	private int mAge;
	private float mWeight;
	private int mHeight;
	private boolean mMarried;

	public People(Context context) {
		super(context);
		
		final Template templ = getTemplate();
		templ.addColumns(COLUMNS);
	}
	
	pubic void setId(int id) {
		setValue(COLUMN_ID, id)
	}
	
	public int getId() {
		return getIntegerValue(COLUMN_ID);
	}

...
	
	pubic void setMarried(boolean married) {
		setValue(COLUMN_MARRIED, (married ? 1 : 0))
	}
	
	public boolean isMarried() {
		return (getIntegerValue(COLUMN_MARRIED) == 1);
	}

}
```

###Saving or loading objects
Before moving forward, you need to understand a little more implementation behind the interface. Database manipulation in DevBricks is basing on ***Content Provider***, which is an important component on Android platform. Even you do not need to know more about this concept, you have to declare things in your ***AndroidManifest.xml*** before you start to use these interfaces. Firstly, you need to declare a ***Content Provider*** in the ***AndroidManifest.xml*** of your project:
```xml
<application
	android:icon="@drawable/ic_app"
    android:label="@string/app_name">
    ...
	<provider
	    android:name=".AppConnectivityProvider"
        android:authorities="com.yourdomain.youapp" />
    ...
</application>
```
Class ***AppConnectivityProvider*** is derived from ***DatabaseConnectivityProvider***. Keep it implementation empty is enough.
```java
public class AppConnectivityProvider extends DatabaseConnectivityProvider {

}
```
Usually, you only need one provider like this to handle all the database operations in your application. Defining the authority of this provider same as your package name will make everything easy. When you create a ***DatabaseReader*** or ***DatabaseWriter***,  you can use a shortcut creator, like this:
```java
DatabaseReader<People> reader = new DatabaseReader(context, People.class);
DatabaseWriter<People> writer = new DatabaseWriter(context, People.class);
...
```
But sometimes, you need to handle more complicated cases. You may need to define two providers. One is using inside application, while the other one is using to share data with other applications. In this case, you need to declare another provider with a different authority:
```xml
<application
	android:icon="@drawable/ic_app"
    android:label="@string/app_name">
    ...
	<provider
	    android:name=".ExternConnectivityProvider"
        android:authorities="com.yourdomain.external" />
    ...
</application>
```
At the same time, when you want to use DatabaseReader or DatabaseWriter on this provider, you need to pass the authority as second parameter in creator:
```java

DatabaseReader<People> reader = new DatabaseReader(context, "com.yourdomain.external", People.class);
DatabaseWriter<People> writer = new DatabaseWriter(context, "com.yourdomain.external", People.class);
...
```

Now, when you finish these steps above, you can easily use database read/write facilites to save or load ***People** objects between memory and database. 

***DatabaseWriter*** is a shortcut class to save im-memory obejcts to database.  For example, add a ***People*** to database:

```java
DatabaseWriter<People> writer = new DatabaseWriter(context, People.class);

People p = new People();
p.setName("David");
p.setAge(33);
p.setWeight(69);
p.setHeight(175);
p.setMarried(true);

writer.insert(p);
```

***DatabaseReader*** is a shortcut class to load database records into memory.  For example, query all ***People*** from database:

```java
DatabaseReader<People> reader = new DatabaseReader(context, People.class);

List<People> people = reader.query(new Query(People.class));

for (People p: people) {
	/* process each people */
}

```
When you are using the ***DatabaseReader***, the ***Query*** will become a much more important helper class. You need to rely on this helper class to describe all of your query on the database.
A ***Query*** object combines the following  ***ExpressToken***  together to define a query. Each kind of these ***ExpressionToken*** correspond to a related SQLite statement:

Expression Token | SQLite Statement
:--              | :--
Selection Token  | where 
GroupBy Token    | group by
OrderBy Token    | order by
Having Token     | having
Limit Token      | limit 

The  following *binary operator* can be performed on a ***ExpressToken*** with another ***ExpressToken***:

Op function      | SQLite Equivalent  | Explanation
:--              | :--                | :--
.plus()          | +                  | a + b
.minus()         | -                  | a - b
.multiple()      | *                  | a * b
.divide()        | /                  | a / b
.modulo()        | %                  | a % b

The  following *logical operations* can  between combine two ***ExpressToken***:

Op function      | SQLite Equivalent  | Explanation
:--              | :--                | :--
.and()           | &&                 | a && b
.or()            | \|\|               | a \|\| b
.eq()            | ==                 | a == b
.neq()           | <>                 | a <> b
.gt()            | >                  | a > b
.gte()           | >=                 | a >= b
.lt()            | <                  | a < b
.lte()           | <=                 | a <= b
.in()            | >= and <=          | a >= b && a <= c
.out()           | < or >             | a < b \|\| a > c



>Copyright
>2010-2016 by Daily Studio.

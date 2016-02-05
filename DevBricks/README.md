#DevBricks

[![License](https://poser.pugx.org/dreamfactory/dreamfactory/license.svg)](http://www.apache.org/licenses/LICENSE-2.0)

DevBricks provides several classes which will be usually  used in daily Android development. With these "bricks", your development will become:

- **Efficient** : The classes provided by DevBricks almost cover all of the aspect in daily devevoplment, from low-end databaes to user interface. You do not need to waste your time on those repeating work.
- **Reliable** :  More than 60% code has related Unit test. Your work will stand on stable foundation. 
- **Consistency** : DevBricks includes unified logging system, database accessing, UI elements and styles. This make all of your applications has consistency at primary impression.

## Setup DevBricks

## Using DevBricks

### Database
Database facilities in DevBricks provides a efficient way to convert between **In-Memory Data Structures** and **SQLite Database Records**. 

- ***DatabaseObject*** represents object in memory which could be easily store in permanent database through Database read/write facility classes.
- ***Column*** describe how to map a field of a In-Memory class to a column of database record.
- ***Template*** contains a set of *Column* which is usually used to describe how to convert a *DatabaseObject* to database record.

If we have a class ***People*** which represent a people in memory. Its structure is defined as below:
```java
public class People {
	private String mName;
	private int mAge;
	private float mWeight;
	private int mHeight;
	private boolean mMarried;
}
```
And we want each people will be stored as one record in database, like this:

ID   | Name    | Age  | Weight | Height | Married 
:--- | :-------| :--: | :--:   | :--:   | :--:   
1    | David   | 34   | 69     | 175    | 1       
2    | Lucy    | 33   | 48.5   | 165    | 0
...  | ...     | ..   | ..     | ...    | .

To map a ***People*** to a database record, we need to derive ***People*** from ***DatabaseObject*** , define a template and bind them together:

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
After that we can easily use database read/writer facilites to save or load ***People** objects between mem or database. 

***DatabaseWriter*** is a shortcut class to save im-memory obejct to database.  For example, add a ***People*** to database:

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

***DatabaseReader*** is a shortcut class to load database record into memory.  For example, query all ***People*** from database:

```java
DatabaseReader<People> reader = new DatabaseReader(context, People.class);

List<People> people = reader.query(new Query(People.class));

for (People p: people) {
	/* process each people */
}

```

>Copyright
>2010-2016 by Daily Studio.

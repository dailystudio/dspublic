#DevBricks

DevBricks provides several classes which will be usually  used in daily Android development. With these "bricks", your development will become:

- **Efficient** : The classes provided by DevBricks almost cover all of the aspect in daily devevoplment, from low-end databaes to user interface. You do not need to waste your time on those repeating work.
- **Reliable** :  More than 60% code has related Unit test. Your work will stand on stable foundation. 
- **Consistency** : DevBricks includes unified logging system, database accessing, UI elements and styles. This make all of your applications has consistency at primary impression.


## Database
Database facilities in DevBricks provides a efficient way to convert between **In-Memory Data Structures** and **SQLite Database Records**. 

- ***DatabaseObject*** represents object in memory which could be easily store in permanent database through Database read/write facility classes.
- ***Column*** describe how to map a field of a In-Memory class to a column of database record.
- ***Template*** contains a set of *Column* which is usually used to describe how to convert a *DatabaseObject* to database record.

If we have a class ***People*** which represent a people in memory. Its structure is defined as below:
```java
public class A {
	private String mName;
	private int mAge;
	private float mWeight;
	private float mHeight;
	private boolean mMarried;
}
```
In database,  each people will be store as one record like:
| ID   | Name    | Age  | Weight | Height | Married |
| :--- | :-------| :--: |  :--:  |  :--:  |  :--:   |
| 1    | David   | 34   | 69     | 175    | 1       | 

## Loader


>Copyright
>2010-2015 by Daily Studio.

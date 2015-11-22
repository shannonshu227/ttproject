##### Package Layout

```
com
    tt
        challenge
        ├── Database.java
        ├── Session.java
        ├── Test.java
        └── ValueFreq.java
```


##### How to Run

1. Interactive
java com.tt.challenge.Session

2. With Input File
cat input1.txt | java com.tt.challenge.Session

##### System Design

a. Database.java
```java
    Map<String, Integer> data;
    ValueFreq vf;
    private Database prev;
```
data: is a Map to store the records, for example, "SET a 10" will put ("a",10) into the data map.
vf: is a HashMap to store the records' value and how many times it occurs in the DB.
prev: is a pointer to previous Database instance (previous transaction starts with 'BEGIN')

Database has two kinds of constructors, public Database() and public Database(Database db). The former is to create an empty Database instance, and the latter is to create a Database instance with data information of another Database instance (so it will get the data records of previous transaction). Database class has function implementations of set/unset/get/numEqualTo/commit/rollback.

Every Database instance maintains its own MAP data. But there is only one MAP vf implemented as Singleton in class ValueFreq. Each Database instance just calls the getter to get the ValueFreq instance.

SET/UNSET/GET operations are guaranteed in O(1) complexity by using MAP data.
NUMEQUALTO operations are guaranteed in O(1) complexity by using MAP vf.
ROLLBACK operation is O(1) complexity, the auxiliary freq map update will take O(k), k is data map size of current Database.
COMMIT operation is O(n) complexity, n is number of commands in session.


SET/UNSET/GET/NUMEQUALTO implementation is obvious, I'll skip it here. One thing to note is,
when doing SET/UNSET, make sure to update ValueFreq freq map accordingly. If a key does not exist in current Database instance, try to find it in all previous Database instances by traversing the prev pointer.

For COMMIT, the data map in current Database instance is merged back to prev Database instance, and so on and so forth till the initial transaction.

For ROLLBACK, just return the previous Database instance. But be sure to update the ValueFreq freq map. To achieve that, for all keys in current Database's data map, we find whether it exists in previous Database instances and its value. By comparing diff, recover the previous Database's freq map.


b. ValueFreq.java
``` java
    private static ValueFreq instance = null;
    private Map<Integer, Integer> freq;
```
This is the Singleton class, and freq map is to store the values and the frequency it occurs in DB. It has increase(int value), decrease(int value) and rollback(Database db) implementations, which will be used in Database.java to update the freq map for each operation.

c. Session.java

This is the main entry for a set of transactions. Each transaction starts with BEGIN and each session ends with END.
Enum is used for commands in order to avoid if-else lookup in order.

d. Test.java

This is the unit test.

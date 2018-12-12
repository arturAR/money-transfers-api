# Revolut Money Transfer - interview task

Implementation a RESTful API (including data model and the backing implementation) for money transfers between accounts.

## Requirements:
Explicit requirements:
1. You can use Java, Scala or Kotlin.
2. Keep it simple and to the point (e.g. no need to implement any authentication).
3. Assume the API is invoked by multiple systems and services on behalf of end users.
4. You can use frameworks/libraries if you like (except Spring), but don't forget about
requirement #2 – keep it simple and avoid heavy frameworks.
5. The datastore should run in-memory for the sake of this test.
6. The final result should be executable as a standalone program (should not require
a pre-installed container/server).
7. Demonstrate with tests that the API works as expected.

Implicit requirements:
1. The code produced by you is expected to be of high quality.
2. There are no detailed requirements, use common sense.

## How to Build
    mvn clean package

## How to Run
    java -jar ./target/revolut-money-transfers-1.0.jar
    
## How to check Code Quality

    mvn site
    
## End Points
### Accounts
    GET /accounts
    GET /accounts/:id
    GET /accounts/:id/transfers
    POST /accounts
    
### Transfers
    GET /transfers
    GET /trsnfers/:id
    POST /transfers
    
## Used technologies:
Project is based on:
- com.sparkjava:spark-core
- com.google.code.gson:gson
- org.jooq:jooq
- org.hsqldb:hsqldb

## Design assumptions and limitations:
- No REST API versioning
- No timezones for database timestamps
- Insufficient description of exceptions 
- No results pagination
- Data type for money: `DECIMAL(19,2)`
- Failed transfers are not restarted
- Correctness of concurrent transfers is based on READ COMMITTED isolation level, 
    which is default and `keeps write locks on tables until commit, but releases the read locks after each operation`, 
    see [HSQLDB locking](http://hsqldb.org/doc/2.0/guide/sessions-chapt.html#snc_tx_2pl)



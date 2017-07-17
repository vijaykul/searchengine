# SearchEngine
A simple application to do a quick search on the JSON data set of format:

```
Example:

[
  {
    "name": "abc"
    "id": 1
  },
  {
    "name": "def"
    "id":2
  }
]
```
##### Main features

* Any field can be used to make a search
* User can make search on empty values
* The search is successful if it’s a complete match
* Only case sensitive search is supported.

## Major highlights of SearchEngine Design

Its implemented as database based query engine, where every json dataset is treated as table and every json object is a unique row in the table. With this abstraction, we can query the table with any existing column. Based on the configuration, every json dataset is registered to database as a unique table.

Behaviour of the application is configuration controlled. Through configuration we can establish a unique relationship between multiple tables (details are explained below).

## Installation Guide

Below instructions will guide you through the process of setting up SearchEngine environment for the first time.

#### Prerequisites

All the dependencies of the SearchEngine have to be installed.

Ubuntu and other Debian-based distributions

For Scala Installation:
```
$ sudo apt-get update
$ sudo apt-get install scala
```
For SBT Installation follow the instruction metioned in the below link:

http://www.scala-sbt.org/release/docs/Installing-sbt-on-Linux.html

Clone the git repository available here on github.

```
$ git clone https://github.com/vijaykul/searchengine.git
```

#### Build SearchEngine Application
```
$ sbt compile

To obtain the JAR file of the application to execute

$ sbt package
```
## Configuration of SearchEngine

SearchEngine reads the below information from the configuration file. A sample file is provided in _conf/application.conf_
```
 {
  name = "Ticket"
  schema = ./schema/ticket.schema
  path = ./code-challenge/tickets.json
 }
 ```
 
 ###### Description of fields:
 1. **name** : Defines the name of the json file containing the array of json objects.
 1. **schema** : The absolute path of schema file, containing all the names in a given json object
 1. **path** : The absolute path of json file

Below stanza defines the relationship between two json dataset, and hence used to search the required fields in related dataset.
```
 {
  source="User"
  destination="Organisation"
  input="organization_id"
  lookup="_id"
  mapping="name"
  output="organization_name"
 }
 ```
 ###### Description of fields
 1. **source** : Source json dataset name
 1. **destination** : Destination json dataset name
 1. **input** : Source dataset, whose value is used to seach in destination dataset.
 1. **lookup** : Destination dataset name used to get the final result(s).
 1. **mapping** : Only mapping name and value is extracted from final result(s)
 1. **output** : Mapped name and value are printed to the user using output name.
 
 ```
 Output for above sample configuration using test dataset.
 
 _id                                               1
url                                               http://initech.zendesk.com/api/v2/users/1.json
external_id                                       74341f74-9c79-49d5-9611-87ef9b6eb75f
name                                              Francisca Rasmussen
alias                                             Miss Coffey
created_at                                        2016-04-15T05:19:46 -10:00
active                                            true
verified                                          true
shared                                            false
locale                                            en-AU
timezone                                          Sri Lanka
last_login_at                                     2013-08-04T01:03:27 -10:00
email                                             coffeyrasmussen@flotonic.com
phone                                             8335-422-718
signature                                         Don't Worry Be Happy!
organization_id                                   119
tags                                              ["Springville", "Sutton", "Hartsville/Hartley", "Diaperville"]
suspended                                         true
role                                              admin
organization_name                                 Multron
ticket_0                                          A Nuisance in Kiribati
ticket_1                                          A Nuisance in Saint Lucia

```
###### Schema files

Simple text files containing the names of the json object.
sample schema files are @path _schema/ticket.schema_

## Running the SearchEngine

The Search application can be launched using below commands

Build the application from base folder containing _build.sbt_:
```
$ sbt package
```
Running the application
```
$ scala <path of jar>/json-database-search-engine_2.11-1.0.0.jar ./conf/application.conf
```
Running the unit test using ScalaTest:
```
$ sbt test
```
**Note** : The unit testing is done for few important classes so its not a complete coverage.

## Some Critical Test Results

1) Search value doesnt exist in the dataset.
```
Select 1)Organisation or 2)User or 3)Ticket
2
Enter search term
_id
Enter search value
123456
Searching User for _id with a value 123456
No results found
```
2) Search value is empty
```
Select 1)Organisation or 2)User or 3)Ticket
3
Enter search term
description
Enter search value

_id                                               436bf9b0-1147-4c0a-8439-6f79833bff5b
url                                               http://initech.zendesk.com/api/v2/tickets/436bf9b0-1147-4c0a-8439-6f79833bff5b.json
external_id                                       9210cdc9-4bee-485f-a078-35396cd74063
created_at                                        2016-04-28T11:19:34 -10:00
type                                              incident
subject                                           A Catastrophe in Korea (North)
description
priority                                          high
status                                            pending
submitter_id                                      38
assignee_id                                       24
organization_id                                   116
tags                                              ["Ohio", "Pennsylvania", "American Samoa", "Northern Mariana Islands"]
has_incidents                                     false
due_at                                            2016-07-31T02:37:50 -10:00
via                                               web
User                                              Elma Castro
organization_name                                 Zentry
```

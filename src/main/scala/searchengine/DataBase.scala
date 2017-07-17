package searchengine

/** Class represents Database containing the Json Tables
 * This class is populated based on the configuration in
 * application.conf
 *
 *  Example:
 *
 *  Tables = [
 *    {
 *      name = User
 *      path=<path of the json file>
 *    }
 *    {
 *      name = "Organisation"
 *      path=<path of the json file>
 *    }
 *  ]                                                                                                                                ]
 *
 *
 */
class Database[TYPE, TABLE] {
  var db = collection.mutable.HashMap.empty[TYPE, TABLE]


  /** Adds table entry into database
   *
   *  @param name Name of the table
   *
   *  @param tableInstance Reference to the json table instance
   */
  def register(name: TYPE, tableInstance: TABLE): Unit = {
    db(name) = tableInstance
  }

  /** Lists all the tables in Database
   */
  def listTables(): List[TYPE] = {
    db.keys.toList
  }

  /** Returns the instance of the table based on table name
   * 
   * @param key Name of the Table
   *
   * @return Instance of the table
   */
  def getTableInstance(key: TYPE): TABLE = {
    db(key)
  }
}

object Database {

  var db: Database[String, Table[String, Any, Any]] = null

  def getInstance =  { 
    if (db == null) {
        db = new Database[String, Table[String, Any, Any]] ()
    }
    db
  }

}

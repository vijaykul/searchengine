package searchengine

import com.typesafe.config.ConfigFactory
import scala.collection.JavaConversions._
import java.io._

case class Tables(name: String, schema: String, path: String)
case class Relations(source: String, destination: String, input: String, lookup: String, mapping: String, output: String)

object searchEngine {

  def main(args: Array[String]) {

    // check for configuration file
    if (args.length <= 0) {
      throw new RuntimeException("Configuration file not provided as a part of cli")
    }

    // Loads the configuration file.
    val config = ConfigFactory.parseFile(new File(args(0)))
    val tables = config.getConfigList("searchengine.tables")
    val list = tables.map(conf => Tables(conf.getString("name"), conf.getString("schema"), conf.getString("path"))).toList
    val db = Database.getInstance

    //This loops for all the configured tables in the configuration file
    for (entry <- list) {
      val table = Table.empty[String, Any, Any]

      // Load the schema file for the table in process
      try {
        for (colName <- scala.io.Source.fromFile(entry.schema).getLines) {
            table.update(colName)
        }
      } catch {
        case ex: IOException => println("Reading of schema file failed "); throw ex
      }

      val jsonObj = JsonUtils.parseJson(entry.path)

      // Check if the JSON file is empty??
      if (jsonObj == null) {
        throw new RuntimeException("Json file [" + entry.path + "] is empty.. Hence existing")
      }

      //loads all the json objects into table mapped to a uniqueId
      var uniqueId = 1
      jsonObj.asInstanceOf[List[Map[Any, Any]]] map {
        jObj => jObj map {
          case (k, v) => table.update(uniqueId.toString, k, v)}; uniqueId = uniqueId + 1
      }
      //Register the table into data base
      db.register(entry.name, table)
    }

    // Here all the cross table relationships are loaded into their respective 
    // tables.
    val tableRelations = config.getConfigList("searchengine.relations")
    val relationList = tableRelations.map(conf =>
        Relations(
          conf.getString("source"),     // Source table name
          conf.getString("destination"),// Destination table name
          conf.getString("input"),      // Source table column name
          conf.getString("lookup"),     // Destination table column name
          conf.getString("mapping"),    
          conf.getString("output"))     // output name
        ).toList

    // Loading the relations details in their respective tables
    for (entry <- relationList) {
      val srcTable = Database.getInstance.getTableInstance(entry.source)
      srcTable.update(entry)
    }

    // Handles the user inputs.
    Display.run
  }
}

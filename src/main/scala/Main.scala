package searchengine

import com.typesafe.config.ConfigFactory
import scala.collection.JavaConversions._
import java.io._

case class Tables(name: String, schema: String, path: String)
case class Relations(source: String, destination: String, input: String, lookup: String, mapping: String, output: String)

object searchEngine {

  def main(args: Array[String]) {

    if ((args.length <= 0) || (args(0) == "")) {
      println("!! Configuration file not provided as part of command line argument !!")
      System.exit(1)
    }

    val config = ConfigFactory.parseFile(new File(args(0)))
    val tables = config.getConfigList("searchengine.tables")
    val list = tables.map(conf => Tables(conf.getString("name"), conf.getString("schema"), conf.getString("path"))).toList
    val db = Database.getInstance

    for (entry <- list) {
      val table = Table.empty[String, Any, Any]

      try {
        for (colName <- scala.io.Source.fromFile(entry.schema).getLines) {
            table.update(colName)
        }
      } catch {
        case ex: IOException => println("Reading of schema file failed "); throw ex
      }

      val jsonObj = JsonUtils.parseJson(entry.path)

      if (jsonObj == null) {
        throw new RuntimeException("Json file [" + entry.path + "] is empty.. Hence existing")
      }

      var uniqueId = 1
      jsonObj.asInstanceOf[List[Map[Any, Any]]] map {
        jObj => jObj map {
          case (k, v) => table.update(uniqueId.toString, k, v)}; uniqueId = uniqueId + 1
      }
      db.register(entry.name, table)
    }

    val tableRelations = config.getConfigList("searchengine.relations")
    val relationList = tableRelations.map(conf =>
        Relations(
          conf.getString("source"),
          conf.getString("destination"),
          conf.getString("input"),
          conf.getString("lookup"),
          conf.getString("mapping"),
          conf.getString("output"))
        ).toList

    for (entry <- relationList) {
      val srcTable = Database.getInstance.getTableInstance(entry.source)
      srcTable.update(entry)
    }

    Display.run
  }
}

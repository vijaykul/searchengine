package searchengine

import com.typesafe.config.ConfigFactory
import scala.collection.JavaConversions._

case class Tables(name: String, path: String)

object searchEngine {

  def main(args: Array[String]) {
    val config = ConfigFactory.load()
    val m = config.getConfigList("searchengine.Tables")
    val list = m.map(conf => Tables(conf.getString("name"), conf.getString("path"))).toList
    val db = Database.getInstance
    for (entry <- list) {
      val table = Table.empty[String, Any, Any]
      val jsonObj = JsonUtils.parseJson(entry.path)
      var uniqueId = 1
      jsonObj.asInstanceOf[List[Map[Any, Any]]] map { 
        jObj => jObj map { 
          case (k, v) => table.update(uniqueId.toString, k, v)}; uniqueId = uniqueId + 1 
      }
      db.register(entry.name, table)
    }
    Display.run
  }
}

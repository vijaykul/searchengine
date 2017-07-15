package searchengine

import java.io._
import scala.util.parsing.json._

/** Simple Json parser singleton
 */

object JsonUtils {
  /** Parses the  Json file.
   *
   *  @param jsonFile Json file path.
   *
   *  @return A complex object containing the parsed items
   */
  def parseJson(jsonFile: String): Any = {

    // sanity check of json file path
    if (jsonFile.length == 0 ) {
      throw new IOException("Json file path invalid");
    }
    // parse the json file
    try {

      val json = JSON.parseFull(scala.io.Source.fromFile(jsonFile).getLines.mkString)

      json match {
        case Some(data) => return data
        case None => new IOException("Json Parse failed")
      }
    } catch {
      case ex: IOException => println(ex.getMessage)
      case all: Throwable => println("Some unknow exception : " + all); throw(all)
    }
    null
  }

}

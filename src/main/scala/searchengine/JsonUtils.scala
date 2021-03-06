package searchengine

import java.io._
import scala.util.parsing.json._

/** Simple Json parser singleton
 */

object JsonUtils {


  val myConversionFunc = {input : String => input.toInt}
  JSON.globalNumberParser = myConversionFunc

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
    val json = JSON.parseFull(scala.io.Source.fromFile(jsonFile).getLines.mkString)

    json match {
      case Some(data) => return data
      case None => return null
    }

    null
  }

}

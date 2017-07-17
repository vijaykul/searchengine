package searchengine

import org.scalatest.FunSuite
import org.scalatest.BeforeAndAfter

class JsonParseTests extends FunSuite with BeforeAndAfter {

  test ("Exception should be raised if the JSON file doesnt exists!!") {
    val thrown = intercept[Exception] {
      JsonUtils.parseJson("")
    }
    assert(thrown.getMessage === "Json file path invalid")
  }
  
  test ("Prasing a proper JSON file") {
    val json = JsonUtils.parseJson("./src/test/scala/searchengine/tickets.json")
    assert(json != null)
  }

  test ("Prasing a empty json file should return null") {
    val json = JsonUtils.parseJson("./src/test/scala/searchengine/empty.json")
    assert(json == null)
  }

}

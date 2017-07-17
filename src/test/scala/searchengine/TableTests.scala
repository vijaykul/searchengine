package searchengine

import org.scalatest.FunSuite
import org.scalatest.BeforeAndAfter

class TableTests extends FunSuite with BeforeAndAfter {

  var table: Table[String, Any, Any] = null

  before {
    table = new Table[String, Any, Any] ()
  }

  test("the new table has zero rows") {
    assert(table.rows.toList.length == 0)
  }

  test("now the table has one rows") {
    table.update("1", "11", "11")
    table.update("2", "2", "2")
    assert(table.rows.toList.length == 2)
  }

  test("selecting added item from table") {
    table.update("1", "11", "11")
    val t = table.select("11", "11")
    assert(t.length == 1)
  }

  test("selecting column with empty value") {
    table.update("1", "11", "")
    val t = table.select("11", "")
    assert(t.length == 1)
  }

}

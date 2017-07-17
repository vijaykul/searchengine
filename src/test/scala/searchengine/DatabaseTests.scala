package searchengine

import org.scalatest.FunSuite
import org.scalatest.BeforeAndAfter

class DatabaseTests extends FunSuite with BeforeAndAfter {

  var table: Table[String, Any, Any] = null
  var db : Database[String, Table[String, Any, Any]] = null

  before {
    table = new Table[String, Any, Any] ()
    db = new Database[String, Table[String, Any, Any]] ()
  }

  test("the db has zero tables") {
    assert(db.listTables.length == 0)
  }

  test("register a table") {
    db.register("0000", table)
    assert(db.listTables.length == 1)
  }

  test ("Exception should be raised if table doesnt exists") {
    val thrown = intercept[Exception] {
      db.getTableInstance("2")
    }
    assert(thrown.getMessage === "key not found: 2")
  }
}

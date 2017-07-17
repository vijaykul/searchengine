package searchengine

import org.scalatest.BeforeAndAfter
import org.scalatest.FeatureSpec
import org.scalatest.GivenWhenThen

class SearchEngineSpec extends FeatureSpec with GivenWhenThen with BeforeAndAfter {

  var db = Database.getInstance
  var table = Table.empty[String, Any, Any]

  feature("A search engine to extract User, Organisation, & Ticket details\n") {

    info("---------------------------------------------------------")
    info("As a user i want to search a User entry")
    info("I am using the User ID to search the value\n")

    scenario("search is initiated using the User ID") {

      Given("User data is present in the User Table")
      table.update("1", "_id", "1")
      db.register("User", table)

      When("Where data is queried from User Table")
      val result = db.getTableInstance("User").select("_id","1")

      Then("One matching value has to be retured\n")
      assert(result.length === 1)
    }

    info("---------------------------------------------------------")
    info("As a user i want to search a Ticket entry with Empty Discription")
    info("I am using Discription field with Empty value to search\n")

    scenario("search is initiated using the Ticket Id, with empty discription") {

      table = Table.empty[String, Any, Any]
      Given("Ticket entry with empty description exists in Tickets table ")
      table.update("2", "_id", "1111-2222-3333")
      table.update("2", "description", "")
      db.register("Ticket", table)

      When("Select entry with empty description")
      val result = db.getTableInstance("Ticket").select("description","")

      Then("One matching value has to be retured\n")
      assert(result.length === 1)
    }
  }
}

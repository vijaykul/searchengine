package searchengine

import scala.util.matching.Regex

/** All display functions are defined here.
 */

object Display {

  // Below instruction works for all linux/unix flavor, but not tested for windows
  private def clearScreen = System.out.print("\033[H\033[2J")

  // Fixed templated at the beginning of search page
  val title = "Welcome to Zendesk Search Engine\nType 'quit' to exit any time, Press 'Enter' to continue"
  val infoString = "\n\n\tSelect the search option:\n\t * Press 1 to search Zendesk\n\t * Press 2 to view the searchable fields \n\t * Type 'quit' to exit"

  /** Continuous running function, which accepts the instruction from
   *  the user
   */
  def run() = {

    clearScreen
    println(title)

    val continue = scala.io.StdIn.readLine()
    if (continue.toLowerCase == "quit") {
      println("Visit again! Bye")
      System.exit(0)
    }

    println(infoString)

    while (true) {

      val consoleInput = scala.io.StdIn.readLine()
      val rgx = "^([0-9]+)$"

      consoleInput match {

        // case to handle the 'quit' Instruction
        case "quit" => println("Good Day! Bye..."); System.exit(0)

        // continue search option
        case "1" =>
          try {
            var count = 1
            var header = "Select"
            val allTables = Database.getInstance.listTables
            for (i <- allTables) {
              header = header + " " +  count.toString + ")" + i + " or"
              count = count + 1
            }
            println(header.dropRight(3))
            var table = scala.io.StdIn.readLine()

            val re = new Regex(rgx)
            val re(value) = table
            table = value

            if(table.toInt <= allTables.length) {
              val column = scala.io.StdIn.readLine("Enter search term\n")
              val columnValue = scala.io.StdIn.readLine("Enter search value\n")
              printPretty(table, column, columnValue)
            } else {
              println("Invalid option: please read the instructions and try again!!!")
            }
          } catch {
            case ex: RuntimeException => println("Something went wrong in selections..\nPlease try again")
            case e: scala.MatchError => println("Something went wrong in selections..\n Please try again")
          }

          // Lists all the searchable fields
          case "2" =>

            val header = "Search %s with:"
            val allTables = Database.getInstance.listTables
            for (table <- allTables) {
              println("-"*30)
              println(header.format(table) + "\n")
              val instance = Database.getInstance.getTableInstance(table)
              for (column <- instance.columnOrder) {
                println(column)
              }
              println("\n")
            }

            case "help" => println(infoString)

            case _ => println("!!! Invalid selection !!!\nPlease follow the instruction provided above...\nOr enter 'help'")

      }
    }
  }

  def printPretty(table: String, column: String, columnValue: String): Unit = {

    val allTables = Database.getInstance.listTables
    val instance = Database.getInstance.getTableInstance(allTables(table.toInt-1))
    val printFormat = "%-50s%s"

    val matchedObj = instance.select(column, columnValue)

    if (matchedObj.length == 0) {

      val output = "Searching %s for %s with a value %s"
      println(output.format(allTables(table.toInt-1), column, columnValue.toString))
      println("No results found")
      return
    }

    for (all <- matchedObj) {
      //for ( (k,v) <- all) {
      for ( column <- instance.columnOrder) {
        val v = all.getOrElse(column, "")
        val value =  v match {
          case v1: List[Any] =>
            var tmp = "["
            for (itr  <- v1.asInstanceOf[List[String]]) {
              tmp = tmp + "\"" + itr + "\"" + ", "
            }
            tmp.dropRight(2) + "]"
          case _ => v
        }
        println(printFormat.format(column, value))
      }
      var ticketCount = 0
      for (i <- instance.relationWithOtherTable) {
        val dest = Database.getInstance.getTableInstance(i.destination)
        val lookupValue = all.getOrElse(i.input, "")
        val outputValue = dest.select(i.lookup.trim, lookupValue.toString)

        for (all <- outputValue) {
          for ( (k,v) <- all) {
            if (i.mapping == k) {
              var output = i.output
              if (i.output == "ticket") {
                output = "ticket" + "_" + ticketCount.toString
                ticketCount = ticketCount + 1
              }
              println(printFormat.format(output, v))
            }
          }
        }
      }
      println("-"*30)
      }
    }
  }

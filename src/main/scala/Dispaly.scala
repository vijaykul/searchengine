package searchengine

object Display {

  val title = "Welcome to Zendesk Search Engine\nType 'quit' to exit any time, Press 'Enter' to continue"

  private def clearScreen = System.out.print("\033[H\033[2J")

  val infoString = "\n\n\tSelect the search option:\n\t * Press 1 to search Zendesk\n\t * Press 2 to view the searchable fields \n\t * Type 'quit' to exit"

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

      consoleInput match {

        case "quit" => println("Good Day! Bye..."); System.exit(0)

        case "1" =>
          var count = 1
          var header = "Select"
          val allTables = Database.getInstance.listTables
          for (i <- allTables) {
            header = header + " " +  count.toString + ")" + i + " or"
            count = count + 1
          }
          println(header.dropRight(3))
          val table = scala.io.StdIn.readLine()
          val column = scala.io.StdIn.readLine("Enter search term\n")
          val columnValue = scala.io.StdIn.readLine("Enter search value\n")

          val instance = Database.getInstance.getTableInstance(allTables(table.toInt-1))
        //  printPretty(instance.select(column, columnValue))
          for (all <- instance.select(column, columnValue)) {
            val printFormat = "%-50s%s"
            for ( (k,v) <- all) {
              println(printFormat.format(k, v))
            }
            println("-"*30)
          }

        case "2" =>

          val header = "Search %s with:"
          val allTables = Database.getInstance.listTables
          for (table <- allTables) {
            println("-"*30)
            println(header.format(table) + "\n")
            val instance = Database.getInstance.getTableInstance(table)
            for (column <- instance.colHeaders) {
              println(column)
            }
            println("\n")
          }

        case "help" => println(infoString)

        case _ => println("!!! Invalid selection !!!\nPlease follow the instruction provided above...\nOr enter 'help'")

      }
    }
  }
}

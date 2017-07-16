package searchengine

import scala.reflect.runtime.universe._
import scala.reflect.{ClassTag, classTag}

import  scala.collection.mutable.ArrayBuffer

/** This class stores JSON data described as an array,
 *  and each element of that array is an object, stored in a map
 *
 * Example:
 *
 *  [
 *    {
 *      "id": 1,
 *      "name" : "abc"
 *    },
 *    {
 *      "id": 2,
 *      "name" : "def"
 *    }
 *  ]
 *
 */
class Table[K, C, V] {

  //Json is stored as key, value pair.
  private val table =  collection.mutable.HashMap.empty[K, collection.mutable.HashMap[C, V]]
  private val columnHeaders = new collection.mutable.HashSet[C] ()

  /** Updates the key-value pair of Json object
   *
   *  @param uniqueKey Running counter representing the entry in the table (uniquq key)
   *
   *  @param column Key value of the json object
   *
   *  @param value value part of json object
   *
   */
  def update(uniqueKey: K, column: C, value: V) = {
    this.table.getOrElseUpdate(uniqueKey, collection.mutable.HashMap.empty[C, V])(column) = value
    columnHeaders += column
  }

  /** Runs through the whole table and dumps all the data.
   *  like : select * from <table>
   */

  def print = {
    this.table map {
      case (k, v) =>
        v map {
          case (k1, v1) => println("Key: "+ k1 + " -- Value :" + v1)
        }
    }
  }

  /** Returns the list of all column names
   */

  def colHeaders = columnHeaders.toList


  /** prints number of rows in the table
   */

  def rows = this.table.keys

  /** selects the row with very limited filter capacity.
   *
   * @param column column to query
   *
   * @param value value to match
   *
   * @return Array containg all the matched rows
   */
  def select(column: String, value: Any): Array[collection.mutable.HashMap[C, V]] = {
    var result = new ArrayBuffer[collection.mutable.HashMap[C, V]] ()
    this.table map { case (id, innerMap) =>
      innerMap map {
        case (k, v) => {
          if (column == k) {
            val mtch =  value match {
              case v1: List[Any] => v1.asInstanceOf[List[String]].contains(value)
              case _ => v.toString == value
            }
            if (mtch) result += innerMap
          }
        }
      }
    }
    result.toArray
  }
}

/** Companion object of Class Table
 */

object Table {
  /** Creates an instance of Class Table
   */
  def empty[K, C, V] = new Table[K, C, V] ()
}



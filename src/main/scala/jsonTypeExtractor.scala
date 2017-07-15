package searchengine

class CC[T] {
  def unapply(a:Option[Any]):Option[T] = if (a.isEmpty) {
    None
  } else {
    Some(a.get.asInstanceOf[T])
  }
}

object M extends CC[Map[String, Any]]
object L extends CC[List[String]]
object S extends CC[String]
object D extends CC[Double]
object B extends CC[Boolean]

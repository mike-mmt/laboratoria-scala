def wystąpienia(arg: List[Int]): List[(Int,Int)] = {
  @annotation.tailrec
  def pomocnicza(arg: List[Int], acc: List[(Int,Int)] = Nil): List[(Int,Int)] = {
    arg match {
      case Nil => acc
      case head :: tail => 
        if (contains(acc, head)) {
          pomocnicza(tail, addOneToElemCount(acc, head, Nil))
        } else {
        pomocnicza(tail, (head, 1) :: acc)
        }
    }
  }
  @annotation.tailrec
  def contains(arg: List[(Int,Int)], element: Int, acc: Boolean = false): Boolean = arg match {
    case Nil => acc
    case head :: tail => if (head._1 == element) true else contains(tail, element, acc) 
  }
  @annotation.tailrec
  def addOneToElemCount(arg: List[(Int,Int)], element: Int, acc: List[(Int,Int)] = Nil): List[(Int,Int)] = arg match {
    case Nil => acc.reverse
    case head :: tail if (head._1 == element) => addOneToElemCount(tail, element, (element, head._2 + 1) :: acc)
    case head :: tail => addOneToElemCount(tail, element, head :: acc)
  }
  pomocnicza(arg).reverse
}

/*
  Poprawność rozwiązania należy testować (z poziomu SBT) poleceniem:
  testOnly Test1
*/


@main def zad_1: Unit = {
  // „program główny” ma znaczenie czysto pomocnicze
  val arg = List(1,2,3,3,2,1)
  val wyn = wystąpienia(arg)
  println(wyn)
}

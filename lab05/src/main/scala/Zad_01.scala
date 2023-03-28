package lab05

// def dodaj(m: Int)(n: Int): Int = m + n
// val wynikCzęściowy = dodaj(2)

def isOrdered[A](leq: (A, A) => Boolean)(l: List[A]): Boolean = {
  @annotation.tailrec
  def pomocnicza(l: List[A]): Boolean = l match {
    case Nil => true
    case head :: second :: tail => if (leq(head,second)) pomocnicza(second :: tail)
      else false
    case head :: Nil => true
    }
  pomocnicza(l)
}

@main def zadanie_01: Unit = {
  // Program powinien umożliwić „sprawdzenie” działania
  // funkcji „isOrdered”.
  val lt = (m: Int, n: Int) => m < n
  val lte = (m: Int, n: Int) => m <= n
  val lista = List(1, 2, 2, 5)
  println(isOrdered(lt)(lista)) // ==> false
  println(isOrdered(lte)(lista)) // ==> true
}


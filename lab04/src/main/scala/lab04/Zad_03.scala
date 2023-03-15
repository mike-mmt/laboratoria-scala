package lab04

def sumuj(l: List[Option[Double]]): Option[Double] = {
    @annotation.tailrec
    def pomocnicza(lista: List[Option[Double]], akumulator: Double = 0): Option[Double] = lista match {
      case Nil => if (akumulator > 0) Some(akumulator) else None
      case Some(hd) :: tail => pomocnicza(tail, hd + akumulator)
      case _ :: tail => pomocnicza(tail, akumulator)
    }
    pomocnicza(l)
}

@main def zadanie_03: Unit = {
  // Program powinien umożliwić „sprawdzenie” działania
  // funkcji „sumuj”.
  val lista = List(Some(4.0), Some(-3.0), None, Some(1.0), Some(0.0))

  println(s"${sumuj(lista)}") // true
}

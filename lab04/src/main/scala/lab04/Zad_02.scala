package lab04

def tasuj(l1: List[Int], l2: List[Int]): List[Int] = {
    def pomocnicza(lista1: List[Int], lista2: List[Int], akumulator: List[Int]): List[Int] = (lista1, lista2) match {
      case (Nil, Nil) => akumulator.reverse
      case (Nil, h2 :: t2) => pomocnicza(Nil, t2, h2 :: akumulator)
      case (h1 :: t1, Nil) => pomocnicza(t1, Nil, h1 :: akumulator)
      case (h1 :: t1, h2 :: t2) =>
        if (h1 < h2) {
          if (akumulator.isEmpty || akumulator.head != h1) pomocnicza(t1, lista2, h1 :: akumulator)
           else pomocnicza(t1, lista2, akumulator)
        } else if (h2 < h1) {
          if (akumulator.isEmpty || akumulator.head != h2) pomocnicza(lista1, t2, h2 :: akumulator)
          else pomocnicza(lista1, t2, akumulator)
        } else { 
          if (akumulator.isEmpty || akumulator.head != h1) pomocnicza(t1, t2, h1 :: akumulator)
          else pomocnicza(t1, t2, akumulator)
        }
    }
    pomocnicza(l1,l2,Nil)
}

@main def zadanie_02: Unit = {
  // Program powinien umożliwić „sprawdzenie” działania
  // funkcji „tasuj”.
  val lista1 = List(2, 4, 3, 5)
  val lista2 = List(1, 2, 2, 3, 1, 5)
  val wynik = tasuj(lista1, lista2)
  println(s"$wynik, czy funkcja działa zgodnie z oczekiwaniami? : ${wynik == List(1, 2, 3, 1, 4, 3, 5)}")
}

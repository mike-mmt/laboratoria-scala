package lab05

def deStutter[A](lista: List[A]): List[A] = {
    // Nil
    lista.foldLeft(List[A]())((acc, value) => if (!acc.isEmpty && acc.reverse.head == value) acc else acc :+ value)
}

@main def zadanie_02: Unit = {
  // Program powinien umożliwić „sprawdzenie” działania
  // funkcji „de    Stutter”.
  val l = List(1, 1, 2, 4, 4, 4, 1, 3)
  val wynik = deStutter(l)
  println(wynik)
  println(wynik == List(1, 2, 4, 1, 3) ) // ==> true
}

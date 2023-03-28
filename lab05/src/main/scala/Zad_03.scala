package lab05

// List(1,2,3).map(n => n * 2)
// List[Int] = List[2, 4, 6]

// List(1,2,3).flatMap(n => Set(n))
// List[Int] = List(1, 2, 3)

// List(1,2,3).flatMap(n => Set(123,n))
// List[Int] = List(123, 1, 123, 2, 123, 3)

// List(1,2,3).Map(n => Set(123,n))
// List[Int] = List(Set(123, 1), Set(123, 2), Set(123, 3))

def chessboard: String = {
  val litery = 'a' to 'h'
  val liczby = (8 to 1 by -1).map(_.toString)
  
  liczby.flatMap(liczba => litery.map(litera => (litera, liczba)).mkString(" ") + "\n").mkString("")
}

@main def zadanie_03: Unit = {
  // Program powinien umożliwić „sprawdzenie” działania
  // funkcji „szachownica”.
  println(chessboard)
}

package lab02

def jestPierwsza(n: Int): Boolean = {
  require(n >= 2)

  val range = 2 to n/2
  // for { i <- range } if (pomocnicza(n, i)) { false }

  def pomocnicza(a: Int, b: Int): Boolean = a%b == 0

  !(for { i <- range } yield pomocnicza(n, i)).contains(true)
}

@main def zad_01: Unit = {
  print("Podaj liczbę naturalną: ")
  val liczba = io.StdIn.readInt()
  val wynik = if jestPierwsza(liczba) then "" else " nie"
  println(s"Liczba $liczba$wynik jest liczbą pierwszą")
}

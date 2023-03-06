package lab02

def jestPierwsza(n: Int): Boolean = {
  require(n >= 2)
  // „trzy znaki zapytania” oznaczają wartość
  // (jeszcze) „niezdefiniowaną” – wygodne na
  // wstępnym etapie implementacji, kiedy
  // nie wszystkie elementy programu mamy już
  // „w ręku”…

  // Gdyby ktoś chciał wykorzystać funkcję „pomocniczą”
  // to może ją zdefiniować wewnątrz „jestPierwsza”, np.
  val range = 2 until n/2
  for { i <- range } if (pomocnicza(n, i)) { return false }


  def pomocnicza(a: Int, b: Int): Boolean = a%b == 0

  // „ostatnie napotkane” wyrażenie zwracane jest jako wynik
  // działania funkcji – np.
  true
}

@main def zad_01: Unit = {
  print("Podaj liczbę naturalną: ")
  val liczba = io.StdIn.readInt()
  val wynik = if jestPierwsza(liczba) then "" else " nie"
  println(s"Liczba $liczba$wynik jest liczbą pierwszą")
}

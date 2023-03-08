package lab03

def hipoteza(liczba: Int): Unit = {

  @annotation.tailrec
  def pomocnicza(liczba: Int, a: Int = 2, b: Int = liczba-2): (Int, Int) = {
    
    if (b == 2 && a != 2) (0, 0)  // "false"
    else if (czyPierwsza(a) && czyPierwsza(b)) (a, b) // para liczb spełniających kryteria
    else pomocnicza(liczba, a+1, b-1)
    
  }
  @annotation.tailrec
  def czyPierwsza(liczba: Int, i: Int = 2): Boolean = {
    if (liczba <= 2) (liczba == 2)
    else if (liczba % i == 0) false
    else if (i * i > liczba) true
    else czyPierwsza(liczba, i+1)
  }

  val wynik = pomocnicza(liczba)
  if (wynik == (0, 0)) println("Nie udało się znaleźć takich liczb")
  else println(s"Liczba ${liczba} jest sumą liczb pierwszych: ${wynik(0)} i ${wynik(1)}")

}

@main def zad_02(liczba: Int): Unit = {
  require(liczba > 2)
  require(liczba % 2 == 0)
  // Zdefiniuj funkcję hipoteza, która jako argument pobiera
  // parzystą liczbę naturalną większą od 2 oraz
  // sprawdza czy jest ona sumą dwóch liczb pierwszych.
  // Jeżeli tak, to funkcja hipoteza powinna wypisać je na
  // konsoli. W przeciwnym wypadku na konsoli powinien pojawić
  // się komunikat mówiący, że liczb takich nie udało sie znaleźć.
  hipoteza(liczba)
}

package lab03

def reverse(napis: String): String = {
  // @annotation.tailrec
  def pomocnicza( /*argumenty*/ ): String = {
    ???
  }
  "*"
}

@main def zad_03(napis: String): Unit = {
  // Napisz funkcje reverse, która dla podanego napisu
  // zwraca odwrócony napis. Wykorzystaj operacje „head”
  // oraz „tail” na napisach
  val odwrócony = reverse(napis)
  println(s"$napis od końca to $odwrócony")
}

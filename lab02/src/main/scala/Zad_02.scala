package lab02

/*
  Funkcja „obramuj” „zdefiniowana” poniżej wykorzystuje dwa parametry
    - „napis” potencjalnie może mieć kilka linijek (patrz przykład)
    - „znak”, z którego należy zbudować ramkę
*/
def obramuj(napis: String, znak: Char): String = {
  // definiujemy funkcję obramowującą
  val splitString = napis.split("\n")
  // val range = 0 until .length
  val maxWordLength = splitString.maxBy(_.length).length
  
    // for { el <- splitString } {
    //   if (el.length > maxWordLength) maxWordLength = el.length
    // }
  val arrOfStringsMultiline = for {
    word <- splitString
  } yield s"${znak} ${word}${" " * (maxWordLength - word.length)} ${znak}\n"
  
  s"${znak.toString * (maxWordLength + 4)}\n${arrOfStringsMultiline.mkString("")}${znak.toString * (maxWordLength+4)}"
  
}

@main def zad_02: Unit = {
  val wynik = obramuj("ala\nma\nkota", '*')
  println(wynik)
  /*
    Efektem powino być coś podobnego do:

    ********
    * ala  *
    * ma   *
    * kota *
    ********

  */
}

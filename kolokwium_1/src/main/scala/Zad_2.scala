//==========================================================================
// Metoda porównuje napisy zgodnie z polskim porządkiem alfabetycznym
// Jedyna zmiana jaka może być tutaj potrzebna to „zamiana komentarzy”
// w linijkach 9 oraz 10.
//--------------------------------------------------------------------------
def ltePL(s1: String, s2: String) = {
  import java.util.Locale
  import java.text.Collator
  val locale = new Locale("pl", "PL") // dla starszych wersji JRE/JDK
  // val locale = Locale.of("pl", "PL") // dla nowszych wersji JRE/JDK
  Collator.getInstance(locale).compare(s1, s2) <= 0
}

// Metoda nie wymaga zmian. Wczytuje dane z pliku i zwraca listę linii
def dane: List[String] = {
  import scala.io.Source
  val plik = Source.fromFile("src/main/resources/machiavelli.txt", "UTF-8")
  plik.getLines.toList
}
//==========================================================================

// Jedyna rzecz do zaimplementowania, czyli metoda „wynik”:
def wynik: List[(String,  List[Int])] = {
  // val linieZIndeksami = dane.zipWithIndex.map( (linia,indeks) => (linia, indeks + 1 ))
  val listySłów = dane.map(
    linia => linia.split("\\s+").toList.map(
      słowo => słowo.filter(c => c.isLetter).toLowerCase()
    )
  ).map(listaSłów => listaSłów.distinct).zipWithIndex.map( (linia, indeks) => (linia, indeks + 1))

  // val listaWszystkichSłów = listySłów.flatMap((x,_) => x).distinct

  val listaParSłów = listySłów.map( (lista, indeks) => (lista.map(słowo => (słowo, indeks)), indeks)) // List(List((slowo,1),(slowo,1)...),1)  

  val listaWszystkichParSłów = listaParSłów.flatMap((x,_) => x).distinct

  listaWszystkichParSłów
    .groupBy((słowo, indeks) => słowo)
    .map( (key, value) => (key, value.map((słowo, indeks) => indeks)))
    .toList.sortWith((a,b) => ltePL(a._1,b._1))
    
}

/*
  Poprawność rozwiązania należy testować (z poziomu SBT) poleceniem:
  testOnly Test2
*/

@main def zad_2: Unit = {
  // „program główny” ma znaczenie czysto pomocnicze
  if ltePL("a", "ą") then println("OK")
  else println("to nie powinno się zdarzyć…")
  println(wynik)
}

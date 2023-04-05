package lab07

case class Ocena(
  imię: String,
  nazwisko: String,
  wdzięk: Int,
  spryt: Int
) {
  require(
    // upewniamy się, że składowe Oceny są sensowne
    imię.trim() != "" &&
    nazwisko.trim() != "" &&
    (0 to 20).contains(wdzięk) &&
    (0 to 20).contains(spryt)
  )
}

case class Wynik(
  miejsce: Int,
  imię: String,
  nazwisko: String,
  średniWdzięk: Double,
  średniSpryt: Double,
  suma: Double
) {
  // upewniamy się, że składowe Wyniku są „sensowne”
  require(
    miejsce >= 0 &&
    imię.trim() != "" &&
    nazwisko.trim() != "" &&
    średniWdzięk >= 0 && średniWdzięk <= 20 &&
    średniSpryt >= 0 && średniSpryt <= 20 &&
    suma == średniWdzięk + średniSpryt
  )
}

@main def zad_03: Unit = {
  // Uwaga! Poniższy przykład to jedynie „inspiracja”
  case class O(imię: String, wynik: Int)

  // val wyniki = List(O("Ola",3), O("Ala",1), O("Zosia",2))
  // val klasyfikacja = wyniki.sortBy(o => o match {
  //   case O(_, w) => w
  // })
  // println(klasyfikacja)

  // example values
  val oceny_czesciowe = List(
    Ocena("Vivienne","Southwood",18,7),
    Ocena("Vivienne","Southwood",19,8),
    Ocena("John","Smith",10,11),
    Ocena("John","Smith",9,12),
    Ocena("Brad","Litt",16,15),
    Ocena("Brad","Litt",18,16),
    Ocena("Rick","Olens",17,14),
    Ocena("Rick","Olens",15,13),
    Ocena("Steve","Nest",6,8),
    Ocena("Steve","Nest",7,14)
  )

  val klasyfikacja = oceny_czesciowe
    .groupBy(x => (x.imię, x.nazwisko))
    .map( (key, value) => value.foldLeft(0,Wynik())( (acc, ocena) => (acc._1 + 1, Wynik(1,ocena.imię,ocena.nazwisko, acc._2.średniWdzięk+ocena.wdzięk, acc._2.średniSpryt + ocena.spryt, acc._2.średniWdzięk+ocena.wdzięk + acc._2.średniSpryt + ocena.spryt))))
  println(klasyfikacja)

}

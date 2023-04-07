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
  // case class O(imię: String, wynik: Int)

  // val wyniki = List(O("Ola",3), O("Ala",1), O("Zosia",2))
  // val klasyfikacja = wyniki.sortBy(o => o match {
  //   case O(_, w) => w
  // })
  // println(klasyfikacja)

  // example values
  val oceny_czesciowe = List(
    Ocena("Vivienne","Southwood",18,13),
    Ocena("Vivienne","Southwood",19,12),
    Ocena("John","Smith",10,11),
    Ocena("John","Smith",9,12),
    Ocena("Brad","Litt",16,15),
    Ocena("Brad","Litt",18,16),
    Ocena("Rick","Olens",19,12),
    Ocena("Rick","Olens",18,13),
    // Ocena("Rick","Olens",17,14),
    // Ocena("Rick","Olens",18,13),
    Ocena("Steve","Nest",6,8),
    Ocena("Steve","Nest",7,14)
  )

  val klasyfikacja325432 = oceny_czesciowe
    .groupBy(x => (x.imię, x.nazwisko))
    .map( (key, value) =>
      value.foldLeft(0.0,0.0,0.0,Map("imię" -> "", "nazwisko" -> "", "średniWdzięk" -> 0.0, "średniSpryt" -> 0.0, "suma" -> 0.0))( (acc, ocena) =>
        (acc._1 + 1,
         acc._2 + ocena.wdzięk,
         acc._3 + ocena.spryt,
         Map("imię" -> ocena.imię,
          "nazwisko" -> ocena.nazwisko,
          "średniWdzięk" -> (acc._2 + ocena.wdzięk)/(acc._1 + 1),
          "średniSpryt" -> (acc._3 + ocena.spryt)/(acc._1 + 1),
          "suma" -> (((acc._2 + ocena.wdzięk)/(acc._1 + 1)) + ((acc._3 + ocena.spryt)/(acc._1 + 1))).toDouble
          )
        )
        ) 
        )

  val wyniki = oceny_czesciowe
    .groupBy(x => (x.imię, x.nazwisko)) // grupowanie wszystkich wynikow jednego zawodnika razem
    .map( (key, value) => // obliczenie sredniej z ocen
      value.foldLeft(0.0,0.0,0.0,Wynik(1,"a", "a", 0.0, 0.0, 0.0))( (acc, ocena) => // (ilosc ocen, suma wdzieku, suma sprytu, Wynik(...))
        (acc._1 + 1,  // ilość ocen
         acc._2 + ocena.wdzięk, // suma wdzięku
         acc._3 + ocena.spryt,  // suma sprytu
         Wynik(1, ocena.imię,   // tymczasowe miejsce, imie,
          ocena.nazwisko,        // nazwisko
          (acc._2 + ocena.wdzięk)/(acc._1 + 1),  // srednia wdzieku
          (acc._3 + ocena.spryt)/(acc._1 + 1),  // srednia sprytu
          (((acc._2 + ocena.wdzięk)/(acc._1 + 1)) + ((acc._3 + ocena.spryt)/(acc._1 + 1))).toDouble // suma srednich
          )
          ))).map( (_,_,_,result) => result) // usuniecie tymczasowych wartosci uzywanych do obliczania sredniej

  val klasyfikacja = wyniki.toList.sortBy(osoba => (-osoba.suma, -osoba.średniWdzięk, osoba.nazwisko)) // dodanie '-' przed sortowaniem odwraca kolejnosc
    .foldLeft(List[Wynik]())( (acc, osoba) => ( // ewaluacja miejsc kazdego z zawodników
      Wynik(
        if (acc.isEmpty) 1
        else if (acc.head.suma == osoba.suma && acc.head.średniWdzięk == osoba.średniWdzięk) acc.head.miejsce // ex aequo
        else acc.head.miejsce + 1,
        osoba.imię,
        osoba.nazwisko,
        osoba.średniWdzięk,
        osoba.średniSpryt,
        osoba.suma
      ) :: acc
    )).reverse

  println(klasyfikacja)
}

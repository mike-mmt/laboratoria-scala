package jp1.akka.lab13.model

import akka.actor.{Actor, ActorRef, ActorLogging, Props, PoisonPill}
// import jp1.akka.lab13.model

val akkaPathAllowedChars = ('a' to 'z').toSet union
  ('A' to 'Z').toSet union
  "-_.*$+:@&=,!~';.)".toSet

object Organizator {
  case object Start
  // rozpoczynamy zawody – losujemy 50 osób, tworzymy z nich zawodników
  // i grupę eliminacyjną
  case object Runda
  // polecenie rozegrania rundy (kwalifikacyjnej bądź finałowej) –  wysyłamy Grupa.Runda
  // do aktualnej grupy
  case object Wyniki
  // polecenie wyświetlenia klasyfikacji dla aktualnej grupy
  case class Wyniki(w: Map[ActorRef, Option[Ocena]])
  // wyniki zwracane przez Grupę
  case object Stop
  // kończymy działanie
}

class Organizator extends Actor with ActorLogging {
  // importujemy komunikaty na które ma reagować Organizator
  import Organizator._

  def receive: Receive = {
    case Start =>
      // tworzenie 50. osób, opdowiadających im Zawodników
      // oraz Grupy eliminacyjnej
      val zawodnicy = List.fill(50) {
        val o = Utl.osoba()
        context.actorOf(Props(Zawodnik(o)), s"${o.imie}-${o.nazwisko}" filter akkaPathAllowedChars)
      }
      log.info("Rozpoczynamy zawody")
      context.become(oczekiwanieNaRundęKwalifikacyjną(zawodnicy))
  
    case Stop =>
      log.info("kończymy zawody...")
      context.system.terminate()
  }

  def sumaOcen(ocena: Ocena): Int = {
        ocena.nota1 + ocena.nota2 +ocena.nota3
      }
  // def sumaOcen(fallback: Int): Int = fallback

  def oczekiwanieNaRundęKwalifikacyjną(zawodnicy: List[ActorRef]): Receive = {
    case Runda =>
      val grupa = context.actorOf(Props(Grupa(zawodnicy)), s"grupa-kwal")
      grupa ! Grupa.Runda
      log.info("Rozpoczynamy rundę kwalifikacyjną")
      context.become(rundaKwalifikacyjna(grupa, zawodnicy))
    case Stop =>
      log.info("kończymy zawody...")
      context.system.terminate()
    case msg =>
      log.info("$msg")
  }

  def rundaKwalifikacyjna(obecnaGrupa: ActorRef, zawodnicy: List[ActorRef]): Receive = {
    case Wyniki(w) =>
      log.info(s"Otrzymałem wyniki")
      obecnaGrupa ! PoisonPill
      /*
      o1 > o2 jeśli:
        - suma1 > suma2  (suma = nota1 + nota3 + nota3)
        - suma1 == suma2 oraz o1.nota1 > o2.nota1
        - suma1 == suma2, o1.nota1 == o2.nota1 oraz o1.nota3 > o2.nota3

      Jeśli o1 i o2 mają identyczne wszystkie noty to mamy REMIS

      */
      val wynikiFinalistów = w
        .toList
        .sortWith((a,b) => (a._2,b._2) match {
          case (None, None) =>
            true
          case (Some(_), None) =>
            true
          case (None, Some(_)) =>
            false
          case (Some(av), Some(bv)) =>
            sumaOcen(av) > sumaOcen(bv) || (sumaOcen(av) == sumaOcen(bv) && av.nota1 > bv.nota1)
        })
        .take(20)

        val wynikiFinalistów2 = w
          .toList
          .filter((_, optionOcena) => optionOcena.isDefined)  // filtrowanie wyników None
          .sortBy( (_, optionOcena) => (-sumaOcen(optionOcena.get), -optionOcena.get._1, -optionOcena.get._2, -optionOcena.get._3))
          .take(20)
        log.info("Wyłoniłem 20 zawodników przechodzących do finału")
      
      context.become(oczekiwanieNaRundęFinałową(wynikiFinalistów2.map((k,v) => k).toList, wynikiFinalistów2))
    case Stop =>
      log.info("kończymy zawody...")
      context.system.terminate()
  }

  def oczekiwanieNaRundęFinałową(zawodnicy: List[ActorRef], wyniki: List[(ActorRef, Option[Ocena])]): Receive = {
    case Runda =>
      val nowaGrupa = context.actorOf(Props(Grupa(zawodnicy)), s"grupa-fin")
      nowaGrupa ! Grupa.Runda
      log.info("Rozpoczynamy rundę finałową")
      context.become(rundaFinałowa(nowaGrupa, zawodnicy, wyniki.toMap))
    case Wyniki =>
      // val ranking = wyniki.foldLeft(List[(ActorRef, Option[Ocena], Int)])( (acc, zawodnik) =>
      //       ((zawodnik._1,
      //       zawodnik._2,
      //       if acc.isEmpty 1
      //       else if (sumaOcen(acc.head._2.get) == sumaOcen(zawodnik._2.get) && acc.head._2.get._1 == zawodnik._2.get._1 && acc.head._2.get._2 == zawodnik._2.get._2 && acc.head._2.get._3 == zawodnik._2.get._3) acc.head._3
      //       else acc.head._3 + 1
      //       ) :: acc).reverse
      //     )
      val ranking = wyniki
        .zipWithIndex // ((ActorRef, Some(...)), index)
        .map((zawodnik,i) => (zawodnik._1,zawodnik._2.get,i+1))    // (ActorRef, Ocena(...), Miejsce)  możemy użyć .get bezkarnie ponieważ wcześniej zostały wyfiltrowane None
        .foldLeft(List[(ActorRef, Ocena, Int)]())( (acc, zawodnik) => // poprawianie miejsc ex aequo
          (zawodnik._1,
          zawodnik._2,
          if acc.isEmpty then zawodnik._3
          else if (sumaOcen(zawodnik._2) == sumaOcen(acc.head._2) && zawodnik._2._1 == acc.head._2._1 && zawodnik._2._2 == acc.head._2._2 && zawodnik._2._3 == acc.head._2._3) acc.head._3
          else zawodnik._3) +: acc
        ).reverse

      // log.info(s"$ranking")
      // val drukowanaWiadomosc = wyniki.foldLeft("")((acc,zawodnik) => s"$acc\n${zawodnik._1.path.name}: ${zawodnik._2.getOrElse(0)} Suma: ${sumaOcen(zawodnik._2.getOrElse(Ocena(0,0,0)))}")
      val drukowanaWiadomosc = ranking.foldLeft("")((acc,zawodnik) => s"$acc\n${zawodnik._1.path.name}: ${zawodnik._2} Suma: ${sumaOcen(zawodnik._2)} Miejsce: ${zawodnik._3}")
      log.info(s"$drukowanaWiadomosc")
    case Stop =>
      log.info("kończymy zawody...")
      context.system.terminate()
  }

  def rundaFinałowa(grupaFinałowa: ActorRef, zawodnicy: List[ActorRef], wynikiKwalifikacji: Map[ActorRef, Option[Ocena]]): Receive = {
    case Wyniki(w) =>
      grupaFinałowa ! PoisonPill
      val wynikiKońcowe = w
        .map( (z,o) =>            // dodawanie wyników z rundy kwalifikacji i finałowej
          val ocenaFinałowa = o.getOrElse(Ocena(0,0,0)) 
          val ocenaKwalifikacyjna = wynikiKwalifikacji.get(z).getOrElse(Some(Ocena(0,0,0)))
          (z, Ocena(ocenaFinałowa._1 + ocenaKwalifikacyjna.get._1, ocenaFinałowa._2 + ocenaKwalifikacyjna.get._2, ocenaFinałowa._3 + ocenaKwalifikacyjna.get._3))
          )
        .toList   
        .sortBy( (_, ocena) => (-sumaOcen(ocena), -ocena._1, -ocena._2, -ocena._3)) // sortowanie   '-' odwraca kolejność sortowania
        .zipWithIndex // ((ActorRef, Ocena(...)), index)    
        .map((zawodnik,i) => (zawodnik._1,zawodnik._2,i+1))    // (ActorRef, Ocena(...), Miejsce)    <- usunięcie niepotrzebnych "nawiasów", sprawienie ze miejsca zaczynaja sie od 1 a nie 0
        .foldLeft(List[(ActorRef, Ocena, Int)]())( (acc, zawodnik) =>   // poprawianie miejsc ex aequo
          (zawodnik._1,
          zawodnik._2,
          if acc.isEmpty then zawodnik._3
          else if (sumaOcen(zawodnik._2) == sumaOcen(acc.head._2) && zawodnik._2._1 == acc.head._2._1 && zawodnik._2._2 == acc.head._2._2 && zawodnik._2._3 == acc.head._2._3) acc.head._3
          else zawodnik._3) +: acc
        ).reverse
      context.become(poFinale(zawodnicy, wynikiKońcowe))
    case Stop =>
      log.info("kończymy zawody...")
      context.system.terminate()  
  }
  
  def poFinale(zawodnicy: List[ActorRef], wynikiKońcowe: List[(ActorRef, Ocena, Int)]): Receive = {
    case Wyniki =>
      val drukowanaWiadomosc = wynikiKońcowe.foldLeft("")((acc,zawodnik) => s"$acc\n${zawodnik._1.path.name}: ${zawodnik._2} Suma: ${sumaOcen(zawodnik._2)} Miejsce: ${zawodnik._3}")
      log.info(s"$drukowanaWiadomosc")
    case Stop =>
      log.info("kończymy zawody...")
      context.system.terminate()  
  }
}

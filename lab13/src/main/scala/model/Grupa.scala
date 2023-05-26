package jp1.akka.lab13.model

import akka.actor.{Actor, ActorRef, ActorLogging}

object Grupa {
  case object Runda
  // Zawodnicy mają wykonać swoje próby – Grupa
  // kolejno (sekwencyjnie) informuje zawodników
  // o konieczności wykonania próby i „oczekuje”
  // na ich wynik (typu Option[Ocena])
  case object Wyniki
  // Polecenie zwrócenia aktualnego rankingu Grupy
  // Oczywiście klasyfikowani są jedynie Zawodnicy,
  // którzy pomyślnie ukończyli swoją próbę
  case class Wynik(ocena: Option[Ocena])
  // Informacja o wyniku Zawodnika (wysyłana przez Zawodnika do Grupy)
  // np. Wynik(Some(Ocena(10, 15, 14)))
  // Jeśli zawodnik nie ukończy próby zwracana jest wartość Wynik(None)
  case object Koniec
  // Grupa kończy rywalizację
}
class Grupa(zawodnicy: List[ActorRef]) extends Actor with ActorLogging {
  def receive: Receive = {
    case Grupa.Runda =>
      zawodnicy.head ! Zawodnik.Próba
      log.info(s"pierwszy zawodnik: ${zawodnicy.head.path.name} rozpoczął próbę")
      context.become(oczekiwanieNaWynik(sender(), zawodnicy.head, zawodnicy.tail))
    case msg => log.info(s"$msg")
  }

  def oczekiwanieNaWynik(organizator: ActorRef, obecnyZawodnik: ActorRef, pozostaliZawodnicy: List[ActorRef], wyniki: Map[ActorRef, Option[Ocena]] = Map[ActorRef, Option[Ocena]]()): Receive = {
    case Grupa.Wynik(ocena) =>
      if pozostaliZawodnicy.isEmpty then
        log.info("runda zakończona")
        organizator ! Organizator.Wyniki((wyniki + (obecnyZawodnik -> ocena)))
      else
        log.info(s"zawodnik ${obecnyZawodnik.path.name} zakończył próbę")
        log.info(s"zawodnik ${pozostaliZawodnicy.head.path.name} rozpoczyna próbę")
        obecnyZawodnik ! Zawodnik.Próba
        context.become(oczekiwanieNaWynik(organizator, pozostaliZawodnicy.head, pozostaliZawodnicy.tail, wyniki + (obecnyZawodnik -> ocena)))
  }


}

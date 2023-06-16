package kolokwium_2

import akka.actor.{Actor, ActorLogging, Props}
import akka.actor.ActorRef

abstract class DoPracownika
case class Wstaw(słowo: String) extends DoPracownika
case class Start(szef: ActorRef) extends  DoPracownika
case class Licz(słowo: String, pełneSłowo: String) extends DoPracownika

class Pracownik extends Actor with ActorLogging {
  def receive: Receive = {
    case Start(szef) =>
      context.become(obsługa(szef, Map[Char, ActorRef](), 0))
    // case msg => log.info(s"Odebrałem wiadomość w stanie receive: ${msg}")
  }

  def obsługa(szef: ActorRef, mapaPracownikow: Map[Char, ActorRef], ilośćSłów: Int): Receive = {
    case Wstaw(słowo) =>
      if słowo.length == 1 then
        context.become(obsługa(szef, mapaPracownikow, ilośćSłów + 1))
      else
        mapaPracownikow.get(słowo.tail.head) match
          case Some(pracownik) => // mam pracownika, wysylam mu resztę słowa
            pracownik ! Wstaw(słowo.tail)
          case None =>  // nie mam pracownika
            // log.info(s"tworze nowego pracownika ${słowo.tail.head}")
            val nowyPracownik = context.actorOf(Props[Pracownik]())
            nowyPracownik ! Start(szef)
            nowyPracownik ! Wstaw(słowo.tail)
            context.become(obsługa(szef, mapaPracownikow + (słowo.tail.head -> nowyPracownik), ilośćSłów))

    case Licz(słowo, pełneSłowo) =>
      if słowo.length == 1 then
        szef ! Ile(pełneSłowo, ilośćSłów)
      else
        mapaPracownikow.get(słowo.tail.head) match
          case Some(pracownik) =>
            pracownik ! Licz(słowo.tail, pełneSłowo)
          case None =>  // nie ma takiego słowa
            szef ! Ile(pełneSłowo, 0) 
    // case msg => log.info(s"Odebrałem wiadomość w stanie obsługa: ${msg}")
  }
}

package kolokwium_2

import akka.actor.{Actor, ActorLogging, Props}
import akka.actor.ActorRef

abstract class DoSzefa
case class W(słowo: String) extends DoSzefa
case class I(słowo: String) extends DoSzefa
case class Ile(słowo: String, n: Int) extends DoSzefa

class Szef extends Actor with ActorLogging {
  def receive: Receive = {
    case W(słowo) =>  // pierwsze słowo
      val pierwszyPracownik = context.actorOf(Props[Pracownik]())
      val mapaPracownikow: Map[Char, ActorRef] = Map(słowo.head -> pierwszyPracownik)
      pierwszyPracownik ! Start(self)
      pierwszyPracownik ! Wstaw(słowo)
      context.become(obsługa(mapaPracownikow))
    case I(słowo) =>
      log.info(s"Ilość słów '$słowo': 0")
  }

  def obsługa(mapaPracownikow: Map[Char, ActorRef]): Receive = {
    case W(słowo) =>
      mapaPracownikow.get(słowo.head) match
        case Some(pracownik) => // mam już pode mną pracownika z pierwszą literą słowa
          pracownik ! Wstaw(słowo)
        case None =>  // nie mam jeszcze takiego pracownika
          val nowyPracownik = context.actorOf(Props[Pracownik]())
          nowyPracownik ! Start(self)
          nowyPracownik ! Wstaw(słowo)
          context.become(obsługa(mapaPracownikow + (słowo.head -> nowyPracownik)))

    case I(słowo) =>
      mapaPracownikow.get(słowo.head) match
        case None => // nie ma takiego słowa
          log.info(s"Ilość słów '$słowo': 0")
        case Some(pracownik) =>
          pracownik ! Licz(słowo, słowo)

    case Ile(słowo, n) =>
      log.info(s"Ilość słów '$słowo': $n")
      
  }
}

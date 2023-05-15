package lab11_3

import akka.actor.{ActorSystem, Actor, ActorLogging, ActorRef, Props}

case object Piłeczka
case class Graj(przeciwnik: ActorRef)
case class Graj2(przeciwnik: ActorRef, maks: Int)
case class Graj3(przeciwnicy: List[ActorRef])

class Gracz03 extends Actor with ActorLogging {
  def receive: Receive = {
    case Graj3(przeciwnicy) =>
      val następnyPrzeciwnik = przeciwnicy.head
      log.info(s"odbijam do ${następnyPrzeciwnik}")
      następnyPrzeciwnik ! Graj3(przeciwnicy.tail :+ self :+ przeciwnicy.head)
      context.become(gramy(następnyPrzeciwnik))
    // case Piłeczka =>
    //   sender() ! Piłeczka
    //   context.become(gramy)
    case msg => log.info(s"Odebrałem wiadomość: ${msg}")
  }

  def gramy(następnyPrzeciwnik: ActorRef): Receive = {
    case Graj3(_) =>
      log.info(s"odbijam do ${następnyPrzeciwnik.path}")
      następnyPrzeciwnik ! Piłeczka
    case Piłeczka =>
      log.info(s"odbijam do ${następnyPrzeciwnik.path}")
      następnyPrzeciwnik ! Piłeczka
  }
}

@main def main3: Unit = {
  val system = ActorSystem("Zawody")
  val gracz1 = system.actorOf(Props[Gracz03](), "gracz1")
  val gracz2 = system.actorOf(Props[Gracz03](), "gracz2")
  val gracz3 = system.actorOf(Props[Gracz03](), "gracz3")
  // val gracz4 = system.actorOf(Props[Gracz03](), "gracz4")
  val przeciwnicy = List(gracz2, gracz3)
  gracz1 ! Graj3(przeciwnicy)
}

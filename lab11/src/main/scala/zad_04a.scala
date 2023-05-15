package lab11_4a

import akka.actor.{ActorSystem, Actor, ActorLogging, ActorRef, Props}

case object Piłeczka
case class Graj(przeciwnik: ActorRef)
case class Graj2(przeciwnik: ActorRef, maks: Int)
case class Graj3(przeciwnicy: List[ActorRef])
case class Graj4a(przeciwnicy: List[ActorRef])

class Gracz04 extends Actor with ActorLogging {
  def receive: Receive = {
    case Graj4a(przeciwnicy) =>
      val następnyPrzeciwnik = przeciwnicy.head
      log.info(s"odbijam do ${następnyPrzeciwnik}")
      następnyPrzeciwnik ! Graj4a(przeciwnicy.tail :+ self :+ przeciwnicy.head)
      context.become(gramy(następnyPrzeciwnik))
    // case Piłeczka =>
    //   sender() ! Piłeczka
    //   context.become(gramy)
    case msg => log.info(s"Odebrałem wiadomość: ${msg}")
  }

  def gramy(następnyPrzeciwnik: ActorRef): Receive = {
    case Graj4a(_) =>
      log.info(s"odbijam do ${następnyPrzeciwnik.path}")
      następnyPrzeciwnik ! Piłeczka
    case Piłeczka =>
      log.info(s"odbijam do ${następnyPrzeciwnik.path}")
      następnyPrzeciwnik ! Piłeczka
  }
}

@main def main4a: Unit = {
  val system = ActorSystem("Zawody")
  // val gracz4 = system.actorOf(Props[Gracz03](), "gracz4")
  val actorCount = 5
  val listOfActors = (for {
    i <- 1 to actorCount
  } yield system.actorOf(Props[Gracz04](), s"gracz$i")).toList
  // val przeciwnicy = List(gracz2, gracz3)
  listOfActors.head ! Graj4a(listOfActors.tail :+ listOfActors.head)
}

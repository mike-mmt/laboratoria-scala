package lab11_4b

import akka.actor.{ActorSystem, Actor, ActorLogging, ActorRef, Props}
import scala.util.Random
val rand = scala.util.Random()

case class Piłeczka(gracze: List[ActorRef])
// case class Graj(przeciwnik: ActorRef)
// case class Graj2(przeciwnik: ActorRef, maks: Int)
// case class Graj3(przeciwnicy: List[ActorRef])
case class Graj4a(przeciwnicy: List[ActorRef])
case class Graj4b(przeciwnicy: List[ActorRef])

class Gracz04 extends Actor with ActorLogging {
  def receive: Receive = {
    case Graj4b(gracze) =>
      val następnyGracz = gracze(rand.nextInt(gracze.length))
      log.info(s"odbijam do ${następnyGracz.path}")
      następnyGracz ! Piłeczka(gracze)
      context.become(gramy)
    case Piłeczka(gracze) =>
      val następnyGracz = gracze(rand.nextInt(gracze.length))
      log.info(s"odbijam do ${następnyGracz.path}")
      następnyGracz ! Piłeczka(gracze)
      context.become(gramy)
    case msg => log.info(s"Odebrałem wiadomość: ${msg}")
  }

  def gramy: Receive = {
    case Piłeczka(gracze) =>
      val następnyGracz = gracze(rand.nextInt(gracze.length))
      log.info(s"odbijam do ${następnyGracz.path}")
      następnyGracz ! Piłeczka(gracze)
  }
}

@main def main4b: Unit = {
  val system = ActorSystem("Zawody")
  // val gracz4 = system.actorOf(Props[Gracz03](), "gracz4")
  val actorCount = 5
  val listOfActors = (for {
    i <- 1 to actorCount
  } yield system.actorOf(Props[Gracz04](), s"gracz$i")).toList
  // val przeciwnicy = List(gracz2, gracz3)
  listOfActors.head ! Graj4b(listOfActors.tail :+ listOfActors.head)
}

package lab11_1

import akka.actor.{ActorSystem, Actor, ActorLogging, ActorRef, Props}

case object Piłeczka
case class Graj(przeciwnik: ActorRef)
case class Graj2(przeciwnik: ActorRef, maks: Int)

class Gracz01 extends Actor with ActorLogging {
  def receive: Receive = {
    case Graj(przeciwnik) =>
      log.info(s"serwuję do ${przeciwnik.path}")
      przeciwnik ! Piłeczka
      context.become(gramy)
    case Piłeczka => 
      sender() ! Piłeczka
      context.become(gramy)
    case msg => log.info(s"Odebrałem wiadomość: ${msg}")
  }

  def gramy: Receive = {
    case Piłeczka =>
      val przeciwnik = sender()
      log.info(s"odbijam do ${przeciwnik.path}")
      sender() ! Piłeczka
  }
  // def gramy02: Receive = {
  //   case Piłeczka =>
  //     val przeciwnik = sender()
  //     log.info(s"pong do ${przeciwnik.path}")
  //     sender() ! Piłeczka
  // }
}

// class Gracz02 extends Actor with ActorLogging {
//   def receive: Receive = {
//     case Piłeczka =>
//       println("pong")
//       sender() ! Piłeczka
//       // log.info("Oskar? No i świetnie! Idę na emeryturę!")
//       // context.system.terminate()
//     case msg => log.info(s"Odebrałem wiadomość: ${msg}")
//   }
// }

@main def main: Unit = {
  val system = ActorSystem("Zawody")
    val gracz1 = system.actorOf(Props[Gracz01](), "gracz1")
    val gracz2 = system.actorOf(Props[Gracz01](), "gracz2")
    // gracz1 ! Graj(gracz2)
    gracz1 ! Graj2(gracz2, 8)
}

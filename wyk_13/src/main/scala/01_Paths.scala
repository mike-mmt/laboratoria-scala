package jp1.akka_13_02.paths

import akka.actor.{ActorSystem, Actor, ActorLogging, ActorRef, Props}

class Worker extends Actor with ActorLogging {
  def receive: Receive = {
    case msg => log.info(s"dostałem $msg")
  }
}

// val w = new Watcher() // Watcher()
// w.x
class Watcher extends Actor with ActorLogging {
 
  val selection = context.actorSelection("/user/myPrecious")

  def receive: Receive = {
    case msg =>
      log.info("wiadomość: $msg")
      selection ! "Aqq"
  }
}

case object Komunikat

@main def main: Unit = {
  val system = ActorSystem("Teatr")
  val worker1 = system.actorOf(Props[Worker](), "myPrecious")
  val worker2 = system.actorOf(Props[Worker](), "myPreciousA")
  val worker3 = system.actorOf(Props[Worker](), "myPreciousB")
  //.../user/myPrecious/dziecko/myPrecious
  val watcher = system.actorOf(Props[Watcher](), "szef")
  watcher ! Komunikat
}

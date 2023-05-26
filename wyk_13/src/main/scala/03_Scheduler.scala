import akka.actor.ActorLogging
import akka.actor.Actor
import akka.actor.Actor.Receive
import scala.concurrent.duration.FiniteDuration
import akka.actor.Props

class Worker extends Actor with ActorLogging {
    def receive: Receive = {
      case msg =>
        log.info(s"dostałem $msg")
        sender() ! msg
        log.info(s"odpowiedź do ${sender()}")
    }
}

// def myWhile(cond: => Boolean)(cmd: => Unit): Unit = { ... }         // => sprawia że wartość wyliczana jest dopiero wtedy gdy jest potrzebna

object Boss {
  def props(delay: FiniteDuration): Props = {
    Props(classOf(Boss), delay)
  }
}

class Boss(delay: FiniteDuration) extends Actor with ActorLogging {
  case object Tick
  override def preStart(): Unit = {
    super.preStart()
    implicit val ec = context.dispatcher
    context.system.scheduler.scheduleWithFixedDelay(
      delay,
      delay,
      self,
      Tick
    )
  }

  def receive: Receive = {
    case Tick =>
      log.info("dostałem Tick")
    case msg =>
  }
}

import scala.concurrent.duration.*

@main def main: Unit = {
  val system = ActorSystem("system")
  val boss = system.actorOf(Props[Boss](), "boss")
  val config = system.settings.config
  val delay = config.getInt("planistaDemo.delay")
  val boss = system.actorOf(Boss.props(delay.millis), "boss")
}
package jp1.akka.lab14

import akka.actor.ActorLogging
import akka.actor.Actor
import akka.actor.Actor.Receive
import akka.actor.Props
import akka.actor.ActorSystem
import scala.concurrent.duration.*
import akka.actor.ActorRef
import akka.actor.ActorContext
import scala.util.Random
val rand = scala.util.Random()
// import jp1.akka.lab14.SiłaNiższa.selection
// import jp1.akka.lab14.SiłaNiższa.selection


case object Strzał
case class Przeciwnik(p: ActorRef)
case object Strzała
case object Poległem

class Zamek extends Actor with ActorLogging {
  
  def receive: Receive = {
    case Strzał =>
      log.info(s"Nie wiemy gdzie strzelać!")
    case Przeciwnik(p) => 
      val obrońcy: List[ActorRef] = (for {
        i <- 1 to 100
      } yield context.actorOf(Props(classOf[Obrońca], p), s"obronca$i")).toList
      context.become(gotowyDoAtaku(p, obrońcy))
  }
  def gotowyDoAtaku(przeciwnik: ActorRef, obrońcy: List[ActorRef]): Receive = {
    case Strzał =>
      obrońcy.foreach((obrońca) => obrońca ! Strzał)
    case Strzała => 
      val randomNum = rand.nextInt(100)
      if randomNum < 20 then
        val cel = obrońcy(rand.nextInt(obrońcy.length))
        cel ! Strzała
    case Poległem =>
      val poległy = sender()
      context.stop(poległy)
      val filteredList = obrońcy.filter((obronca) => obronca != poległy)
      if filteredList.nonEmpty then
        context.become(gotowyDoAtaku(przeciwnik, filteredList))
      else
        log.info("Przegrana!")
        context.system.terminate()

  }
}

class Obrońca(przeciwnik: ActorRef) extends Actor with ActorLogging {
  def receive: Receive = {
    case Strzał => 
      przeciwnik ! Strzała
    case Strzała =>
      val randomNum2 = rand.nextInt(100)
        if randomNum2 < 40 then
          log.info(s"strzała trafiła ${self.path}")
          sender() ! Poległem
          context.become(poległy)
  }
  def poległy: Receive = {
    case _ =>
  }
}

class SiłaNiższa extends Actor with ActorLogging {
  val selection = context.actorSelection("/user/Zamek*")
  def receive: Actor.Receive = {
    case Strzał =>
      log.info(s"\n${"-" * 40}\nStrzał\n${"-" * 40}")
      selection ! Strzał
  }
}

@main def go: Unit = {
  val system = ActorSystem("system")
  // żeby planista mógł działać „w tle” potrzebuje puli wątków:
  implicit val executionContext = system.dispatcher

  // tworzymy zamki
  val zamekA = system.actorOf(Props[Zamek](), "ZamekA")
  val zamekB = system.actorOf(Props[Zamek](), "ZamekB")
  zamekA ! Przeciwnik(zamekB)
  zamekB ! Przeciwnik(zamekA)
  
  val siłaNiższa = system.actorOf(Props[SiłaNiższa](), "SilaNizsza")

  // wczytujemy konfigurację aplikacji z „resources/application.conf”
  // i odwołujemy się do jej elementu, wyrażonego w milisekundach
  val config = system.settings.config
  val delay = config.getInt("planista.delay").milli

  // uruchamiamy planistę
  system.scheduler.scheduleWithFixedDelay(
    delay,
    delay,
    siłaNiższa,
    Strzał
  )

  // Uwaga! Oczywiście powyższy planista kontaktuje się jedynie
  // z jednym z zamków!!!
}

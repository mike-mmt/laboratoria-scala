import akka.actor.{ActorSystem, Actor, ActorLogging, ActorRef, Props}
import scala.io

class MyActor extends Actor {
    def receive: Receive = {
        case msg =>
            println(s"Odebrałem wiadomość: ${msg}")
            context.become(nextStep(2))
    }
    def nextStep(msgCount: Int): Receive = {
        case msg =>
            println(s"Odebrałem wiadomość nr ${msgCount}: ${msg}")
            context.become(nextStep(msgCount + 1))
    }
}

@main def main2: Unit = {
    val system = ActorSystem("HaloAkka")
    // try {
        val leonardo = system.actorOf(Props[MyActor](), "leonardo")
        leonardo ! "Dostałeś Oskara!"
        leonardo ! "Dostałeś drugiego Oskara!"
        leonardo ! "Dostałeś trzeciego Oskara! Brawo!"
        println(">>>> Naciśnij ENTER żeby zakończyć <<<<")
        io.StdIn.readLine()
    // } finally {
        system.terminate()
    // }
}
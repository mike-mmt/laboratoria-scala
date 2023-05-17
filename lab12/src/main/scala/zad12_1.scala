
import akka.actor.{ActorSystem, Actor, ActorLogging, ActorRef, Props}

case class Oblicz(liczba : Int)
case class Wynik(wynik : Int)

class Boss extends Actor with ActorLogging {
    def receive: Receive = {
      case Oblicz(liczba) =>
        context.actorOf(Props[Pracownik]()) ! Oblicz(liczba)
      case Wynik(liczba) =>
        log.info(s"Wynik: $liczba")
        context.system.terminate()
    }
}

class Pracownik extends Actor with ActorLogging {
    def receive: Receive = {
      case Oblicz(liczba) =>
        if liczba == 0 || liczba == 1 then
          sender() ! Wynik(1)
        else
          val przelozony = sender()
          context.actorOf(Props[Pracownik]()) ! Oblicz(liczba - 1)
          context.actorOf(Props[Pracownik]()) ! Oblicz(liczba - 2)
          context.become(czekamNaWynik(przelozony))
    }

    def czekamNaWynik(przelozony: ActorRef): Receive = {
       case Wynik(liczba) =>
        context.become(półWyniku(przelozony, liczba))
    }

    def półWyniku(przelozony: ActorRef, półWyniku: Int): Receive = {
      case Wynik(liczba) =>
        val wynik = półWyniku + liczba
        przelozony ! Wynik(wynik)
    }
}

@main def main: Unit = {
  val system = ActorSystem("Teatr")
    val szef = system.actorOf(Props[Boss](), "Szef")
    szef ! Oblicz(5)
    szef ! Oblicz(7)
}

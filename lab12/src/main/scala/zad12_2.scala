
import akka.actor.{ActorSystem, Actor, ActorLogging, ActorRef, Props}

case class Oblicz(liczba: Int)
case class Wynik(n: Int, fin_n: BigInt)

class Boss extends Actor with ActorLogging {
    def receive: Receive = {
      case Oblicz(indeks) =>
        context.actorOf(Props[Nadzorca]()) ! Oblicz(indeks)
      case Wynik(liczba) =>
        log.info(s"Wynik: $liczba")
    }
}

class Nadzorca(cache: Map[Int, BigInt] = Map(1 -> 1, 2 -> 1)) extends Actor with ActorLogging {
    def receive: Receive = {
      case Oblicz(key) =>
        if cache.isDefined(key) then
          sender() ! Wynik(key, cache.get(key))
        else
          context.actorOf(Props[Pracownik](key)) ! Oblicz(key)
          context.become(czekamNaWynik(sender()))
    }

    def czekamNaWynik(przelozony): Receive = {
        case Wynik(n, fib_n) =>
          cache = cache + (n, fib_n)
          przelozony ! Wynik(n, fib_n)
          context.become(receive)
    }
}

class Pracownik(k: Int) extends Actor with ActorLogging {
    def receive: Receive = {
      case Oblicz(key) =>
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

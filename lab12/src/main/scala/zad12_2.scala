package zad12_2
import akka.actor.{ActorSystem, Actor, ActorLogging, ActorRef, Props}

case class Oblicz(liczba: Int)
case class Wynik(n: Int, fin_n: BigInt)

class Boss extends Actor with ActorLogging {
    def receive: Receive = {
      case Oblicz(indeks) =>
        val nadzorca = context.actorOf(Props[Nadzorca]())
        nadzorca ! Oblicz(indeks)
        context.become(receive2(nadzorca))
    }

    def receive2(nadzorca: ActorRef): Receive = {
      case Wynik(n, liczba) =>
        log.info(s"Wynik: $liczba")
      case Oblicz(indeks) =>
        nadzorca ! Oblicz(indeks)
    }
}

class Nadzorca(cache: Map[Int, BigInt] = Map(1 -> 1, 2 -> 1)) extends Actor with ActorLogging {
    def receive: Receive = {
      case Oblicz(key) =>
        if cache.isDefinedAt(key) then
          sender() ! Wynik(key, cache.get(key).get)
        else
          context.actorOf(Props[Pracownik](key)) ! Oblicz(key)
          context.become(czekamNaWynik(sender(), cache))
    }

    def czekamNaWynik(przelozony: ActorRef, cache: Map[Int, BigInt]): Receive = {
        case Wynik(n, fib_n) =>
          val newCache = cache + (n -> fib_n)
          przelozony ! Wynik(n, fib_n)
          context.become(receive)
        case Oblicz(key) =>
        if cache.isDefinedAt(key) then
          sender() ! Wynik(key, cache.get(key).get)
        else
          context.actorOf(Props[Pracownik](key)) ! Oblicz(key)
          context.become(czekamNaWynik(sender(), cache))
    }
    }
}

class Pracownik(k: Int) extends Actor with ActorLogging {
    def receive: Receive = {
      case Oblicz(key) =>
        val przelozony = sender()
        context.actorOf(Props[Pracownik]()) ! Oblicz(k - 1)
        context.actorOf(Props[Pracownik]()) ! Oblicz(k - 2)
        context.become(czekamNaWynik(przelozony))
    }

    def czekamNaWynik(przelozony: ActorRef): Receive = {
       case Wynik(n, liczba) =>
        context.become(półWyniku(przelozony, liczba))
    }

    def półWyniku(przelozony: ActorRef, półWyniku: Int): Receive = {
      case Wynik(n, liczba) =>
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

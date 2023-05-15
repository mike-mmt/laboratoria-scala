package wyk_11

import akka.actor.Actor
import akka.actor.ActorLogging
import akka.actor.ActorSystem
import akka.actor.Props
import akka.actor.ActorRef

case class Primes(limit: Int)
case class Kandydat(n: Int)

class Boss extends Actor with ActorLogging {
    def receive: Receive = {
        case Primes(limit) =>
            log.info(s"Searching for prime numbers in [2...${limit}]")
            val worker = context.actorOf(Props[Worker](), "worker0")
            val wynik = for {
                kandydat <- (2 to limit).toList
            } worker ! Kandydat(kandydat) // : Unit
            // worker ! 997
    }
}

class Worker extends Actor with ActorLogging {
    def receive: Receive = {
        case Kandydat(skarb) =>
            log.info(s"Received: $skarb")
            context.become(withMyPrecious(skarb))
    }

    def withMyPrecious(skarb: Int): Receive = {
        case Kandydat(liczba) =>
            if liczba % skarb != 0 then {
                val potomek = context.actorOf(Props[Worker](),  s"worker$skarb")
                potomek ! Kandydat(liczba)
                context.become(aqq(skarb, potomek))
            }
    }

    def aqq(skarb: Int, syn: ActorRef): Receive = {
        case Kandydat(liczba) =>
            if liczba % skarb != 0 then {
                syn ! Kandydat(liczba)
            }
    }
}

@main def sito: Unit = {
    val system = ActorSystem("Eratostenes")
    val boss = system.actorOf(Props[Boss](), "boss")

    boss ! Primes(50)
}

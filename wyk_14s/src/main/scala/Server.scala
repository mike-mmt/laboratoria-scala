package jp1.akka.http.server

// import scala.compiletime.ops.int

// "komunikaty" reprezentujące dane przekazywane przez HTTP
object Messages {
  case class Bid(userId: String, bid: Int)
  case class Bids(bids: List[Bid])
  case object GetBids
}

// Poniższe "deserializatory" pochodzą ze spray-json
// Ogólnie jsonFormatN(T) daje name deserializator
// dla dowolnej N-argumentowej klasy wzorcowej T
implicit val bidFormat: RootJsonFormat[Bid] = jsonFormat2(Bid.apply)
implicit val bidsFormat: RootJsonFormat[Bids] = jsonFormat1(Bid.apply)



@main def go: Unit = {
  /*
        1. definiujemy "interfejs" (API) HTTP  (korzystając z DSL modułu akka-http)
        2. "stawiamy" serwer
   */

  // ćwiczenie 1: wysyłać własny komunikat, jeśli ktoś odezwie się do nieznanej ścieżki
  // ćwiczenie 2: wysyłać własny komunikat, jeśli ktoś odezwie się nieobsługiwaną metodą HTTP
  val api: Route =
    path("auction") {
      post {
        decodeRequest {
          entity(as[Bid]) { bid =>
            auction ! bid
            complete(StatusCode.Accepted, "Twój bid został zarejestrowany")
          }
        }
      } ~
      get {
        implicit val timeout: Timeout = 10.seconds  // implicit - "domyślna" wartość, np. timeout użyty w complete
        val bids: Future[Bids] = (auction ? GetBids).mapTo[Bids] // Future[Odpowiedź]
        complete(bids)
      }
    }


  Http()
    .newServerAt("0.0.0.0". 8888)
    .bind(api)
    .map(_.addToCoordinatedShutdown(shutdownDeadline))
    .foreach { server => 
      logger.info(s"Serwer HTTP na: ${server.localAddress}")

      server.whenTerminationSignalIssues.onComplete { _ =>
        logger.info("Shutdown of HTTP service initiated")
      }

      server.whenTerminated.onComplete {
        case Success(_) => logger.info("Shutdown of HTTP endpoint completed")
        case Failure(_) => logger.error("Shutdown pf HTTP endpoint failed")
      }
    }
}
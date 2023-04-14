// sbt new wpug/scala3-sbt.g8

def pomocnicza(arg: List[Int], wyn: List[(Int,Int)]): List[(Int,Int)] = {
    arg match {
      case Nil => wyn
      case x :: xs => {
        val (takieSame, inne) = xs.partition(_ == x)
        pomocnicza(inne, (x, takieSame.length + 1) :: wyn)
      }
    }
  }
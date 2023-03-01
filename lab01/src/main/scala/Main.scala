@main def mainProg: Unit = {
  println("Hello World!")
}
// @ - informacja dla kompilatora (nie jest częścią języka)
// Unit - primitywny typ tego co produkuje funkcja, jest głównie związany z efektami ubocznymi obliczenia tak jak drukowanie na ekranie
// sbt:lab01> run
// sbt:lab01> reStart
// reStop
// reStatus

@main def aqqk123: Unit = {
  var x = 1
  while (x < 10) {
    print("x") //println("x")
    x = x + 1
  }
  println("x")
  println("aqqk123! $x")
}

@main def main_2(n: Int): Unit = {        // def nazwa_funkcji(argument: typ, argument: typ, ...): typZwracanejWartości = { ... }
  // var x = 1
  val x: Int = 1
  println(s"Commandline argument: $n, ${n*(x+3)}") // s przed "" sprawia że można używać wartości - $n, $x itd.
}

@main def main_4(n: Int): Unit = {
  val rand = scala.util.Random()
  val liczba = rand.nextInt(100)
  if (liczba > n) {
    println("Wygrana")
  } else {
    println("Przegrana")
  }
  println(s"Wylosowana liczba to: $liczba")
}
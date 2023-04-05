package lab07

def usuń[A](lista: List[A], k: Int): List[A] = {
  lista.zipWithIndex.filter( (value,index) => index != k-1 ).map( (value,index) => value )
  // lista.zipWithIndex.filter( _._2 != k-1 ).map( _._1 )
}

@main def zad_01: Unit = {
  val wynik = usuń(List(9,8,7,6,5), 3)
  println(wynik)
}

package lab07

def indeksy[A](lista: List[A], el: A): Set[Int] = {
  lista.zipWithIndex.filter((value, index) => value == el).map((value,index) => index).toSet
  // lista.zipWithIndex.filter(_._1 == el).map(_._2).toSet
}

@main def zad_02: Unit = {
  println(indeksy(List(8,8,5,8,5,5,8,5), 5))
}

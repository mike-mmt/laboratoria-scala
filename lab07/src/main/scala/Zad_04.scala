package lab07

def przestaw[A](l: List[A]): List[A] = {
  def pomocnicza(lista: List[A]): List[A] = lista match {
    case head :: Nil => lista
    case head :: tail => tail.head :: head :: pomocnicza(tail.tail)
    case Nil => Nil
  }
  pomocnicza(l)
}

@main def zad_04: Unit = {
  val lista = List(1, 2, 3, 4, 5)
  println(przestaw(lista))
  println(przestaw(List(1)))
  println(przestaw(List()))
}

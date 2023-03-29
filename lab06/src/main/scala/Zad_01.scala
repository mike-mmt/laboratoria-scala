package lab06

def subseq[A](list: List[A], begIdx: Int, endIdx: Int): List[A] = {
  // Nil
  list.drop(begIdx).take(endIdx-begIdx+1) 
}

@main def zadanie_01: Unit = {
  println("Testujemy zadanie 1")
  val l = List(1,2,3,4,5,6,7,8)
  println(subseq(l, 2, 6))
}

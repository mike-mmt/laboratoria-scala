package lab06

def freqMax[A](list: List[A]): (Set[A],Int) = {
  (Set(), 0)
  val groupedbyLength = list.groupBy(x => x).groupBy((k,v) => v.length)
  val maxAmountOfNums = if (groupedbyLength.isEmpty) { 0 } else { groupedbyLength.keys.max }

  if ( maxAmountOfNums == 0 ) {
    (Set(), 0)
  } else {
    (groupedbyLength.get(maxAmountOfNums).get.keySet, maxAmountOfNums)
  }
}

def freqMaxAlternative[A](list: List[A]): (Set[A],Int) = list match {
  case Nil => (Set(), 0)
  case _ =>
  val groupedbyLength = list.groupBy(x => x).groupBy((k,v) => v.length)
  val maxAmountOfNums = groupedbyLength.keys.max
  (groupedbyLength.get(maxAmountOfNums).get.keySet, maxAmountOfNums)
}


@main def zadanie_02: Unit = {
  println("Testujemy zadanie 2")
  val l = List(1,1,2,4,4,3,4,1,3)
  println(freqMax(l))
  println("alternatywna funkcja:")
  println(freqMaxAlternative(l))
}

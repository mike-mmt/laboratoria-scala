package lab06

def difficult[A](list: List[A])(len: Int, shift: Int = 1): List[List[A]] = {
    def length(list: List[A]): Int = {
      @annotation.tailrec
      def helper(l: List[A], acc: Int = 0): Int = l match {
        case Nil => acc
        case _ :: reszta => helper(reszta, acc + 1)
      }
      helper(list)
    }
    def take(list: List[A], n: Int): List[A] = {
      @annotation.tailrec
      def helper(l: List[A], licznik: Int = 0, acc: List[A] = Nil): List[A] = l match {
        case _ if (licznik == n) => acc.reverse
        case głowa :: ogon =>
          helper(ogon, licznik + 1, głowa :: acc)
        case _ => acc.reverse
      }
      if n <= 0 then Nil
      else helper(list)
    }
    def drop(list: List[A], n: Int): List[A] = {
      @annotation.tailrec
      def helper(l: List[A], licznik: Int = 0): List[A] = l match {
        case _ if (licznik == n) => l
        case _ :: ogon =>
          helper(ogon, licznik + 1)
        case _ => Nil
      }
      if n <= 0 then list
      else helper(list)
    }
    def difficultHelper(list: List[A], len: Int, shift: Int, acc: List[List[A]] = Nil): List[List[A]] = {
      if (length(list) < len) acc.reverse
      else if (length(list) == 0) acc.reverse
      else difficultHelper(drop(list, shift), len, shift, take(list, len) :: acc)
    }
    difficultHelper(list, len, shift)
}

@main def zadanie_03: Unit = {
  println("Testujemy zadanie 3")
  val ( list, len, shift ) = ( List(1,2,3,4,5), 3, 1 )
  println(difficult(list)(len, shift)) // == List(List(1,2,3), List(2,3,4), List(3,4,5)) // => true
  println(difficult(list)(len, shift) == List(List(1,2,3), List(2,3,4), List(3,4,5))) // => true)

  val ( list2, len2, shift2 ) = ( List(1,2,3,4,5), 2, 2 )
  println(difficult(list2)(len2, shift2)) // == List(List(1,2), List(3,4), List(5)
  println(difficult(list2)(len2, shift2) == List(List(1,2), List(3,4), List(5)))   
}

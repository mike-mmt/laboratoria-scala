package lab09

sealed trait MyList[+A]
case object Empty extends MyList[Nothing]
case class Cons[+A](head: A, tail: MyList[A]) extends MyList[A]

object MyList {

  def head[A](list: MyList[A]): A = list match {
    case Cons(h, tl) => h
    case _ => throw IllegalArgumentException("Head of an empty MyList")
  }

  // wynik: MyList-a zawierająca wszystkie elementy poza pierwszym
  def tail[A](list: MyList[A]): MyList[A] = list match {
    case Cons(head, tail) => tail
    case _ => throw IllegalArgumentException("Tail of empty MyList")
  }

  // wynik: długość MyList-y będącej argumentem
  def length[A](list: MyList[A]): Int = {
    @annotation.tailrec
    def helper(list: MyList[A], acc: Int = 0): Int = list match {
      case Cons(head, tail) => helper(tail, acc + 1)
      case _ => acc
    }
    list match 
      case Cons(head, tail) => helper(list)
      case _ => 0     
  }

  // wynik: MyList-a zawierająca wszystkie elementy poz n początkowymi
  def drop[A](list: MyList[A], n: Int): MyList[A] = {
    @annotation.tailrec
    def helper(list: MyList[A], n: Int): MyList[A] = list match {
      case Cons(head, tail) if n > 0 => helper(tail, n - 1)
      case _ => list
    }
    list match 
      case Cons(head, tail) => helper(list, n)
      case _ => list
  }

  // wynik: „odwrócony” argument
  def reverse[A](list: MyList[A]): MyList[A] = {
    @annotation.tailrec
    def helper(list: MyList[A], acc: MyList[A] = Empty): MyList[A] = list match {
      case Cons(head, tail) => helper(tail, Cons(head, acc))
      case _ => acc
    }
    list match 
      case Cons(head, tail) => helper(list)
      case Empty => list
  }

  // wynik: argument po odrzuceniu początkowych elementów spełniających p
  def dropWhile[A](l: MyList[A])(p: A => Boolean): MyList[A] = {
    @annotation.tailrec
    def helper(list: MyList[A])(p: A => Boolean): MyList[A] = list match {
      case Cons(head, tail) if p(head) => helper(tail)(p)
      case _ => list 
    }
    l match 
      case Cons(head, tail) => helper(l)(p)
      case _ => l
  }

  // wynik: połączone MyList-y list1 oraz list2
  def append[A](list1: MyList[A], list2: MyList[A]): MyList[A] = {
    @annotation.tailrec
    def helper(list1: MyList[A], list2: MyList[A], acc: MyList[A] = Empty): MyList[A] = (list1, list2) match {
      case (Empty, Empty) => reverse(acc)
      case (Empty, Cons(head2,tail2)) => helper(Empty, tail2, Cons(head2, acc))
      case (Cons(head1, tail1), Empty) => helper(tail1, Empty, Cons(head1, acc))
      case (Cons(head1, tail1),Cons(head2, tail2)) => helper(tail1, list2, Cons(head1, acc))
    }
    helper(list1, list2)
  }

  // wynik: MyList-a składająca się ze wszystkich alementów argumentu, poza ostatnim
  def allButLast[A](list: MyList[A]): MyList[A] = {
    @annotation.tailrec
    def helper(list: MyList[A], acc: MyList[A] = Empty): MyList[A] = list match {
      case Cons(head, Cons(headLast, Empty)) => reverse(Cons(head, acc))
      case Cons(head, Empty) => Empty
      case Cons(head, tail) => helper(tail, Cons(head, acc))
      case _ => throw IllegalArgumentException("Błąd!?")
    }
    list match
      case Cons(head, tail) => helper(list)
      case _ => throw IllegalArgumentException("allButLast of empty MyList")
  }

  def map[A,B](list: MyList[A])(f: A => B): MyList[B] = {
    @annotation.tailrec
    def helper(list: MyList[A], acc: MyList[B] = Empty)(f: A => B): MyList[B] = list match {
      case Empty => reverse(acc)
      case Cons(head, tail) => helper(tail, Cons(f(head), acc))(f)
    }
    helper(list)(f)
  }

  @annotation.tailrec
  def foldLeft[A,B](list: MyList[A])(akum: B)(f: (B,A) => B): MyList[B] = {
    ???
  }

}

@main def listy: Unit = {
  val l1 = Cons(1, Cons(2, Cons(3, Empty)))
  val l2 = Cons(4, Cons(5, Cons(6, Empty)))
  val l3 = Cons(4, Cons(5, Cons(6, Cons(7, Cons(8, Empty)))))

  val res = MyList.head(l1)
  println(s"MyList.head($l1) == $res")
  
  val res2 = MyList.tail(l1)
  println(s"MyList.tail($l1) == $res2")
  
  val res3 = MyList.length(l1)
  println(s"MyList.length($l1) == $res3")
  // println(MyList.head(Empty)) // spowoduje „wyjątek”
  
  val res4 = MyList.drop(l1, 2)
  println(s"MyList.drop($l1, 2) == $res4")

  val res5 = MyList.reverse(l1)
  println(s"MyList.reverse($l1) == $res5")

  val res6 = MyList.dropWhile(l1)(x => x < 3)
  println(s"MyList.dropWhile($l1)(x => x < 3) == $res6")

  val res7 = MyList.append(l1,l2)
  println(s"MyList.append($l1,$l2) == $res7")

  val res8 = MyList.allButLast(l3)
  println(s"MyList.allButLast($l3) == $res8")

  val res9 = MyList.map(l3)(x => s"a${x}a")
  println(s"MyList.map($l3)(f) == $res9")
}

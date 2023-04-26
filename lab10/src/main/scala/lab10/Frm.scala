package lab10

sealed trait Frm
case object False extends Frm
case object True extends Frm
case class Not(f: Frm) extends Frm {
  override def toString(): String = f match {
    case True | False | Not(_) => s"!$f"
    case _ => s"!($f)"
  }
}
case class And(f1: Frm, f2: Frm) extends Frm {
  override def toString(): String = (f1, f2) match {
    case (True | False | Not(_), True | False | Not(_)) => s"$f1 & $f2"
    case (True | False | Not(_), _) => s"$f1 & ($f2)"
    case (_, True | False | Not(_)) => s"($f1) & $f2"
    case (_, _) => s"($f1) & ($f2)"
  }
}
case class Or(f1: Frm,f2: Frm) extends Frm {
  override def toString(): String = (f1, f2) match {
    case (True | False | Not(_), True | False | Not(_)) => s"$f1 | $f2"
    case (True | False | Not(_), _) => s"$f1 | ($f2)"
    case (_, True | False | Not(_)) => s"($f1) | $f2"
    case (_, _) => s"($f1) | ($f2)"
  }
}
case class Imp(f1: Frm, f2: Frm) extends Frm {
  override def toString(): String = (f1, f2) match {
    case (True | False | Not(_), True | False | Not(_)) => s"$f1 -> $f2"
    case (True | False | Not(_), _) => s"$f1 -> ($f2)"
    case (_, True | False | Not(_)) => s"($f1) -> $f2"
    case (_, _) => s"($f1) -> ($f2)"
  }
}

val frm = Imp(Or(True, False), Not(And(True, False)))

// UWAGA: W rozwiązaniach poniższych zadań (tam gdzie to możliwe)
//        wykorzystaj rekurencję ogonową.

@main def zad_1: Unit = {
  // Ćwiczenie 1: zaimplementuj toString tak, aby „minimalizowało”
  //              liczbę nawiasów
  println(frm)
  // Powinno wyprodukować coś „w stylu”:
  // (True | False) -> !(True & False)
}

@main def zad_2: Unit = {
  // Ćwiczenie 2: zaimplementuj funkcję wyliczającą wartość logiczną
  //              formuły.
  def eval(f: Frm): Boolean =  {
    // @annotation.tailrec
    def helper(f: Frm): Boolean = f match {
      case True => true
      case False => false
      case Not(f) => !helper(f)
      case And(f1, f2) => helper(f1) && helper(f2)
      case Or(f1, f2) => /*helper(f1) || helper(f2)*/ helper( Not( And( Not(f1), Not(f2) ) ) )
      case Imp(f1, f2) => /*if (helper(f1) == true && helper(f2) == false) false else true*/ helper(Or(Not(f1), f2))
    }
    helper(f)
  }

  def evalTR(f: Frm): Boolean = {
    // def constHelper(f: Frm): Boolean = f match {
    //   case True => true
    //   case False => false
    // }
    def oneArgHelper(f: Frm): Boolean = f match {
      // case True | False => !constHelper()
      case Not(True) => false // constHelper(False)
      case Not(False) => true // constHelper(True)
      case Not(Not(f1)) => evalTR(f1)
      case Not(And(f1, f2)) => 
      case Not(Or(f1, f2)) =>
      case Not(Imp(f1, f2)) =>
    }
    def twoArgsHelper(f1: Frm, f2: Frm): Boolean = f match {

    }
  }
  println(eval(False)) // == false
  println(eval(frm)) // == true
}

@main def zad_3: Unit = {
  // Ćwiczenie 3: napisz funkcję wyliczającą dla danej formuły f
  //              zbiór jej wszystkich „podformuł”

  // def closure(f: Frm): Set[Frm] = {
  //   def helper(f: Frm, acc: Set[Frm]) = f match {
  //     case True => True :: acc
  //     case False => False :: acc
  //     case Not(f) => helper(f, f :: acc)
  //     // case And(f1, f2) => helper()
  //   }
  // }

  // np. dla formuły frm powyżej wynikiem powinien być zbiór:
  //
  // { True, False, True | False, True & False, !(True & False), frm }
  //
  // Jak widać, closure(frm) zawiera również formułę będącą argumentem.
}

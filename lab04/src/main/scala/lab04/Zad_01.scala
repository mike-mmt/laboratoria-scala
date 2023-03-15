package lab04


def ciąg(n: Int): Int = {
  // C(0) == 2
  // C(1) == 1
  // C(n) == C(n - 1) + C(n - 2) dla n > 1
  @annotation.tailrec
  def pomocnicza(i: Int, a: Int, b: Int): Int = {
    // a: wartosc 2 do tylu,  b: wartosc 1 do tylu
    if (i==n) a
    else pomocnicza(i+1, b, a+b)
  }
  if (n == 0) 2
  else if (n == 1) 1
  else pomocnicza(0, 2, 1)
}

// @annotation.tailrec
// def ciag2(n: Int, ak1: Int = 2, ak2: Int = 1): Int = { // sposób 2
//   if (n==0) ak1
//   else if (n == 1) ak2
//   else ciag(n-1, ak1 + ak2, ak2 + (n-1))
// }

@main def zadanie_01: Unit = {
  // Program powinien umożliwić „sprawdzenie” działania
  // funkcji „ciąg”.
  for { el <- 0 until 10 } print(s"${ciąg(el)} ")
}

package lab03

@annotation.tailrec
def ciągGeometryczny(n: Int, iloraz: Double, początek: Double): Double = {
  if (n <= 1) początek
  else ciągGeometryczny(n - 1, iloraz, początek * iloraz)
}

@main def zad_01(n: Int, iloraz: Double, początek: Double): Unit = {
  require(n >= 0)
  // Zdefiniuj funkcję ciągGeometryczny tak, aby zwracała n-ty wyraz
  // ciągu geometrycznego dla zadanego ilorazu i wyrazu początkowego.
  val wynik = ciągGeometryczny(n, iloraz, początek)
  println(s"a_$n dla ciągu a_n=$początek*($iloraz^n) wynosi:$wynik")
}

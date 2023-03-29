// @main def mainProg: Unit = {

//   val lista = List(1,2,3)
//   // lista.foldLeft((0,""))((m,n) => (m._1 + n, m._2 + ("@" * m._2)))

//   val wynik = lista.foldLeft((0,""))((akum,liczba) => akum match {
//     case (suma, napis) =>
//       (suma + liczba, napis + ("@" * liczba))
//   })

//   println(wynik)

//   val wynik2 = lista.foldLeft((0,""))((akum, liczba) => (akum._1 + liczba, "aqq"))
//   //                          ^^^^^^                    ^^^^^^^^^^^^^^^^^^^^^^^^^
//   //                      początkowa                  wartość tego wyrażenia jest przekazywania do akum
//   //                         wartość
// //                             akum

//   println(wynik2)

//   val trójka = ("ala","tomek","rysio")

//   trójka match { case (a, b, c) => a }
//   // "ala"
//   trójka match { case (a, b, c) => (a,c) }
//   // ("ala","rysio")
//   trójka match { case (a, _, c) => (a,c) }
//   // ("ala","rysio")
//   ///////////////////////////////////////////////////////////////////
//   ///////////////////////////////////////////////////////////////////
//   ///////////////////////////////////////////////////////////////////
//   //////////////////////  kolekcje  /////////////////////////////////
//   ///////////////////////////////////////////////////////////////////
//   kolekcja: CC[A] // ELementy są typu A

//   // Odwzorowania: Map[A,B]
//   val lista = List(1,2,3,2)
//   val wynik = lista.groupBy(n => n) // : Map[???,List[Int]]
//   //val wynik: Map[Int, List[Int]] = HashMap(1 -> List(1), 2 -> List(2, 2), 3 -> List(3))

//   val wynik = lista.groupBy(n => n % 2 == 0)
//   // val wynik: Map[Boolean, List[Int]] = HashMap(false -> List(1, 3), true -> List(2, 2))
//   lista.::(99)
//   // List(99, 1, 2 ,3 ,2)

//    99 :: lista

//    lista.filter( n => n % 2 == 1)
//    // List(1, 3)

//    Set(1,2,3).filter( n => n % 2 == 1)
//    // Set(1, 3)

//    Map(1 -> "ola",2 -> "ala", (3,"tomek")).filter( para => para._1 == 1)
//    // Map(1 -> ola)

//    Set(1,2,3).reverse
//    //         !!!

//    Seq(1,2,3).reverse
//    // Seq[Int] = List(3, 2 ,1)

//    val ciąg: Seq[Int] = Vector(1,2,3).reverse
//     // ciąg: Seq[Int] = Vector(3,2,1)
//    ciąg(0)
//    // Int = 3

//    ciąg(2)
//    // Int = 1

//    ciąg(ciąg.length - 1)
//    // Int = 1

//    Set(1,2,3)(1)
//    // true       // sprawdza czy 1 znajduje się w secie

//    Set(1,2,3)(7)
//    // false       

//    lista.reduce((m,n) => m + n)
//    // 8

//    Set(1,2,3).reduce(_ + _)
//    // 6 
// }

// def aqq[A](seq: Seq[A]): Unit = seq match {
//   case List(jedynyElement) => println(s"Ciąg z element $jedynyElement ")
//   case głowa :: _ => println(s"Ciąg z pierwszym elementem $głowa")
//   case List(głowa, _*) => println()(s"Ciąg z pierwszym elementem $głowa")
//   case Vector(1, _, 2, _*) => println(s"1, x, 2, x , x, ...") 
//   case _ => println("coś innego")
// }
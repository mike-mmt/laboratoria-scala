object funkcjeRekurencyjne extends App {
    
    val przykladowaLista = List(1, 2, 3, 4, "test")
    val listaLiczb: List[Int] = List(1,2,3,4,5)

    def lengthList[A](inputList: List[A], length: Int = 0): Int = {
        inputList match {
            case head :: tail => lengthList(tail, length + 1)
            case _ => length
        }
    }

    def sliceList[A](inputList: List[A], startIndex: Int, endIndex: Int): List[A] = {
        take(drop(inputList, startIndex), endIndex - startIndex)
    }

    def initString(inputString: String): String = {
        reverseString(tailString(reverseString(inputString)))
    }

    def initList[A](inputList: List[A]): List[A] = {
        reverse(tailList(reverse(inputList)))
    }

    def lastList[A](inputList: List[A]): A = inputList(lengthList(inputList)-1)
    
    def lastString(inputString: String): Char = inputString(inputString.length-1)

    def reverse[A](inputList: List[A], reversedList: List[A] = List()): List[A] = {
        inputList match {
            case head :: tail => reverse(tail, head :: reversedList)
            case _ => reversedList
        }
    }

    def reverseString(inputString: String, reversedString: String = ""): String = {
        inputString.length == 0 match {
            case true => reversedString
            case _ => reverseString(tailString(inputString), s"${headString(inputString)}${reversedString}" )
        }
    } 

    def head[A](inputList: List[A]): A = {
        inputList match {
            case head :: tail => head
            case _ => throw new NoSuchElementException("head of empty list")
        }
    }// uzywac gdy patternmatching niewygodny

    def headString(inputString: String): Char = inputString(0)

    def tailList[A](inputList: List[A], outputList: List[A] = List()): List[A] = {
        inputList match {
            case head :: tail => tail
            case _ => outputList
        }
    }

    def tailString(inputString: String, outputString: String = "", currentIndex: Int = 1): String = {
        currentIndex == inputString.length match {
            case true => outputString
            case _ => tailString(inputString, outputString + inputString(currentIndex), currentIndex + 1)
        }        
    }

    def drop[A](list: List[A], length: Int): List[A] = {
        lengthList(list) > length match {
            case true => {
                length match {
                    case 0 => list
                    case _ => drop(tailList(list), length -1)
                }
            }
            case _ => List()
        }
    }

    def take[A](list: List[A], length: Int, outputList: List[A] = List()): List[A] = {
        lengthList(list) > length match {
            case true => {
                length match {
                    case 0 => outputList
                    case _ => take(tailList(list), length -1, outputList :+ head(list))
                }
            }
            case _ => list
        }        
    }

    def takeRight[A](inputList: List[A], length: Int, outputList: List[A] = List() ): List[A] = {
        lengthList(inputList )> length match {
            case true => {
                length match {
                    case 0 => outputList
                    case _ => takeRight((initList(inputList)), length -1, head(reverse(inputList)) :: outputList )
                }
            }
            case _ => outputList
        }           
    }

    def dropRight[A](inputList: List[A], length: Int, outputList: List[A] = List()): List[A] = {
        lengthList(inputList) > length match {
            case true => {
                length match {
                    case 0 => inputList
                    case _ => drop(reverse(tailList(reverse(inputList))), length -1)
                }
            }
            case _ => List()
        }        
    }

    def map[A, B](list: List[A], f: A => B, outputList: List[B] = List()): List[B] = list match {
        case head :: tail => map(tail, f, outputList :+ f(head) )
        case _ => outputList
    }

    def filter[A](list: List[A], f: A => Boolean, outputList: List[A] = List()): List[A] = {
        list match {
            case head :: tail => filter(tailList(list), f, if f(head) == true then outputList :+ head else outputList)
            case _ => outputList
        }
    }

    def foldLeft[A, B](initialValue: B)(list: List[A], f: (B, A) => B): B = { // UŻYWAĆ TEGO RÓWNIEŻ ZAMIAST REDUCA

        def reduceFunction(inputList: List[A], accumulator: B): B = inputList match {
            case head :: tail => reduceFunction(tail, f(accumulator, head))
            case _ => accumulator
        }

        reduceFunction(list, initialValue)
        }

    def split(inputString: String, delimiter: String, outputList: List[String] = List(), currentWord: String = ""): List[String] = {
        inputString.length > 0 match {
            case true => {
                headString(inputString).toString == delimiter match {
                    case true => split(tailString(inputString), delimiter, outputList :+ currentWord, "")
                    case _ => split(tailString(inputString), delimiter, outputList, currentWord :+ inputString.head)   
                }
            } 
            case _ => outputList :+ currentWord
        }
    }

    def collect[A, B](inputList: List[A], partialFunction: A => B, outputList: List[B] = List()): List[B] = {
        inputList match {
            case head :: tail =>{
                try {
                    collect(tail, partialFunction, outputList :+ partialFunction(head))
                } catch {
                    case _: MatchError => collect(tail, partialFunction, outputList)
                }
            }
            case _ => outputList
        }
    }

    def exists[A](inputList: List[A], predicateFunction: A => Boolean): Boolean = {
        inputList match {
            case head :: tail => {
                predicateFunction(head) match {
                    case true => true
                    case _ => exists(tailList(inputList), predicateFunction)
                }
            }
            case _ => false
        }
    }

    def forAll[A](inputList: List[A], predicateFunction: A => Boolean): Boolean = {
        inputList match {
            case head :: tail => {
                predicateFunction(head) match {
                    case true => forAll(tailList(inputList), predicateFunction)
                    case _ => false
                }
            }
            case _ => true
        }
    }

    def count[A](inputList: List[A], predicateFunction: A => Boolean, occurences: Int = 0): Int = {
        inputList match {
            case head :: tail => {
                predicateFunction(head) match {
                    case true => count(tailList(inputList), predicateFunction, occurences + 1)
                    case _ => count(tailList(inputList), predicateFunction, occurences)
                }
            }
            case _ => occurences
        }
    }

    def product(inputList:List[Double]): Double = {  // inne typy liczb przekonwertować na double i git
        foldLeft(1.0)(inputList, ((acc, elem) => acc * elem))
    }

    def sum(inputList:List[Double]): Double = {  // inne typy liczb przekonwertować na double i git
        foldLeft(1.0)(inputList, ((acc, elem) => acc + elem))
    }

    def zipWithIndex[A](inputList: List[A], outputList: List[(A, Int)] = List(), currentIndex: Int = 0): List[(A, Int)] = {
        inputList match {
            case head :: tail => zipWithIndex(tail, outputList :+ (head, currentIndex), currentIndex + 1)
            case _ => outputList
        }
    }

    def delete[A](inputList: List[A], k: Int): Any = {
        k > lengthList(inputList) - 1 match {
            case true => inputList
            case _ => {
                map(filter(zipWithIndex(inputList), (tuple => {
                        tuple match {
                            case (element, index) => index != k
                        }
                    })), (tuple => {
                        tuple match {
                            case (element, index) => element
                        }
                    }))                
            }
        }
    }

    

    println(reverse(przykladowaLista))
    println(List(przykladowaLista))
    println(tailList(List()))
    println(tailList(List(1)))
    // println(head(List())) error działa poprawnie
    println(head(przykladowaLista))

    println("\n\nPOROWNYWANIE DROP I TAKE\n\n")
    println(drop(przykladowaLista, 2))
    println(przykladowaLista.drop(2))
    println(drop(List(), 2))
    println(przykladowaLista.take(3))
    println(take(przykladowaLista, 3) )
    println(take(List(), 2))

    println("\n\nMAPMAPAMPMAPMAPMAPMPAA\n\n")
    println(map(listaLiczb, x => x + 2))
    println(listaLiczb.map(x=> x * 2))

    println("\n\nFOLDLEFTFOLDLEFTFOLDLEFT\n\n")
    println(foldLeft(0)(listaLiczb, (acc, elem) => acc + elem))
    println(listaLiczb.foldLeft(0)((acc , elem) => acc + elem))

    println("\n\nHEADSTRINGTAILSTRINGHEAD STRING TAIL STRING\n\n")
    println(tailString("abc"))
    
    println("\n\nSPLITSPLITPLSITPLSIT\n\n")
    println(split("ab-ab-ab","-"))

    println("\n\nLASTLASTLASTLASTLAST LAST REVERSE STRING INIT NITINITNINIT\n\n")
    println(lastString("abc"))
    println(lastList(List(1,2,3)))
    println(initString("abc"))
    println(reverseString("abc"))
    println(reverseString(""))
    println(initList(List(1,2,3)))
    println(filter(List(1,2,3,4,5), n => n%2==0))
    
    println("\n\n SLICE SLICE SLICE \n\n")
    println(sliceList(List(1,2,3,4,5,6,7,8,9,10), 2, 3))
    println(drop((List(1,2)),0))
    println(takeRight(List(1,2,3,4),3))
    println(List(1,2,3,4).takeRight(3))
    println(dropRight(List(1,2,3,4), 2))

    println("\n\n COLLECT COLLECT COLLECT\n\n")

    val funkcjaCzęściowa: PartialFunction[Int, Int] = n =>{
        n match {
            case 1 => 100
            case 5 => 500
            }
        }
    val wynik = List(1,2,3,4,5).collect(funkcjaCzęściowa)
    val wynik2 = collect(List(1,2,3,4,5), funkcjaCzęściowa)
    println(wynik)
    println(wynik2)
    println()

    println("\n\n TESTOWANIE TESTOWANIE\n\n")
    val test1 = exists(List(1,2,3,4,5),(n => n % 2 == 0)) // czy istnieje
    val test2 = forAll(List(1,2,3,4,5),(n => n % 2 == 0)) // czy wszystkie takie są (nie mylić z foreachem)
    val test3 = count(List(1,2,3,4,5),(n => n % 2 == 0))
    println(test1)
    println(test2)
    println(test3)

    println("\n\n PRODUCT SUM PRODUCT SUM \n\n")
    println(product(List(1.0,2.0,3.0,4.0,5.0)))
    println(sum(List(1.0,2.0,3.0,4.0,5.0)))

    println("\n\nLength LENGTH LENGTH\n\n")
    println(lengthList(List(1,2,3,4)))
    
    println("\n\n ZIPWITHINDEX ZIPWITHINDEX DELETE DELETE")
    println(zipWithIndex(List("a", "b", "c")))
    println(delete(List(1,2,3,4), 2))
}
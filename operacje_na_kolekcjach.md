# Poznane operacje na kolekcjach

Na wykładach 4 i 5 mówiliśmy o „kolekcjach” `CC[A]`. Poniżej, nieco uzupełniona, lista poznanych operacji na nich:

- rozmiar kolekcji

```scala
val list1 = List(1, 2, 3)
val list2 = List.empty[Int]

isEmpty: Boolean
  list1.isEmpty // false
  list2.isEmpty // true

nonEmpty: Boolean
  list1.isEmpty // true
  list2.isEmpty // false

size: Int
  list1.size // 3

knownSize: Int
  // dostępna w niektórych kolekcjach w języku Scala, takich jak Stream lub Iterator. Zwraca znany rozmiar kolekcji w przypadku, gdy jest on znany w czasie kompilacji. W przeciwnym razie, zwraca -1.
```

- testowanie elementów z ewentualnym wyborem

```scala
val list = List(1, 2, 3, 4, 5)

exists (p: A ⇒ Boolean): Boolean
  list.exists(_ > 3) // true, istnieje element większy od 3
  list.exists(_ > 10) // false, nie ma elementu większego od 10

forall (p: A ⇒ Boolean): Boolean
  list.forall(_ > 0) // true, wszystkie elementy są większe od 0
  list.forall(_ > 3) // false, nie wszystkie elementy są większe od 3

count (p: A ⇒ Boolean): Int
  list.count(_ > 3) // 2, dwa elementy są większe od 3
  list.count(_ % 2 == 0) // 2, dwa elementy są liczbami parzystymi

find (p: A ⇒ Boolean): Option[A]
  list.find(_ > 3) // Some(4), pierwszy element większy od 3
  list.find(_ > 10) // None, brak elementu który jest większy od 10
```

- przekształcanie kolekcji

```scala
val numbers = List(1, 2, 3)

map[B] (f: A ⇒ B): CC[B]
  val mappedNumbers = numbers.map(n => List(n, n * 2))
  // mappedNumbers: List[List[Int]] = List(List(1, 2), List(2, 4), List(3, 6))

flatMap[B] (f: A ⇒ IterableOnce[B]): CC[B]
  val flatMappedNumbers = numbers.flatMap(n => List(n, n * 2))
  // flatMappedNumbers: List[Int] = List(1, 2, 2, 4, 3, 6)

collect[B] (pf: PartialFunction[A, B]): CC[B]
  val collectedNumbers = numbers.collect {
    case n if n % 2 == 1 => n * 2
  }
  // collectedNumbers: List[Int] = List(2,6)

collectFirst (pf: PartialFunction[A, B]): Option[B]
  // działa podobnie do collect, ale zwraca tylko jeden element w postaci Some(element) lub None

fill
val apples5 = List.fill(5)("apple")
  // List(apple, apple, apple, apple, apple) 
```

- „zwijanie” i „grupowanie” kolekcji

```scala
foldLeft[B] (z: B)(op: (B, A) ⇒ B): B
  val numbers = List(1, 2, 3, 4, 5)
  val sum = numbers.foldLeft(0)((acc, num) => acc + num)
  // sum = 15

foldRight[B] (z: B)(op: (A, B) ⇒ B): B

groupBy[K](key: A => K): Map[K, CC[A]]
  val odds = numbers.groupBy( x => x % 2)
  // HashMap(0 -> List(2, 4), 1 -> List(1, 3, 5))

groupMap[K, B] (key: A ⇒ K)(f: A ⇒ B): Map[K, CC[B]]
  val groups = list.groupMap(x => x % 3)(x => x * 2)
  // Map(0 -> List(6), 1 -> List(2, 8), 2 -> List(4, 10))
  // najpierw grupuje, potem mapuje

groupMapReduce[K, B] (key: A ⇒ K)(f: A ⇒ B)(op: (B, B) ⇒ B): Map[K, B]
  // najpierw grupuje, potem mapuje, a na koniec redukuje

reduce[B >: A](op: (B, B) => B): B
  val sum = list.reduce((acc, y) => acc + y) // sum = 15

  // reduce z wartością początkową akumulatora
  val sum10 = list.reduce((x, y) => x + y, 10) // sum = 25

```

- dostęp do podkolekcji

```scala
  val numbers = List(1, 2, 3, 4, 5, 6, 7, 8, 9, 10)

filter (p: A ⇒ Boolean): CC[A]
  val evenNumbers = numbers.filter(n => n % 2 == 0)
  //List(2, 4, 6, 8, 10)

filterNot (p: A ⇒ Boolean): CC[A]
  // działa jak filter, ale wybiera wartości które nie spełniają podanego warunku

take (n: Int): CC[A]
  val firstThree = numbers.take(3) // 3 to liczba elementów
  // List(1, 2, 3)

takeWhile (p: A ⇒ Boolean): CC[A]
  val smallerThenFour = numbers.take( n => n < 4 )
  // List(1, 2, 3) zwraca wartość z listy dopóki spełnia ona warunek funkcji podanej jako argument

drop (n: Int): CC[A]
  val withoutFirstThree = numbers.drop(3) // 3 to liczba elementów
  // List(4, 5, 6, 7, 8, 9, 10)

dropWhile (p: A ⇒ Boolean): CC[A]
  val biggerThanFive = numbers.dropWhile( n => n <= 5 )
  // usuwa wartość z listy dopóki spełnia ona warunek funkcji podanej jako argument

slice (from: Int, until: Int): CC[A]
  val fromSecondToFifth = numbers.slice(2, 5)
  // List(3, 4, 5) 
  // zwraca kolekcję podaną od jednego indeksu (włącznie) do drugiego (bez tego drugiego)
```

- kombinacje numeryczne

```scala
sum[B >: A] (implicit math.Numeric[B]): B
// zwraca sumę elementów kolekcji

product[B >: A] (implicit math.Numeric[B]): B
// mnoży przez siebie wszystkie elementy kolekcji
```

- operacje wykorzytujące „uporządkowanie” kolekcji

```scala
min[B >: A] (implicit math.Ordering[B]): A
// zwraca najmniejszy element kolekcji dla typów które mają zdefiniowany porządek

minOption[B >: A] (implicit math.Ordering[B]): Option[A]
// jak min, ale zwraca wynik w postaci opcjonalnej wartości jeśli podana lista jest pusta
// na przykład: None, Some(2)

minBy[B] (f: A ⇒ B) (implicit math.Ordering[B]): A
// zwraca najmniejszą wartość wg podanej funkcji, 
  case class Person(name: String, age: Int)
  val people = List(Person("Alice", 25), Person("Bob", 30), Person("Charlie", 20))
  val youngestPerson = people.minBy(_.age)
  // Person(Charlie, 20)

minByOption[B] (f: A ⇒ B) (implicit math.Ordering[B]): Option[A]
// zwraca najmniejszą wartość wg podanej funkcji, w postacji wartości opcjonalnej

max[B >: A] (implicit math.Ordering[B]): A
maxOption[B >: A] (implicit math.Ordering[B]): Option[A]
maxBy[B] (f: A ⇒ B) (implicit math.Ordering[B]): A
maxByOption[B] (f: A ⇒ B) (implicit math.Ordering[B]): Option[A]
```
- sortowanie kolekcji
```scala
val lista = List(5, 3, 1, 4, 2)
case class Osoba(imie: String, wiek: Int)
val listaOsób = List(Osoba("Anna", 25), Osoba("Bartek", 30), Osoba("Celina", 22))

sorted
  val posortowanaLista1 = lista.sorted
  // List(1, 2, 3, 4, 5)
sortWith
  // ustalenie własnego porządku sortowania za pomocą funkcji porównującej
  val posortowanaLista2 = lista.sortWith((a, b) => a < b)
  // List(1, 2, 3, 4, 5)
  val posortowanaLista = listaOsób.sortWith((a, b) => a.wiek < b.wiek || (a.wiek == b.wiek && a.imie < b.imie)) 
  // Sortowanie po wieku, a w przypadku równej wartości wieku, po imieniu
  // List(Osoba(Celina,22), Osoba(Anna,25), Osoba(Bartek,30))
sortBy
  val posortowanaLista = listaOsób.sortBy(osoba => osoba.wiek) // Sortowanie po wieku
  // List(Osoba(Celina,22), Osoba(Anna,25), Osoba(Bartek,30))

```
- „spinanie” i „rozpinanie” kolekcji

```scala
  val fruit = List("apple", "banana", "cherry")
  val numbers = List(1, 2, 3)
  val strings = List("one", "two", "three")

zipWithIndex: CC[(A, Int)]
  // łączy każdy element kolekcji z jego indeksem, tworząc krotki (element, indeks). Wynik funkcji jest kolekcją takich krotek
  val FriutWithIndex = fruit.zipWithIndex
  // List((apple,0), (banana,1), (cherry,2))

zip[B] (that: Iterable[B]): CC[(A, B)]
  // łączy dwa równoległe strumienie danych, tworząc krotki (element1, element2) z odpowiadającymi sobie elementami z obu strumieni
  val zippedList = numbers.zip(strings)
  // List((1,one), (2,two), (3,three))

zipAll[B] (that: Iterable[B], thisElem: A, thatElem: B): CC[(A, B)]
  // tak jak zip, ale jeśli jedna z kolekcji jest dłuższa niż druga, to brakujące elementy są uzupełniane wartościami domyślnymi. Funkcja zipAll przyjmuje trzy argumenty: drugą kolekcję, wartość domyślną dla elementów pierwszej kolekcji oraz wartość domyślną dla elementów drugiej kolekcji.
  val zippedList = numbers.zip(strings, 0, "none")
  // List((1,one), (2,two), (3,none))

unzip[A1, A2]: (implicit asPair: A ⇒ (A1, A2)): (CC[A1], CC[A2])
  // rozdziela kolekcje 2-elementowych krotek na dwie osobne kolekcje krotek
  val list = List((1, "one"), (2, "two"), (3, "three"))
  val (list1, list2) = list.unzip
  // list1 = List(1, 2, 3), list2 = List("one", "two", "three")

unzip3[A1, A2, A3]: (implicit asTriple: A ⇒ (A1, A2, A3)): (CC[A1], CC[A2], CC[A3])
  // jak unzip ale dla krotek 3-elementowych
```

- konwersje pomiędzy kolekcjami

```scala
toSeq/toList, toSet, toMap, …
```

- dodawanie/łączenie kolekcji

```scala
concat[B >: A] (suffix: IterableOnce[A]): CC[B]
// łączy dwie kolekcje 
  val list1 = List(1, 2, 3)
  val list2 = List(4, 5, 6)
  val list3 = List(7, 8, 9)
  val str = "abc"
  val result1 = list1.concat(list2).concat(list3)
  val result2 = list1.concat(str)
  // result1 =  List(1, 2, 3, 4, 5, 6, 7, 8, 9)
  // result2 = List(1, 2, 3, a, b, c)
++ // to samo co concat

val list = List(1, 2, 3)
+: //dodanie elementu na początek listy
  val preappendedList = 20 +: list
  //  List(20, 1, 2, 3)

:+ //dodanie elementu na koniec listy
  val apenndedList = list :+ 20
  // List(1, 2, 3, 20)
```

- dostęp do „wybranych” elementów

```scala
head, last: A
// zwraca pierwszy element
  val list = List(1, 2, 3)
  val firstElement = list.head
  // firstElemet = 1

headOption, lastOption: Option[A]
// podobnie do head, ale wynik jest w postaci option
```

- dostęp do wybranych „podkolekcji”

```scala
val list = List(1, 2, 3)

init: CC[A]
// zwraca wszystkie elementy kolekcji poza ostatnim
  val withoutLastElement = list.init
  // List(1, 2)

inits: Iterator[CC[A]]
// zwraca kolekcję wszystkich możliwych prefixów wejściowej kolekcji
  val list = List(1, 2, 3)
  val allPrefixes = list.inits.toList
  // List(List(1, 2, 3), List(1, 2), List(1), List())

tail: CC[A]
// zwraca wszystkie elementy poza pierwszym

tails: Iterator[CC[A]]
// zwraca kolekcję wszystkich możliwych sufixów wejściowej kolekcji

takeRight (n: Int): CC[A]
dropRight (n: Int): CC[A]
// jak zwykły take i drop, ale w drugą stronę
```

## Operacje specyficzne dla odwzorowań `Map[K,V]`

- dostęp do kluczy/wartości

```scala
// „Zastosowanie” odwzorowania do klucza „key” (czy wykorzystanie operacji „apply”)
// zapisujemy jako: odwzorowanie(key)
val list = List(1, 2, 3, 4, 5)

apply (key: K): V
// wyciąganie n-tego elementu z kolekcji
  val thirdOne = list.apply(2)
  // zazwyczaj zapiusje się to tak:
  val thirdOne = list(2)

get (key: K): Option[V]
// wyciąganie n-tego elementu z kolekcji w postaci Some(element) lub None

getOrElse (key: K, default: ⇒ V): V
// jak get
  val element3 = list.lift(10).getOrElse("default value")
isDefined (key: K): Boolean       // (alternatywna nazwa `contains`)
// można użyc, aby sprawdzić, czy opcjonalna wartość zawiera faktyczną wartość, zanim próbujemy na niej wykonać jakieś operacje, np.
  if (x.isDefined) {
  // wykonaj operację na x.get()
  }
```

- tworzenie nowych kolekcji na podstawie odwzorowania

```scala
keys: Iterable[K]
keySet: Set[K]
values: Iterable[V]
```

- dodawanie/usuwanie elementów

```scala
+ (kvs: (K, V)*): Map[K, V] // dodaje element do kolekcji
- (key: K, keys: K*): Map[K, V] // usuwa
val list1 = List(1, 2, 3)
val list2 = list1 + 4
println(list1) // List(1, 2, 3)
println(list2) // List(1, 2, 3, 4)
```

### Operacje specyficzne „widoków” odwzorowań – `MapView[A]`

```scala
// Dla dowolnego odwzorowania
val m: Map[K, V] = ???
// możemy stworzyć jego „widok”
val widok: MapView[K, V] = m.view
// na którym możemy wykonywać (nawet wielokrotnie) operacje
widok.filterKeys (p: K ⇒ Boolean): MapView[K, V]
// filterKeys do filtrowania kluczy słownika wg podanej funkcji

widok.mapValues[W] (f: V ⇒ W): MapView[K, W]
// do mapowania wartości słownika wg podanej funkcji

// jak widać w efekcie otrzymujemy ponownie widok, czyli
// użycie metod filterKeys oraz mapValues możemy kontynuować.
// Aby uzyskać odzworowanie wystarczy na koniec użyć metody
// ….toMap
```

## Operacje specyficzne dla zbiorów – `Set[A]`

- należenie, podzbiór

```scala
  contains (elem: A): Boolean
  // sprawdza czy podany element jest w kolekcji, w przypadku obiketu Map sprawdza czy wśród kluczy jest podany element, 

  subsetOf (that: Set[A]): Boolean
  // sprawdza czy podany zbiór jest podzbiorem tego drugiego
    val s1 = Set(4, 12, 2, 31) 
    val s2 = Set(4, 12)
    val result = s2.subsetOf(s1)
    // result = true
```

- operacje na zbiorach

```scala
  intersect (that: Set[A]): Set[A] // (alternatywna nazwa: &)
  // część wspólna
    val list1 = List(1, 2, 3, 4)
    val list2 = List(3, 4, 5, 6)
    val intersection = list1.intersect(list2)
    // intersection: List[Int] = List(3, 4)

  union (that: Set[A]): Set[A]     // (alternatywna nazwa: |)  UWAGA: podobne do `concat` (++)
  // suma

  diff (that: Set[A]): Set[A]      // (alternatywna nazwa: &~)
  // różnica
```
## Kolekcje mutowalne
ArrayBuffer - Jest to zmienna wielkość tablicy, którą można zmieniać poprzez dodawanie, usuwanie i modyfikowanie elementów w miejscu.

ListBuffer - Jest to zmienna wielkość lista, którą można modyfikować poprzez dodawanie, usuwanie i modyfikowanie elementów w miejscu.

HashSet - Jest to zbiór, który można modyfikować poprzez dodawanie i usuwanie elementów w miejscu.

## Kolekcje niemutowalne
List - Jest to lista o stałym rozmiarze, której nie można modyfikować po jej utworzeniu. Można jednak tworzyć nowe listy na podstawie istniejącej listy.

Vector - Jest to wektor o stałym rozmiarze, który nie jest mutowalny. Może być wydajniejszy od List przy operacjach, które wymagają częstych modyfikacji, takich jak dodawanie i usuwanie elementów na początku listy.

Set - Jest to zbiór, który nie jest mutowalny. Może być wykorzystywany do przechowywania unikalnych elementów, ale nie można go modyfikować po jego utworzeniu.

## Dokumentacj/API kolekcji

- Operacje „wspólne” – czyli na kolekcjach „iterowalnych”: [Iterable](https://scala-lang.org/api/3.x/scala/collection/Iterable.html)
- Operacje na odwzorowaniach: [Map](https://scala-lang.org/api/3.x/scala/collection/Map.html)
  - Operacje na „widokach” odwzorowań [MapView](https://scala-lang.org/api/3.x/scala/collection/MapView.html)
- Operacje na zbiorach: [Set](https://scala-lang.org/api/3.x/scala/collection/Set.html)

__Uwaga__: W przypadku operacji dla `Map`, `MapView` oraz `Set` warto ukryć sobie operacje „dziedziczone”. W tym celu w rozwijalnym menu „Inherited”, znajdującym się pod „Members list”, należy wybrać „Not inherited”.

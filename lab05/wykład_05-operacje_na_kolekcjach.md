# Poznane operacje na kolekcjach

Na wykładach 4 i 5 mówiliśmy o „kolekcjach” `CC[A]`. Poniżej, nieco uzupełniona, lista poznanych operacji na nich:

## Operacje wspólne dla kolekcji „iterowalnych”

- rozmiar kolekcji

```scala
    isEmpty: Boolean
    nonEmpty: Boolean
    size: Int
    knownSize: Int
```

- testowanie elementów z ewentualnym wyborem

```scala
    exists (p: A ⇒ Boolean): Boolean
    forall (p: A ⇒ Boolean): Boolean
    count (p: A ⇒ Boolean): Int
    find (p: A ⇒ Boolean): Option[A]
```

- przekształcanie kolekcji

```scala
    map[B] (f: A ⇒ B): CC[B]
    flatMap[B] (f: A ⇒ IterableOnce[B]): CC[B]
```

- „zwijanie” i „grupowanie” kolekcji

```scala
    foldLeft[B] (z: B)(op: (B, A) ⇒ B): B
    groupBy[K](key: A => K): Map[K, CC[A]]
```

- dostęp do podkolekcji

```scala
    filter (p: A ⇒ Boolean): CC[A]
    filterNot (p: A ⇒ Boolean): CC[A]
    take (n: Int): CC[A]
    takeWhile (p: A ⇒ Boolean): CC[A]
    drop (n: Int): CC[A]
    dropWhile (p: A ⇒ Boolean): CC[A]
    slice (from: Int, until: Int): CC[A]
    collect[B] (pf: PartialFunction[A, B]): CC[B]
    collectFirst (pf: PartialFunction[A, B]): Option[B]
```

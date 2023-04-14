import org.scalatest.flatspec.AnyFlatSpec

class Zad02FlatSpec extends AnyFlatSpec {

  "For non-positive len and/or shift argument values the function" should "throw the IllegalArgumentException" in {
    assertThrows[IllegalArgumentException] {
      group(Nil)(0,1)
    }
    assertThrows[IllegalArgumentException] {
      group(Nil)(1,0)
    }
    assertThrows[IllegalArgumentException] {
      group(Nil)(-1,1)
    }
    assertThrows[IllegalArgumentException] {
      group(Nil)(1,-1)
    }
    val list = List(1,2,3,4,5);
    assertThrows[IllegalArgumentException] {
      group(list)(0,1)
    }
    assertThrows[IllegalArgumentException] {
      group(list)(1,0)
    }
    assertThrows[IllegalArgumentException] {
      group(list)(-1,1)
    }
    assertThrows[IllegalArgumentException] {
      group(list)(1,-1)
    }
  }

  "For empty list and positive values of len and shift the result" should "be empty" in {
    assert(group(Nil)(1,1) == Nil)
  }

  "Examples provided with the exercise" should "return expected results" in {
    val ( list, lenA, shiftA ) = ( List(1, 2, 3, 4, 5), 3, 1 )
    assert(group(list)(lenA, shiftA) == List(List(1, 2, 3), List(2, 3, 4), List(3, 4, 5)))
    val ( lenB, shiftB ) = ( 2, 2 )
    assert(group(list)(lenB, shiftB) == List(List(1, 2), List(3, 4), List(5)))
  }

  "For len >= list.length and any positive shift the value of group(list)(len, shift)" should "equal List(list)" in {
    val list = List(1,2,3,4,5,6,7)
    assert(group(list)(8, 1) == List(list))
    assert(group(list)(8, 2) == List(list))
    assert(group(list)(8, 7) == List(list))
    assert(group(list)(8, 14) == List(list))
  }

  "The value of group(list)(1, 1)" should "equal list.map(a => List(a))" in {
    val list = List(1,2,3,4,5,6,7)
    assert(group(list)(1, 1) == list.map(a => List(a)))
  }

}

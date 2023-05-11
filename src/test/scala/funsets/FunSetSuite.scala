package funsets

/**
 * This class is a test suite for the methods in object FunSets.
 *
 * To run this test suite, start "sbt" then run the "test" command.
 */
class FunSetSuite extends munit.FunSuite {

  import FunSets._

  test("contains is implemented") {
    assert(contains(x => true, 100))
  }

  /**
   * When writing tests, one would often like to re-use certain values for multiple
   * tests. For instance, we would like to create an Int-set and have multiple test
   * about it.
   *
   * Instead of copy-pasting the code for creating the set into every test, we can
   * store it in the test class using a val:
   *
   *   val s1 = singletonSet(1)
   *
   * However, what happens if the method "singletonSet" has a bug and crashes? Then
   * the test methods are not even executed, because creating an instance of the
   * test class fails!
   *
   * Therefore, we put the shared values into a separate trait (traits are like
   * abstract classes), and create an instance inside each test method.
   *
   */

  trait TestSets {
    val s1 = singletonSet(1)
    val s2 = singletonSet(2)
    val s3 = singletonSet(3)
  }

  /**
   * This test is currently disabled (by using @Ignore) because the method
   * "singletonSet" is not yet implemented and the test would fail.
   *
   * Once you finish your implementation of "singletonSet", remove the
   * .ignore annotation.
   */
  test("singleton set one contains one") {

    /**
     * We create a new instance of the "TestSets" trait, this gives us access
     * to the values "s1" to "s3".
     */
    new TestSets {
      /**
       * The string argument of "assert" is a message that is printed in case
       * the test fails. This helps identifying which assertion failed.
       */
      assert(contains(s1, 1), "Singleton")
    }
  }

  test("union contains all elements of each set") {
    new TestSets {
      val s = union(s1, s2)
      assert(contains(s, 1), "Union 1")
      assert(contains(s, 2), "Union 2")
      assert(!contains(s, 3), "Union 3")
    }
  }

  test("intersection contains all elements that exist in both sets") {
    new TestSets {
      val s = intersect(union(s1, s2), s2)
      assert(!contains(s, 1), "Intersect 1")
      assert(contains(s, 2), "Intersect 2")
      assert(!contains(s, 3), "Intersect 3")
    }
  }

  test("diff contains all elements that exist in first set but not in second set") {
    new TestSets {
      val s = diff(union(s1, s2), s2)
      assert(contains(s, 1), "Diff 1")
      assert(!contains(s, 2), "Diff 2")
      assert(!contains(s, 3), "Diff 3")
    }
  }

  test("filter contains all elements that exist in set and hold true to predicate") {
    new TestSets {
      val p = (x: Int) => x >= 2
      val s = union(union(s1, s2), s3)
      val ps = filter(s, p)
      assert(!contains(ps, 1), "Filter 1")
      assert(contains(ps, 2), "Filter 2")
      assert(contains(ps, 3), "Filter 3")
    }
  }

  test("forall returns true if all elements of set answer predicate") {
    new TestSets {
      val p = (x: Int) => x >= 2
      val s_1 = union(union(s1, s2), s3)
      val s_2 = union(s2, s3)
      assert(!forall(s_1, p), "forall 1")
      assert(forall(s_2, p), "forall 2")
    }
  }

  test("exists returns true if at least one element exists which belongs to set and answers predicate") {
    new TestSets {
      val p = (x: Int) => x >= 2
      val s_1 = union(union(s1, s2), s3)

      assert(!exists(s1, p), "exists 1")
      assert(exists(s_1, p), "exists 2")
    }
  }

  test("map returns a new set which contains transformed values of original set, given a function") {
    new TestSets {
      val f = (x: Int) => x + 2
      val sAll = union(union(s1, s2), s3)
      val sNew = map(sAll, f)

      assert(!contains(sNew, 1), "map 1")
      assert(!contains(sNew, 2), "map 2")
      assert(contains(sNew, 3), "map 3")
      assert(contains(sNew, 4), "map 4")
      assert(contains(sNew, 5), "map 5")
      assert(!contains(sNew, 6), "map 6")
    }
  }



  import scala.concurrent.duration._
  override val munitTimeout = 10.seconds
}

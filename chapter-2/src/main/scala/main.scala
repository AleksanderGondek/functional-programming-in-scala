object Main {
  // Exercise 2.1 
  def fib(n: Int): Int = {
    def loop(a: Int): Int = {
      if (a == 0) 0
      else if (a == 1) 1
      else loop(a - 2) + loop(a - 1)
    }

    @annotation.tailrec
    def rtoLoop(a: Int, x: Int = 0, y: Int = 1): Int = {
      if(a == 0) x
      else if (a == 1) y
      else rtoLoop(a - 1, y, x + y)
    }

    rtoLoop(n)
  }

  // Exercise 2.2
  def isSorted[A](as: Array[A], ordered: (A,A) => Boolean): Boolean = {
    @annotation.tailrec
    def loop(p: Int, c: Int): Boolean = {
      if(p == -1 || c >= as.length) true
      else if (ordered(as(p), as(c)) != true) false
      else loop(p+1, c+1)
    }

    return loop(-1, 0)
  }

  def main(args: Array[String]): Unit = {
    println(fib(5))
  }
}

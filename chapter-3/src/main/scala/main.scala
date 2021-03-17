sealed trait List[+A]
case object Nil extends List[Nothing]

case class Cons[+A](head: A, tail: List[A]) extends List[A]

object List {
  def apply[A](as: A*): List[A] =
    if (as.isEmpty) Nil
    else Cons(as.head, apply(as.tail: _*))
  
  // Exercise 3.2
  def tail[A](l: List[A]): List[A] = l match {
    case Nil => Nil
    case Cons(_, tail) => tail
  }

  // Exercise 3.3
  def setHead[A](l: List[A], x: A): List[A] = l match {
    case Nil => Cons(x, Nil)
    case Cons(_, t) => Cons(x, t)
  }
 
  // Exercise 3.4
  def drop[A](l: List[A], n: Int): List[A] = {
    @annotation.tailrec
    def loop(lst: List[A], i: Int): List[A] = {
      if (i >= n) lst
      else lst match {
        case Nil => Nil
        case Cons(_, tail) => loop(tail, i+1)
      }
    }

    loop(l, 0)
  }

  // Exercise 3.5
  def dropWhile[A](l: List[A], f: A => Boolean): List[A] = {
    l match {
      case Cons(head, tail) if f(head) => dropWhile(tail, f)
      case _ => l
    }
  }

  // Exercise 3.6
  def init[A](l: List[A]): List[A] = {
    // Because we are not using scala built-in list
    // and we do not know anything about lazy list / streams
    // however we HAVE TO prevent stack overflow
    import collection.mutable.ListBuffer
    val buffer = new ListBuffer[A]

    @annotation.tailrec
    def loop(lst: List[A]): List[A] = {
      lst match {
        case Nil => Nil
        case Cons(_, Nil) => List(buffer.toList: _*)
        case Cons(head, tail) => {
          buffer.append(head)
          loop(tail)
        }
      }
    }
    loop(l)
  }

  def foldRight[A,B](as: List[A], z: B)(f: (A,B) => B): B = {
    as match {
      case Nil => z
      case Cons(head, tail) => f(head, foldRight(tail, z)(f))
    }
  }

  def sum(ints: List[Int]): Int = {
    foldRight(ints, 0)((x,y) => x + y)
  }

  def product(ds: List[Double]): Double = {
    foldRight(ds, 1.0)(_ * _)
  }

  // Exercise 3.7
  // foldRight needs to evaluate every item in the list, before returning
  // so, early halt is not possible

  // Exercise 3.9
  def length[A](as: List[A]): Int = {
    // as match {
    //   case Nil => 0
    //   case Cons(head, tail) => 1 + length(tail)
    // }
    foldRight(as, 0)((_,acc) => acc +1)
  }

  // Exercise 3.10
  @annotation.tailrec
  def foldLeft[A,B](l: List[A], z: B)(f: (B,A) => B): B = {
    l match {
      case Nil => z
      case Cons(head, tail) => foldLeft(tail, f(z, head))(f)
    }
  }

  // Exercise 3.11
  def productLeft(ds: List[Double]): Double = {
    foldLeft(ds, 1.0)(_ * _)
  }

  // Exercise 3.12
  def reverse[A](l: List[A]): List[A] = {
    foldLeft(l, List[A]())(
      (all, x) => Cons(x, all)
    )
  }

  // Exercise 3.13
  def foldLeftViaFoldRight[A,B](l: List[A], z: B)(f: (B,A) => B): B = {
    foldRight(l, (b:B) => b)((a,g) => b => g(f(b,a)))(z)
  }    

  def foldRightViaFoldLeft[A,B](l: List[A], z: B)(f: (A,B) => B): B = {
    foldLeft(reverse(l), z)((b,a) => f(a,b))
  }

  // Exercise 3.14
  def append[A](a1: List[A], a2: List[A]): List[A] = {
    foldRightViaFoldLeft(a1, a2)(
      Cons(_,_)
    )
  }

  // Exercise 3.15
  def myLittleFlatten[A](l: List[List[A]]): List[A] = {
    foldLeft(l, List[A]())(append)
  }

  // Exercise 3.16
  def transformPlusOne(l: List[Int]): List[Int] = {
    foldRightViaFoldLeft(l, List[Int]())(
      (head, tail) => Cons(head+1, tail)
    )
  }

  // Exercise 3.17
  def stringify(l: List[Double]): List[String] = {
    foldRightViaFoldLeft(l, List[String]())(
      (head, tail) => Cons(head.toString(), tail)
    )
  }

  // Exercise 3.18
  def map[A,B](as: List[A])(f: A => B): List[B] = {
    foldRightViaFoldLeft(as, List[B]())(
      (head, tail) => Cons(f(head), tail)
    )
  }
}

object Main {
  def main(args: Array[String]): Unit = {
    println("// Exercise 3.1")
    val x = List(1,2,3,4,5) match {
      case Cons(x, Cons(2, Cons(4, _))) => x
      case Nil => 42
      case Cons(x, Cons(y, Cons(3, Cons(4, _)))) => x + y
      case Cons(h, t) => h + List.sum(t)
      case _ => 101
    }

    println(x) // Prints 3!

    println("// Exercise 3.2")
    val exampleList = List(1,2,3,4,5)
    println(List.tail(exampleList))
    println(List())

    println("// Exercise 3.3")
    println(List.setHead(exampleList, 6))
    println(List.setHead(List(), 6))

    println("// Exercise 3.4")
    println(List.drop(List(), 0))
    println(List.drop(List(), 1))
    println(List.drop(List(1,2,3,4,5), 0))
    println(List.drop(List(1,2,3,4,5), 1))
    println(List.drop(List(1,2,3,4,5), 5))
    println(List.drop(List(1,2,3,4,5), 6))

    println("// Exercise 3.5")
    println(List.dropWhile(List(1,2,3,4,5), (x: Int) => x < 4))
    println(List.dropWhile(List(1,2,3,4,5), (x: Int) => x < 6))
    println(List.dropWhile(List(), (x: Int) => x < 6))
    println(List.dropWhile(List(1,2,3), (x: Int) => x > 6))

    println("// Exercise 3.6")
    println(List.init(List(1,2,3,4,5)))
    println(List.init(List(1)))
    println(List.init(List()))

    println("// Exercise 3.8")
    println(List.foldRight(List(1,2,3), Nil:List[Int])(Cons(_,_)))

    println("// Exercise 3.9")
    println(List.length(List()))
    println(List.length(List(1)))
    println(List.length(List(1,2,3)))

    println("// Exercise 3.12")
    println(List.reverse(List()))
    println(List.reverse(List(1)))
    println(List.reverse(List(1,2,3)))

    println("// Exercise 3.14")
    println(List.append(List(1,2,3), List(4,5,6)))

    println("// Exercise 3.15")
    println(List.myLittleFlatten(List(List(5,6,7), List(8,9,0))))

    println("// Exercise 3.16")
    println(List.transformPlusOne(List(1,2,3,4,5)))

    println("// Exercise 3.17")
    println(List.stringify(List(2.0, 3.0, 4.0)))

    println("// Exercise 3.18")
    println(List.map(List(1,2,3,4))(x => x + 1))
  }
}

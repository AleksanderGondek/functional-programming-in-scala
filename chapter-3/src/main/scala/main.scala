sealed trait CList[+A]
case object CNil extends CList[Nothing]

case class CCons[+A](head: A, tail: CList[A]) extends CList[A]

// C stands for Custom
// Custom List, Custom Nil, Custom Cons

object CList {
  def apply[A](as: A*): CList[A] =
    if (as.isEmpty) CNil
    else CCons(as.head, apply(as.tail: _*))
  
  // Exercise 3.2
  def tail[A](l: CList[A]): CList[A] = l match {
    case CNil => CNil
    case CCons(_, tail) => tail
  }

  // Exercise 3.3
  def setHead[A](l: CList[A], x: A): CList[A] = l match {
    case CNil => CCons(x, CNil)
    case CCons(_, t) => CCons(x, t)
  }
 
  // Exercise 3.4
  def drop[A](l: CList[A], n: Int): CList[A] = {
    @annotation.tailrec
    def loop(lst: CList[A], i: Int): CList[A] = {
      if (i >= n) lst
      else lst match {
        case CNil => CNil
        case CCons(_, tail) => loop(tail, i+1)
      }
    }

    loop(l, 0)
  }

  // Exercise 3.5
  def dropWhile[A](l: CList[A], f: A => Boolean): CList[A] = {
    l match {
      case CCons(head, tail) if f(head) => dropWhile(tail, f)
      case _ => l
    }
  }

  // Exercise 3.6
  def init[A](l: CList[A]): CList[A] = {
    // Because we are not using scala built-in list
    // and we do not know anything about lazy list / streams
    // however we HAVE TO prevent stack overflow
    import collection.mutable.ListBuffer
    val buffer = new ListBuffer[A]

    @annotation.tailrec
    def loop(lst: CList[A]): CList[A] = {
      lst match {
        case CNil => CNil
        case CCons(_, CNil) => CList(buffer.toList: _*)
        case CCons(head, tail) => {
          buffer.append(head)
          loop(tail)
        }
      }
    }
    loop(l)
  }

  def foldRight[A,B](as: CList[A], z: B)(f: (A,B) => B): B = {
    as match {
      case CNil => z
      case CCons(head, tail) => f(head, foldRight(tail, z)(f))
    }
  }

  def sum(ints: CList[Int]): Int = {
    foldRight(ints, 0)((x,y) => x + y)
  }

  def product(ds: CList[Double]): Double = {
    foldRight(ds, 1.0)(_ * _)
  }

  // Exercise 3.7
  // foldRight needs to evaluate every item in the list, before returning
  // so, early halt is not possible

  // Exercise 3.9
  def length[A](as: CList[A]): Int = {
    // as match {
    //   case CNil => 0
    //   case CCons(head, tail) => 1 + length(tail)
    // }
    foldRight(as, 0)((_,acc) => acc +1)
  }

  // Exercise 3.10
  @annotation.tailrec
  def foldLeft[A,B](l: CList[A], z: B)(f: (B,A) => B): B = {
    l match {
      case CNil => z
      case CCons(head, tail) => foldLeft(tail, f(z, head))(f)
    }
  }

  // Exercise 3.11
  def productLeft(ds: CList[Double]): Double = {
    foldLeft(ds, 1.0)(_ * _)
  }

  // Exercise 3.12
  def reverse[A](l: CList[A]): CList[A] = {
    foldLeft(l, CList[A]())(
      (all, x) => CCons(x, all)
    )
  }

  // Exercise 3.13
  def foldLeftViaFoldRight[A,B](l: CList[A], z: B)(f: (B,A) => B): B = {
    foldRight(l, (b:B) => b)((a,g) => b => g(f(b,a)))(z)
  }    

  def foldRightViaFoldLeft[A,B](l: CList[A], z: B)(f: (A,B) => B): B = {
    foldLeft(reverse(l), z)((b,a) => f(a,b))
  }

  // Exercise 3.14
  def append[A](a1: CList[A], a2: CList[A]): CList[A] = {
    foldRightViaFoldLeft(a1, a2)(
      CCons(_,_)
    )
  }

  // Exercise 3.15
  def myLittleFlatten[A](l: CList[CList[A]]): CList[A] = {
    foldLeft(l, CList[A]())(append)
  }

  // Exercise 3.16
  def transformPlusOne(l: CList[Int]): CList[Int] = {
    foldRightViaFoldLeft(l, CList[Int]())(
      (head, tail) => CCons(head+1, tail)
    )
  }

  // Exercise 3.17
  def stringify(l: CList[Double]): CList[String] = {
    foldRightViaFoldLeft(l, CList[String]())(
      (head, tail) => CCons(head.toString(), tail)
    )
  }

  // Exercise 3.18
  def map[A,B](as: CList[A])(f: A => B): CList[B] = {
    foldRightViaFoldLeft(as, CList[B]())(
      (head, tail) => CCons(f(head), tail)
    )
  }

  // Exercise 3.19
  def filter[A](as: CList[A])(f: A => Boolean): CList[A] = {
    foldRightViaFoldLeft(as, CList[A]())(
      (head, tail) => {
        if(f(head)) {
          CCons(head, tail)
        } else {
          tail
        }
      }
    )
  }

  // Exercise 3.20
  def flatMap[A,B](as: CList[A])(f: A => CList[B]): CList[B] = {
    CList.myLittleFlatten(CList.map(as)(f))
  }

  // Exercise 3.21
  def filterViaFlatMap[A](as: CList[A])(f: A => Boolean): CList[A] = {
    CList.flatMap(as)(x => if (f(x)) CList(x) else CList())
  }

  // Exercise 3.22
  def addPairs(as: CList[Int], at: CList[Int]): CList[Int] = {
    (as, at) match {
      case (_, CNil) => CNil
      case (CNil, _) => CNil
      case(CCons(as_head, as_tail), CCons(at_head, at_tail)) => CCons(as_head+at_head, addPairs(as_tail, at_tail))
    }
  }

  // Exercise 3.23
  def zipWith[A](xr: CList[A], xs: CList[A])(f: (A, A) => A): CList[A] = {
    (xr, xs) match {
      case (_, CNil) => CNil
      case (CNil, _) => CNil
      case(CCons(xr_head, xr_tail), CCons(xs_head, xs_tail)) => CCons(f(xr_head, xs_head), zipWith(xr_tail, xs_tail)(f))
    }
  }

  // Exercise 3.24
  @annotation.tailrec
  def _begins_with[A](xr: CList[A], xs: CList[A]): Boolean = {
    (xr, xs) match {
      case (CNil, CNil) => true
      // In general, CList does not 'begin with' CNil,
      // but this implementation allows us to built 
      // recursive solution.
      case (_, CNil) => true
      case (CCons(xr_head, xr_tail), CCons(xs_head, xs_tail)) if (xr_head == xs_head) => _begins_with(xr_tail, xs_tail)
      case _ => false
    }
  }

  @annotation.tailrec
  def hasSubsequence[A](sup: CList[A], sub: CList[A]): Boolean = {
    sup match {
      case CNil => sub == CNil
      case _ if _begins_with(sup, sub) => true
      case CCons(_, sup_tails) => hasSubsequence(sup_tails, sub)
    }
  }
}


sealed trait CTree[+A]
case class CLeaf[A](value: A) extends CTree[A]
case class CBranch[A](left: CTree[A], right: CTree[A]) extends CTree[A]

object CTree {
  println("// Exercise 3.25")
  def size[A](tree: CTree[A]): Int = {
    def loop(t: CTree[A]): Int = {
      t match {
        case CLeaf(_) => 1
        case CBranch(l,r) => {
          1 + loop(l) + loop(r)
        }
      }
    }

    loop(tree)
  }

  println("// Exercise 3.26")
  def max(tree: CTree[Int]): Int = {
    def loop(t: CTree[Int]): Int = {
      t match {
        case CLeaf(i) => i
        case CBranch(l,r) => {
          loop(l) max loop(r)
        }
      }
    }

    loop(tree)
  }

  println("// Exercise 3.27")
  def depth[A](tree: CTree[A]): Int = {
    def loop(t: CTree[A]): Int = {
      t match {
        case CLeaf(_) => 1
        case CBranch(l,r) => {
          (1 + loop(l)) max (1 + loop(r))
        }
      }
    }

    loop(tree)
  }

  println("// Exercise 3.28")
  def map[A,B](tree: CTree[A])(f: A => B): CTree[B] = {
    def loop(t: CTree[A]): CTree[B] = {
      t match {
        case CLeaf(x) => CLeaf(f(x))
        case CBranch(l,r) => {
          CBranch(loop(l), loop(r))
        }
      }
    }

    loop(tree)
  }
}

object Main {
  def main(args: Array[String]): Unit = {
    println("// Exercise 3.1")
    val x = CList(1,2,3,4,5) match {
      case CCons(x, CCons(2, CCons(4, _))) => x
      case CNil => 42
      case CCons(x, CCons(y, CCons(3, CCons(4, _)))) => x + y
      case CCons(h, t) => h + CList.sum(t)
      case _ => 101
    }

    println(x) // Prints 3!

    println("// Exercise 3.2")
    val exampleList = CList(1,2,3,4,5)
    println(CList.tail(exampleList))
    println(CList())

    println("// Exercise 3.3")
    println(CList.setHead(exampleList, 6))
    println(CList.setHead(CList(), 6))

    println("// Exercise 3.4")
    println(CList.drop(CList(), 0))
    println(CList.drop(CList(), 1))
    println(CList.drop(CList(1,2,3,4,5), 0))
    println(CList.drop(CList(1,2,3,4,5), 1))
    println(CList.drop(CList(1,2,3,4,5), 5))
    println(CList.drop(CList(1,2,3,4,5), 6))

    println("// Exercise 3.5")
    println(CList.dropWhile(CList(1,2,3,4,5), (x: Int) => x < 4))
    println(CList.dropWhile(CList(1,2,3,4,5), (x: Int) => x < 6))
    println(CList.dropWhile(CList(), (x: Int) => x < 6))
    println(CList.dropWhile(CList(1,2,3), (x: Int) => x > 6))

    println("// Exercise 3.6")
    println(CList.init(CList(1,2,3,4,5)))
    println(CList.init(CList(1)))
    println(CList.init(CList()))

    println("// Exercise 3.8")
    println(CList.foldRight(CList(1,2,3), CNil:CList[Int])(CCons(_,_)))

    println("// Exercise 3.9")
    println(CList.length(CList()))
    println(CList.length(CList(1)))
    println(CList.length(CList(1,2,3)))

    println("// Exercise 3.12")
    println(CList.reverse(CList()))
    println(CList.reverse(CList(1)))
    println(CList.reverse(CList(1,2,3)))

    println("// Exercise 3.14")
    println(CList.append(CList(1,2,3), CList(4,5,6)))

    println("// Exercise 3.15")
    println(CList.myLittleFlatten(CList(CList(5,6,7), CList(8,9,0))))

    println("// Exercise 3.16")
    println(CList.transformPlusOne(CList(1,2,3,4,5)))

    println("// Exercise 3.17")
    println(CList.stringify(CList(2.0, 3.0, 4.0)))

    println("// Exercise 3.18")
    println(CList.map(CList(1,2,3,4))(x => x + 1))

    println("// Exercise 3.19")
    println(CList.filter(CList(1,2,3,4,5,6,7,8,9))(x => x % 2 == 0))

    println("// Exercise 3.20")
    println(CList.flatMap(CList(1,2,3))(i => CList(i,i)))

    println("// Exercise 3.21")
    println(CList.filter(CList(1,2,3,4,5,6,7,8,9))(x => x % 2 == 0))

    println("// Exercise 3.22")
    println(CList.addPairs(CList(1,1,1), CList(1,1,1)))

    println("// Exercise 3.23")
    def substract(a: Int, b: Int) = a-b
    println(CList.zipWith(CList(1,1,1), CList(1,1,1))(substract))

    println("// Exercise 3.24")
    println("_begins_with: ")
    println(CList._begins_with(
      CList(1,2,3,4,5),
      CList(1,2,3)
    ))
    println("hasSubsequence: ")
    println(CList.hasSubsequence(
      CList(1,2,3,4,5,6,7,8,9,0),
      CList(7,8,9)
    ))

    println("// Exercise 3.25")
    val test = CBranch(
      CBranch(CLeaf("a"), CLeaf("b")),
      CBranch(CLeaf("c"), CLeaf("d"))
    )
    println(CTree.size(test))
    val testTwo = CBranch(
      CBranch(
        CBranch(CLeaf("a"), CLeaf("b")),
        CBranch(CLeaf("c"), CLeaf("d"))
      ),
      CBranch(
        CBranch(CLeaf("e"), CLeaf("f")),
        CBranch(CLeaf("g"), CLeaf("h"))
      ),
    )
    println(CTree.size(testTwo))

    println("// Exercise 3.26")
    val testThree = CBranch(
      CBranch(CLeaf(1), CLeaf(2)),
      CBranch(CLeaf(3), CLeaf(4))
    )
    println(CTree.max(testThree))

    println("// Exercise 3.27")
    println(CTree.depth(test))

    println("// Exercise 3.28")
    println(CTree.map(testThree)(n => n + 1))
  }
}

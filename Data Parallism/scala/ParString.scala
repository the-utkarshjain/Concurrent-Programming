package parString
import scala.collection.parallel._
import scala.collection.mutable._


/* 
 * In this exercise, we will briefly focus on achieving better 
 * performance while requiring minimal changes in the organization of legacy programs. One 
 * specific way of achieving better performance is data parallelism which relies on doing computation
 * in parallel on a different portion of a data collection. 
 *
 * 
 * We implement a custom parallel collection called ParString which is a counter 
 * part of the Java's String class that supports parallel operations:
 * 
 * class ParString(val str: String) extends immutable.ParSeq[Char] {
 *  def apply(i: Int) 
 *  def length
 *  def seq
 *  def splitter
 *  override def newCombiner
 * }
 *
 * A parallel string is a sequence of characters, so it is only logical to extend the ParSeq parallel collection 
 * trait with the Char type argument. When we extend a parallel collection, we need to implement its apply, 
 * length, splitter, and seq methods. Follow the Hint links below for more detail.       
 *
 * Resources: Chapter 5 of the book: Learning concurrent programming in Scala by Aleksandar Prokopec and/or 
 * https://docs.scala-lang.org/overviews/parallel-collections/custom-parallel-collections.html
*/


class ParString(val str: String) extends immutable.ParSeq[Char] {
  def apply(i: Int) = str.charAt(i)
  
  def length = str.length
  
  def seq = new collection.immutable.WrappedString(str)
  
  def splitter = new ParStringSplitter(str, 0, str.length)

  class ParStringSplitter(private var s: String, private var i: Int, private val ntl: Int)
  extends SeqSplitter[Char] {

    final def hasNext = i < ntl

    final def next = {
      val r = s.charAt(i)
      i += 1
      r
    }

    def remaining = ntl - i

    def dup = new ParStringSplitter(s, i, ntl)

    def split = {
      val rem = remaining
      if (rem >= 2) psplit(rem / 2, rem - rem / 2)
      else Seq(this)
    }

    def psplit(sizes: Int*): Seq[ParStringSplitter] = {
      val splitted = new ArrayBuffer[ParStringSplitter]
      for (sz <- sizes) {
        val next = (i + sz) min ntl
        splitted += new ParStringSplitter(s, i, next)
        i = next
      }
      if (remaining > 0) splitted += new ParStringSplitter(s, i, ntl)
      splitted
    }
  }

  override def newCombiner =  new ParStringCombiner

  class ParStringCombiner extends Combiner[Char, ParString] {
  var sz = 0
  val chunks = new ArrayBuffer[StringBuilder] += new StringBuilder
  var lastc = chunks.last

  def size: Int = sz

  def +=(elem: Char): this.type = {
    lastc += elem
    sz += 1
    this
  }

  def clear = {
    chunks.clear
    chunks += new StringBuilder
    lastc = chunks.last
    sz = 0
  }

  def result: ParString = {
    val rsb = new StringBuilder
    for (sb <- chunks) rsb.append(sb)
    new ParString(rsb.toString)
  }

  def combine[U <: Char, NewTo >: ParString](other: Combiner[U, NewTo]) = if (other eq this) this else {
    val that = other.asInstanceOf[ParStringCombiner]
    sz += that.sz
    chunks ++= that.chunks
    lastc = chunks.last
    this
  }
}

}

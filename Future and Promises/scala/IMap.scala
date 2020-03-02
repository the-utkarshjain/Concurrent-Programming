package imap
import scala.concurrent.{Await, Future, Promise}
import scala.concurrent.duration.Duration
import java.util.concurrent.ConcurrentHashMap

/* 
 * In a way, many of the concurrency patterns seen so far support asynchronous
 * programming; thread creation and scheduling execution context tasks can be used to start
 * executing a computation concurrent to the main program flow. Still, using these facilities
 * directly when avoiding blocking or composing asynchronous computations is not
 * straightforward. In this task, we will focus on two abstractions in Scala that are
 * specifically tailored for this task: futures and promises.
 * 
 * In this task, we implement a single-assignment map with a twist:
 * 
 * class IMap[K, V] {
 *  def update(k: K, v: V): Unit
 *  def apply(k: K): Future[V]
 *  def whenReady[T: scala.reflect.ClassTag](func: V => T , p: Promise[T], f: Future[V])
 * }
 *
 * Pairs of keys and values can be added to the IMap object, but they can neither be
 * removed or modified. A specific key can be assigned only once, and subsequent calls to 
 * update with that same key should result in an exception. Calling apply with a specific 
 * key should return a future, which is completed after that key is inserted into the map.
 * As an additional learning twist we will also implement a whenReady function which completes a promise 
 * p using a function func (i.e. p success func(v)) when the future f is completed with a value v (use a callback 
 * to check f's completion).       
 *
 * Resources: Consult lecture 5, https://docs.scala-lang.org/overviews/core/futures.html, 
 * and chapter 4 of the book: Learning concurrent programming in Scala by Aleksandar Prokopec.
*/

class IMap[K, V] {
  import scala.collection.convert.decorateAsScala._
  import scala.concurrent.ExecutionContext.Implicits.global
  private val m = new ConcurrentHashMap[K, V]().asScala

  def update(k: K, v: V): Unit = {
    if (m.get(k) != None) {
      throw new Exception("[Error] Key Exists")
    }
    else{
      m.put(k, v);
    }
  }

  def apply(k: K): Future[V] = Future {
    Thread.sleep(100)
    m.get(k).get
  }

  
  def whenReady[T: scala.reflect.ClassTag](func: V => T , p: Promise[T], f: Future[V]) =  Future {
    var v = Await.result(f, Duration.Inf)
    p success func(v)
    
  }
}

package lockFree
import instrumentation.monitors.LockFreeMonitor
import atomicReference.SimpleAtomicReference

/*
* One of the main uses of a atomic types is to implement syncronization 
* perimitives such as semaphores and mutexes. It is also useul to implement
* lock-free algorithms.
*  
* In this task we implement a lock-free stack using an atomic reference.
* Assume that you have a class implementing an atomic refrence called 
* SimpleAtomicReference with an interface as follows:
*
* class SimpleAtomicReference[V](initValue: V) {
*  protected var value:V
*
*  def compareAndSet(expect: V, update: V): Boolean
*  def get: V
*  def set(newValue: V): Unit 
*}
*/

/* Implement a lock free stack. */
class LockFreeStack[E](capacity: Int) extends LockFreeMonitor {
  class Node[E](val value: E) {
    var next: Node[E] = null
  }
  val top = new SimpleAtomicReference[Node[E]](null)
  // Do not add other variables

  def push(e: E): Unit = {

      var temp = new Node[E](e)
      temp.next = top.get
      top.set(temp)

  } 

  def pop(): E = {

      var temp = top.get
      top.set(top.value.next)
      return temp.value
    }
}


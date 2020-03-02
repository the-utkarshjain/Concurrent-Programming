package simpleCountdownLatch

/* Many languages including Scala and Java provide a low-level mechanism 
* that effectively allow one thread to signal another. Without such mechanism,
* various high-level constructs would be difficult to implement. To put it simply,
* Java and Scala provide wait(), notify() and notifyAll() methods to facilitate 
* signaling between threads.
*
* One simple example of a high-level construct that can be implemented using such 
* a signaling mechanism is a countdown latch. A countdown latch is a counter that 
* triggers an event when a count reaches zero from an initially set value.
* A countdown latch allows one or more threads to wait until a set of operations 
* being performed in other threads complete  
*
* Resources
* https://docs.oracle.com/javase/tutorial/essential/concurrency/guardmeth.html 
*/

class SimpleCountDownLatch(initCount: Int) extends AbstractCountDownLatch {
  
  if(initCount < 0){
    throw new IllegalArgumentException("initCount must be non-negative")
  }

  count = initCount
  
  def await(): Unit = {
    while(count > 0)
    {
      wait()
    }

  }

  def countDown(): Unit = {
    this.synchronized
    {
      if( count == 0)
        {
          notifyAll()
          return
        }
      count = count - 1
    }
  }

}
package monitorBlocking
import instrumentation.monitors.Monitor
import scala.collection.mutable._

/*
* One example use of monitor locks is in implementing Monitor Blocking Queue. 
* A Monitor Blocking Queue is a queue that blocks when you try to take from it 
* and the queue is empty, or if you try to put items to it and the queue is 
* already full. A thread trying to take from an empty queue is blocked until 
* some other thread inserts an item into the queue. A thread trying to put an 
* item in a full queue is blocked until some other thread makes space in the queue.
* 
* In this task, we implement a simple thread-safe Blocking Monitor 
* Queue with take() and put() methods.
* 
*/

/* Use synchronized, wait and notifyAll to synchronize the operations. */
class MonitorBlockingQueue[E](capacity: Int) extends Monitor {
  if (capacity <= 0) {
    throw new IllegalArgumentException("capacity must be positive")
  }

  /* Holds the elements of this BlockingQueue. */
  val queue = Queue[E]()

  def put(e: E): Unit = {
    this.synchronized
    {
      if(queue.length < capacity)
      {
        queue.enqueue(e)
        notifyAll();
      }
      else
      {
        while(queue.length == capacity)
        {
          wait()
        }
        queue.enqueue(e)
        notifyAll()
      }
    }
  }

  def take(): E = {
    this.synchronized
    {
      if(queue.length > 0)
      { 
        var temp = queue.dequeue
        notifyAll()
        return temp
      }
      else
      {
        while(queue.length == 0)
        {
          wait()
        }
        var temp = queue.dequeue
        notifyAll()
        return temp
      
      }
    }
  }
}

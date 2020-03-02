package atomicInteger
/* One issue that needs to be noted when implementing concurrent
 * programs is memory consistency and errors associated with it. We 
 * say memory consistency error occured when multiple execution units
 * operating on a shared memory have an inconsistent view of a given 
 * shared variable. There are a lot of reasons why this inconsistanceies 
 * happen which include caching and compiler optimizations. The key to 
 * avoid memory concistency problem is to understand and insure happens-before
 * relationship such that if a happens-before b, the effects of a will be visible 
 * to b.
 *    
 * In this exercise we implement a simple atomic integer; an integer value 
 * that may be updated atomically and provides a happen-before relationship when
 * reading and writing an integer value.  
 *
*/
class SimpleAtomicInteger(initValue: Int) extends AbstractAtomicInteger {
  value = initValue

  def compareAndSet(expect: Int, update: Int): Boolean = {
    this.synchronized
    {
      if(value == expect)
      {
        value = update;
        return true;
      }

      return false;
    }
  }

  def get: Int = {
    return value;
  }

  def set(newValue: Int): Unit = {
    this.synchronized
    {
      value = newValue;
    }
  }
}
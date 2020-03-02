package roundRobin
import akka.actor._

/* In this task, we implement an actor called RoundRobin declared as  
class RoundRobin[A <: Actor : scala.reflect.ClassTag](numChildren: Int) extends Actor. 
This actor would be able to create a number of children(numChildren) and enqueue them 
in an internal queue called children. For each message, it receives the RoundRobin actor 
should forward the message to the child found at the head of the children queue and 
move(enqueue) that child to the tail of the queue. The task is about 
creating children and forwarding messages
*/

class RoundRobin[A <: Actor: scala.reflect.ClassTag](numChildren: Int) extends Actor {
  if (numChildren <= 0)
    throw new IllegalArgumentException("numChildren must be positive")
  // TODO: Implement class
  var children = collection.mutable.Queue[ActorRef]()

  override def preStart = {
    var childActorList = for{a <- 1 to numChildren} yield context.actorOf(Props[A], a.toString);
    childActorList.foreach(child_actor => children.enqueue(child_actor))
  }

  def receive = {
    case x => {
  
      var temp = children.dequeue;
      children.enqueue(temp)
      temp.forward(x);
    }
  } 
}



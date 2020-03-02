package lifecycle
import akka.actor._
import akka.actor.SupervisorStrategy._
import scala.concurrent.duration._
import akka.actor.OneForOneStrategy._
/*
  The following actor(LifeCycler) has a child of type Other which is defined 
  in the file Other.scala. Task is to get the child actor print the string "SCALA". 

  Looking at the Other.scala you can notice that the Other actor will print 
  "SCALA" if its lifecycle goes as follows: preStart, receive 1, preRestart, postRestart 
  and  postStop.
*/

class LifeCycler[A <: Actor: scala.reflect.ClassTag](sink: ActorRef) extends Actor {
  // TODO: Implement class

  val child = context.actorOf(Props(classOf[Other], sink), "child")

  override def preStart = {
    child ! 1
    child ! 0
  }

  override val supervisorStrategy = OneForOneStrategy() {

    case _ :ExceptionB => Restart

    case _ :ExceptionA => Stop

    }

  def receive = {
    case _ => print ("Execute Me")
  }
}

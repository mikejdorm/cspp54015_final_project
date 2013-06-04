package servlet
import akka.event.Logging
import akka.actor._
import akka.routing.RoundRobinRouter


  object AsyncHandler{
  
	    val system = ActorSystem("AsyncHandler")
	    val listener = system.actorOf(Props[ResultListener], name = "resultlistener")
	    val master = system.actorOf(Props(new AsyncMaster(4)),name = "asyncmaster")
	      
    def createJob(req:HttpRequest, func:HttpRequest=>Option[HttpResponse]){
    	      master ! Work(req,func)
    }


class AsyncMaster(workers:Int) extends Actor{
  val requestRouter = context.actorOf(
      Props[AsyncJob].withRouter(RoundRobinRouter(workers)), name = "workerRouter")
  def receive = {
      case Work(req,func) => requestRouter ! Work(req,func)
      case HttpResponseResult(req,response) =>    println(req + " " + response)    
    }
}

class AsyncJob() extends Actor{
   def receive = {
      case Work(req, func) =>
        sender ! HttpResponseResult(req, func.apply(req)) // perform the work
    }
}

 class ResultListener extends Actor {
    def receive = {
      case HttpResponseResult(value, func) => println("Received result for : ".format(value))
    }
  }
 
 
  sealed trait AsyncRequestMessage
  case class Work(req:HttpRequest, func:HttpRequest=>Option[HttpResponse]) extends AsyncRequestMessage
  case class HttpResponseResult(req:HttpRequest, response: Option[HttpResponse]) extends AsyncRequestMessage
}
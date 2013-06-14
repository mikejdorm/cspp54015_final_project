package servlet

import akka.routing.RoundRobinRouter
import akka.actor.{ActorSystem, Actor, Props}
import akka.event.Logging
import scala.collection.mutable.ConcurrentMap
import scala.collection.mutable.HashMap
import scala.collection.mutable.SynchronizedMap
import java.util.concurrent.ConcurrentHashMap
import scala.collection.JavaConverters._
import scala.collection.concurrent.Map

object AsyncHandler{

	private var work:ConcurrentHashMap[Int,Option[HttpResponseResult]] = new ConcurrentHashMap[Int, Option[HttpResponseResult]]   
	type AsyncHandler = (HttpRequest=>Option[HttpResponse])
	@volatile private var asyncHandlers: List[AsyncHandler] = Nil
	val system = ActorSystem("AsyncRouter")
	val master = system.actorOf(Props(new AsyncMaster(30)), name = "asyncmaster")
     	
    private def vendId:Int = {
	  var max:Int = 0
	  work.keys.asScala.foreach(f => if(f>max) max = f)  	
	  return max+1
	}
	
   def storeResult(id:Int, req:HttpRequest, res:Option[HttpResponse]) = {
         println("Storing result: " + id + " " + res)
         work.put(id,Some(new HttpResponseResult(id,req,res)))
         
   }
 
   private def prefixToUrl(prefix:Seq[String]):String = prefix.toList match{
     case x::Nil => x
     case x::xs => x+"/"+prefixToUrl(xs)
     case _ => ""
   }
   
  private def retreiveResult(str:String):Option[HttpResponse] = {
       var intVal:Int = 0
       try{intVal = str.toInt}
       catch{case e: Exception => return None}
        work.get(intVal) match{
         case None => None
         case Some(x)=> x.response
         case _ => None
       }
   }
   
   def registerHandler(Prefix:Seq[String], RequestType:HttpRequestType, handle:AsyncHandler): 
     	   PartialFunction[HttpRequest, Option[HttpResponse]] = {
		   case a: HttpRequest => a.context match{  
		   case HttpContext(Prefix,RequestType) => {
		     println("Creating Async Job")
		     val id = vendId
		     work.put(id,None)
		     createJob(id,a,handle)
		     Some(new HttpAcceptProcessingResponse(prefixToUrl(Prefix)+"/"+vendId.toString))
		   }
		   case HttpContext(Prefix:+x,RequestType) => {
		     Some(new HttpOkResponse("You got it"))
		     retreiveResult(x) match {
		       case Some(x)=> Some(x)
		       case None => Some(new HttpAcceptProcessingResponse(prefixToUrl(Prefix)+"/"+x))
		     }
		   }
		   case _ => {
		     println("Async received: " + a) 
		     None}
		 }
		 case _ => { println("Error in the handler")
			 		Some(new HttpErrorResponse("Item not found")) 
		 }		 
    }
     	
    def getNumberOfHandlers:Int = asyncHandlers.size
	def getHandlers:List[AsyncHandler] = asyncHandlers  
	        
    private def createJob(id:Int, req:HttpRequest, func:HttpRequest=>Option[HttpResponse]){
    	      master ! Work(id,req,func)
	}
}
		    
	private class AsyncMaster(workers:Int) extends Actor{
	  val log = Logging(context.system, this)
	  log.debug("Initializing Actor Master Router")
	  val requestRouter = context.actorOf(
	  Props[AsyncJob].withRouter(RoundRobinRouter(workers)), name = "workerRouter")
	  def receive = {
	      case Work(id, req,func) => requestRouter ! Work(1,req,func)
	      case HttpResponseResult(id, req,response) =>   {
	        log.info("Result created for job " + id)
	        AsyncHandler.storeResult(id,req,response)
	      }
	    }
	}
	
	private class AsyncJob() extends Actor{
	  	  val log = Logging(context.system, this)
	   def receive = {
	      case Work(id, req, func) =>{
	        log.info("Scheduling work: " + id)
	        sender ! HttpResponseResult(id,req, func.apply(req))
	      }
	    }
	}

  sealed trait AsyncRequestMessage
  case class Work(id:Int, req:HttpRequest, func:HttpRequest=>Option[HttpResponse]) extends AsyncRequestMessage
  case class HttpResponseResult(id:Int, req:HttpRequest, response: Option[HttpResponse]) extends AsyncRequestMessage

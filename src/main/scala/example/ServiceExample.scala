package example
import servlet._
import org.json4s._
import org.json4s.native.JsonMethods._

/*
 * Below are two simple examples of a web service using the library
 * One service allows for the retrieval of salutations (hello, hi, etc.) the
 * other valedictions (goodbye, adios, etc.). 
 * 
 * The salutations can be reached at http://localhost:8080/api/salutation
 * 
 * The valedtictions can be reached at http://localhost:8080/api/valediction
 * 
 * The uri's for the services are defined in the line RequestHandler.defineHandler(crudDefaultHandler(....
 * 
 * The crud default handler will then create all of the handler methods for the different
 * create, read, update, and delete functions. It does this by pairing up uri's with http methods
 * and then utilizing pattern matching to match incoming http requests with the corresponding 
 * proper action. 
 * 
 * An HTTP Get will execute the read method, HTTP Put will update, HTTP Post will create an object, 
 * and HTTP Delete will remove an object.
 * 
 * The great thing with this library is that to implement all these actions for a simple service all
 * you really need are the following lines to implement a service with the URI 'api/salutation' that
 * handles all the actions for a class holding a string value.
 * 
 *    case class Salutation(val value:String) extends CrudHelperClass with JsonSerializable
 *	  object Salutation extends CrudHelperObject[Salutation]{
 *	 	 RequestHandler.defineHandler(crudDefaultHandler(Seq("api"):+"salutation"))
 *	  }
 * 	
	
 */
object ServiceExample{

	case class Salutation(val value:String) extends CrudHelperClass with JsonSerializable
	object Salutation extends CrudHelperObject[Salutation]{
	  create(new Salutation("hello"))
	  create(new Salutation("hi"))
	  create(new Salutation("hola"))
	  create(new Salutation("bonjour"))
	  RequestHandler.defineHandler(crudDefaultHandler(Seq("api"):+"salutation"))
	  RequestHandler.defineHandler(defineCustomHandler(Seq("api"):+"salutation":+"custom"))
	  
	  def defineCustomHandler(Prefix:Seq[String]): 
		  PartialFunction[HttpRequest, ()=>Option[HttpResponse]] = {
		    case req: HttpRequest => req.context match{
		  		case HttpContext(Prefix:+x, Get) => ()=>customHandler(req)
		    }
		    case _ => ()=> Some(new HttpErrorResponse("Item not found"))
	  }
	  
	  def workerFunction(req:HttpRequest):Option[HttpResponse] = {
	    None
	  }
	  
	  def customHandler(req:HttpRequest):Option[HttpResponse] = {
	    AsyncHandler.createJob(req, workerFunction)
	    Some(new HttpAcceptProcessingResponse("Accepted"))
	  }
	}
	
	case class Valediction(val value:String) extends CrudHelperClass
	object Valediction extends CrudHelperObject[Valediction]{
	  create(new Valediction("goodbye"))
	  create(new Valediction("farwell"))
	  create(new Valediction("adios"))
	  create(new Valediction("hasta luego"))
	  create(new Valediction("later"))
	  RequestHandler.defineHandler(crudDefaultHandler(Seq("api"):+"valediction"))
	}
}

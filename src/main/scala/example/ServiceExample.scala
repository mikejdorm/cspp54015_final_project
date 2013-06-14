package example
import servlet._
//import org.json4s._
//import org.json4s.native.JsonMethods._

//import reflect.{ClassDescriptor, Reflector}
//import org.json4s._
import org.json4s._
import org.json4s.jackson.JsonMethods._ 

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

	  def asyncHandler(req:HttpRequest):Option[HttpResponse] = {
			  for(i <- 1 to 10){
			    println(i)
			    Thread.sleep(1000)
			  }
			  Some(new HttpOkResponse(
					  Person.read(0) match{
					    case Some(person) => JsonUtils.anyToJValue(person)
					    case None => "No person"
					  }
			    )
			)
	  }
	  
	case class Person(name:String, age:Int, location:String) extends CrudHelperClass
	object Person extends CrudHelperObject[ServiceExample.Person] with JsonSerializable[ServiceExample.Person]{	  
	      create(new Person("Michael", 29, "Chicago"))
	      create(new Person("Rachel", 30, "Chicago"))     
	   
	      override def update(str:String):Option[Person] = fromJson(str) 
	      override def fromJson(json:String):Option[Person] = {
	      try{
	    	 Some(parse(json).extract[ServiceExample.Person])
	       } 
	       catch{
	       		case e: Exception => {
	       		  println("Error parsing json")
	       		  None
	       		}
	       }
	     }
	     override def create(str:String):Option[Person] = {
	      println("Creating object... with " + str)
	      fromJson(str) match {
	     
	       case Some(person) => {
	         create(person) 
	         Some(person)
	       }
	       case None => None
	     } 	 
	     }
	     RequestHandler.defineHandler(crudDefaultHandler(Seq("api"):+"person"))
	     RequestHandler.defineHandler(AsyncHandler.registerHandler(Seq("api"):+"async", Get, asyncHandler))
	  
	}
	

}

	

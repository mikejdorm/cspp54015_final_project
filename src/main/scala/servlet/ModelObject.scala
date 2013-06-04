package servlet

/*
 * These helper classes are influenced by the Ruby on Rails' concept
 * of models and controllers that support the the convention of 
 * CRUD(Create, Read, Update, Delete). What I'm doing here
 * is first creating a trait CrudHelperClass that is an interface
 * for a single object model. Next the CrudHelperObject stores the
 * instances of the instantiated object models and performs the controller
 * duties for the model by implementing the create, read, delete, and
 * update functions for the model. An example of how to use these
 * traits is included in the ServiceModel example. 
 * 
 * The most important function in this class is the crudDefaultHandler. 
 * This defines the partial function that is then used by the request
 * handler for matching a specific URI in the service. The URI prefix
 * is defined within the implementation of the service model class. An
 * example of how it is defined is also included in the ServiceModel
 * example. The chaining of these functions was kind of tricky but it makes
 * the implementation by the user of the library much easier as can be
 * seen in the example.
 * 
 */

/*
 * To be used by the implementation of the model
 */
trait CrudHelperClass{
  var id:Int = 0
  def _id:Int = id
  //override def toJson:String = id.toString
}

/*
 * To be used by a companion object of the model 
 * implementation in order to provide the necessary
 * handlers and CRUD actions. 
 */
trait CrudHelperObject[X <: CrudHelperClass] {
  
//holds the individual instantiated model classes
private var items: Map[Int,X] = Map()

/*
 * delete an object
 */
def delete(x:X):Option[X] = this.delete(x._id)

def delete(id:String):Option[X] = 
  try{this.delete(id.toInt)}
  catch{
  case nfe: NumberFormatException => None
  }

def delete(id:Int):Option[X] = {
  if(items.contains(id)){
	  val deletion = items(id)
	  items.-(id)
	  Some(deletion)
  }
  else
    None
}

/*
 * create an object
 */
def create(x:X) = {
  x.id = items.size
  items.+=(x._id->x)
}
def create(str:String):Option[X] = {
   None
}

/*
 * update an existing object
 */
def update(str:String):Option[X] = {
  None
}
def update(id:Int, x:X):Option[X] = {
		items.get(id).map(f => x)
}
/*
 * read and return an existing object
 */
def read(element:X):Option[X] = {
  System.out.println("reading: (element:X)" + element)
  items.get(element._id)
} 
def read(id:Int):Option[X] = {
    System.out.println("reading (id:Int): for object: " 
        +  items.get(id))
  items.get(id)
}
def read(id:String):Option[X] = {
    System.out.println("reading: (id:String)" + id)
  try{ read(id.toInt) }
  catch{ case nfe: NumberFormatException => None }
}
def read():List[X] = items.values.toList

/*
 * Create the handlers 
 */
def crudDefaultHandler(Prefix:Seq[String]): 
  PartialFunction[HttpRequest, Option[HttpResponse]] = {
		 case a: HttpRequest => a.context match{
			    case HttpContext(Prefix:+x, Delete) => wrapResponse(delete(x))
			    case HttpContext(Prefix:+x, Get) => wrapResponse(read(x))
			    case HttpContext(Prefix, Get) => wrapResponse(read())
			    case HttpContext(Prefix:+y, Post) => wrapResponse(create(y))
			    case HttpContext(Prefix:+y, Put) => wrapResponse(update(y))
			    case _ => wrapResponse(None)
		 }
		 case _ => { println("Error in the handler")
			 		Some(new HttpErrorResponse("Item not found")) 
		 }
		 
    }
/*
 * Wrap a list of models in an http response object
 */
def wrapResponse(elems:List[X]):Option[HttpResponse] = {
   if(items.size>0) 
     Some(new HttpOkResponse(JsonUtils.anyToJValue(elems).toString))
   else 
	  Some(new  HttpErrorResponse("No items exist") )
}
/*
 * wrap a model within an http response object
 */
def wrapResponse(opt:Option[X]):Option[HttpResponse] = {
    println("Wrapping response: " + opt)
	  opt match{
	  case Some(x) => Some(new HttpOkResponse(JsonUtils.anyToJValue(x).toString))
	  case None => Some(new HttpErrorResponse("Item not found"))
	}
}
}


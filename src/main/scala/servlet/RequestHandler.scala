package servlet
import javax.servlet.http.HttpServletResponse._

/*
 * This object is called by the 
 */
/*object RequestHandler{
    type Handler = PartialFunction[HttpRequest, () => Option[HttpResponse]]
    @volatile private var handlers: List[Handler] = Nil
    def defineHandler(handler:Handler) = handlers ::=handler
    def getNumberOfHandlers:Int = handlers.size
    def getHandlers:List[Handler] = handlers
    def dispatch(req:HttpRequest) = {
      handlers.foreach(h => {
    	  val e = h.apply(req) 
	    	  val re =  e.apply match{
	    	  	case Some(x) => x match{ 
		    	  	  case a: HttpAcceptProcessingResponse => {
		    	  	     req.res.setStatus(SC_ACCEPTED)
		    	  	     req.res.getOutputStream().print(a.body)
		    	  	  }
		    	  	  case c: HttpCreatedResponse => {
		    	  	     req.res.setStatus(SC_CREATED)
		    	  	     req.res.getOutputStream().print(c.body)
		    	  	  }
		    	  	  case o: HttpOkResponse => { 
		    	  	      req.res.setStatus(SC_OK)
		    	  	      req.res.getOutputStream().print(o.body)
		    	  	  }
		    	  	  case _ =>
		    	  	}
		    	  	case None => {
		    	  	   req.res.setStatus(SC_BAD_REQUEST)
		    	  	   req.res.getOutputStream().print("Invalid Request")
		    	  	}
		    	  }
	    	  }
    	)
    }
    
}*/

object RequestHandler{
    type Handler = PartialFunction[HttpRequest, Option[HttpResponse]]
    @volatile private var handlers: List[Handler] = Nil
    def defineHandler(handler:Handler) = handlers ::=handler
    def getNumberOfHandlers:Int = handlers.size
    def getHandlers:List[Handler] = handlers
    def dispatch(req:HttpRequest) = {
      handlers.foreach(h => 
    	  		h.apply(req) match{
	    	  		case Some(x) => x match{ 
		    	  	  case a: HttpAcceptProcessingResponse => {
		    	  	     req.res.setStatus(SC_ACCEPTED)
		    	  	     req.res.getOutputStream().print(a.body)
		    	  	  }
		    	  	  case c: HttpCreatedResponse => {
		    	  	     req.res.setStatus(SC_CREATED)
		    	  	     req.res.getOutputStream().print(c.body)
		    	  	  }
		    	  	  case o: HttpOkResponse => { 
		    	  	      req.res.setStatus(SC_OK)
		    	  	      req.res.getOutputStream().print(o.body)
		    	  	  }
		    	  	  case _ =>
		    	  	}
		    	  	case None => {
		    	  	   req.res.setStatus(SC_BAD_REQUEST)
		    	  	   req.res.getOutputStream().print("Invalid Request")
		    	  	}
    	  		}
    		  )
      }
}

package servlet
import javax.servlet.http._
import javax.servlet.{ AsyncEvent, AsyncListener }
import example._
 
/*
 * Servlet instance receives 
 */
class Servlet extends HttpServlet {

	  override def service(req: HttpServletRequest, resp: HttpServletResponse) ={

			ServiceExample.Person
			System.out.println("Handlers" + RequestHandler.getNumberOfHandlers)
			val request = new HttpRequest(req,resp)
			RequestHandler.dispatch(request)
	  }
}

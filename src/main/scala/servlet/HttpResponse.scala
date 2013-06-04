package servlet
import javax.servlet.http._

trait HttpResponse{
   val body:String
}
class HttpAcceptProcessingResponse(override val body:String) extends HttpResponse
class HttpCreatedResponse(override val body:String) extends HttpResponse
class HttpErrorResponse(override val body:String) extends HttpResponse
class HttpOkResponse(override val body:String) extends HttpResponse
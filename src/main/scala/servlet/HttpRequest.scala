package servlet
import javax.servlet.http._

trait Request{
  val req:HttpServletRequest
  val res:HttpServletResponse
  def pathTranslation = req.getPathTranslated()
  def pathInfo = req.getPathInfo()
  def requestType = req.getAuthType()
  def method = req.getMethod()
  val context:HttpContext =new HttpContext(splitPath(req.getRequestURI()), getHttpMethod(req.getMethod()))
  
  def getHttpMethod(method:String):HttpRequestType = method.toUpperCase() match{
    case "GET" =>  Get
    case "PUT" =>  Put
    case "POST" =>  Post
    case "DELETE" =>  Delete
    case _ =>  Unknown
  }
  def splitPath(path:String):List[String] = path.split("/").filter(_ != "").toList
}

class HttpRequest(val req:HttpServletRequest, val res:HttpServletResponse) extends Request

case class HttpContext(val path:List[String], val requestType:HttpRequestType){
  println(path + " " + requestType)
}
trait  HttpRequestType
object Get extends HttpRequestType
object Put extends HttpRequestType
object Post extends HttpRequestType
object Delete extends HttpRequestType
object Unknown extends HttpRequestType
object Header extends HttpRequestType
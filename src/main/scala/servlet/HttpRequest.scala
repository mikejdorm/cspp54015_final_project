package servlet
import javax.servlet.http._
import scala.io._

trait Request{
  val req:HttpServletRequest
  val res:HttpServletResponse
  def pathTranslation = req.getPathTranslated()
  def pathInfo = req.getPathInfo()
  def requestType = req.getAuthType()
  def method = req.getMethod()
  def body:String = 
    Source.fromInputStream(req.getInputStream()).getLines().mkString("\n")
  
  val context:HttpContext =new HttpContext(splitPath(req.getRequestURI()), getHttpMethod(req.getMethod()))
  
  def getHttpMethod(method:String):HttpRequestType = method.toUpperCase() match{
    case "GET" =>  Get
    case "PUT" =>  Put
    case "POST" =>  Post
    case "DELETE" =>  Delete
    case _ =>  Unknown
  }
  def splitPath(path:String):List[String] = path.split("/").filter(_ != "").toList
  override def toString:String = pathInfo + " " + method
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
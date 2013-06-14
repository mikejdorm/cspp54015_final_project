package servlet

object Publisher {
  
  def publish(req:HttpRequest, http:Option[HttpResponse]) = http match{
    case Some(a) => req.res.getOutputStream().print(a.body)
    case None => println("Error")
  }
  
} 
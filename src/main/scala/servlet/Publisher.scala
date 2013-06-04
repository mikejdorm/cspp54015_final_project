package servlet

object Publisher {

  def publish(req:HttpRequest, http:HttpResponse) = {
    
    req.res.getOutputStream().print(http.body)
    
  }
  
} 
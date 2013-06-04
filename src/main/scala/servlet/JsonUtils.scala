package servlet
import org.json4s._
import org.json4s.native.JsonMethods._

trait JsonSerializable{
	def toJson:String = JsonUtils.anyToJValue(this)
}

object JsonUtils{
  def anyToJValue(in: Any): String = {
    implicit def formats = DefaultFormats
    compact(render(Extraction.decompose(in)))
  }
  
}


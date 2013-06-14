package servlet
import example._
import org.json4s._
import org.json4s.jackson.JsonMethods._

trait JsonSerializable[X]{
  implicit val formats = DefaultFormats // Brings in default date formats etc.
	def toJson:String = JsonUtils.anyToJValue(this)
	def fromJson(str:String):Option[X]
}

object JsonUtils{
  implicit val formats = DefaultFormats // Brings in default date formats etc.

  def anyToJValue(in: Any): String = {
    implicit def formats = DefaultFormats
    compact(render(Extraction.decompose(in)))
  }
  
}


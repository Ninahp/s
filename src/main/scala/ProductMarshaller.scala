import spray.json.DefaultJsonProtocol

trait ProductMarshaller extends DefaultJsonProtocol{
  import Orderer._
  implicit val order = jsonFormat2(ProductOrder)

}
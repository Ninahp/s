import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.{Await, Future}
import scala.concurrent.duration._
import akka.actor.ActorSystem
import akka.http.scaladsl.model.{FormData, HttpRequest, HttpResponse}
import akka.http.scaladsl.unmarshalling.Unmarshal
import akka.stream.ActorMaterializer

trait TestHttp {
  implicit protected val system: ActorSystem
  final implicit val materializer = ActorMaterializer()

  def getFormField(data: FormData, field: String): Option[String] = {
    data.fields.find(_._1 == field) match {
      case Some(x) => Some(x._2)
      case None    => None
    }
  }

  def getHttpResponse(req: HttpRequest) = {
    val dataFut = Unmarshal(req.entity).to[String]
    getHttpResponseImpl(Await.result(dataFut, 1.second))
  }

  def getHttpResponseImpl(data: String): ATHttpClientResponse
}

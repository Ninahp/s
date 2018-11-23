import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.{Await, Future}
import scala.concurrent.duration._
import akka.actor.ActorSystem
import akka.http.scaladsl.model.{FormData, HttpRequest, HttpResponse}
import akka.http.scaladsl.unmarshalling.Unmarshal
import akka.stream.ActorMaterializer
import com.africasTalking.elmer.core.Orderer.ProductResponse


trait TestHttp {
  implicit protected val system: ActorSystem
  final implicit val materializer = ActorMaterializer()

  def getHttpResponse(req: HttpRequest) = {
    val dataFut = Unmarshal(req.entity).to[String]
    getHttpResponseImpl(Await.result(dataFut, 1.second))
  }

  def getHttpResponseImpl(data: String): ProductResponse
}

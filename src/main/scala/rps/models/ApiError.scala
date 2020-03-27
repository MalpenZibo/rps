package rps.models

import akka.http.scaladsl.model.{HttpResponse, StatusCodes}
import wiro.server.akkaHttp.ToHttpResponse
import scala.concurrent.Future
import scala.concurrent.ExecutionContext

trait ApiError extends Throwable {
  val msg: String
}

object ApiErrors {
  case class InternalError(msg: String) extends ApiError
  case class NotFound(msg: String = "") extends ApiError

  def someOrNotFound[R](f: Future[Either[ApiError, Option[R]]])(
    implicit ec: ExecutionContext
  ): Future[Either[ApiError, R]] = 
    f.map(e => e.flatMap(_.toRight(NotFound())))
    //f.map(e => e.right.flatMap(v => Either.cond(v.isDefined, v.get, NotFound())))

  implicit def apiError: ToHttpResponse[ApiError] =
    error =>
      HttpResponse(
        status = error match {
          case InternalError(msg) => StatusCodes.InternalServerError
          case NotFound(msg) => StatusCodes.NotFound
        },
        entity = error.msg
      )
}
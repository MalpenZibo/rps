package rps.models

import akka.http.scaladsl.model.{HttpResponse, StatusCodes}
import wiro.server.akkaHttp.ToHttpResponse

trait ApiError extends Throwable {
  val msg: String
}

object ApiErrors {
  case class InternalError(msg: String) extends ApiError
  case class NotFound(msg: String = "") extends ApiError

  implicit def apiError: ToHttpResponse[ApiError] =
    error =>
      HttpResponse(
        status = error match {
          case InternalError(msg) => StatusCodes.InternalServerError
          case NotFound(msg) => StatusCodes.NotFound
        },
        entity = {
          println(error.msg)
          error.msg
        }
      )
}
package rps.models

import wiro.server.akkaHttp.ToHttpResponse
import akka.http.scaladsl.model.{HttpResponse, StatusCodes}

case class NotFoundError(msg: String)

object Errors {
  implicit def throwableResponse: ToHttpResponse[Throwable] = { exc =>
    HttpResponse(
      status = StatusCodes.InternalServerError,
      entity = exc.toString
    )
  }
  
  implicit def notFoundResponse: ToHttpResponse[NotFoundError] = { msg =>
    HttpResponse(
      status = StatusCodes.NotFound,
      entity = msg.toString
    )
  }
}
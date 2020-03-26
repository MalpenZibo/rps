package rps.repositories

import scala.concurrent.Future
import scala.concurrent.ExecutionContext

import rps.models.ApiError
import rps.models.ApiErrors._

trait SlickRepository {
  def futureToApiResult[R](f: Future[Option[R]])(implicit ec: ExecutionContext): Future[Either[ApiError, R]] = {
    f.map { Right(_) }.recover { case e => Left(e) }.map( 
      slickValue => slickValue match {
        case Right(Some(r)) => Right(r)
        case Right(None) => Left(NotFound())
        case Left(exc: Throwable) => Left(InternalError(exc.toString))
      }
    )
  }    
}
package rps.repositories

import scala.concurrent.Future
import scala.concurrent.ExecutionContext
import rps.models.ApiError
import rps.models.ApiErrors.InternalError

trait SlickRepository {
  def futureToEither[R](f: Future[R])(implicit ec: ExecutionContext): Future[Either[ApiError, R]] = {
    f.map { Right(_) }.recover { case e => Left(InternalError(e.toString)) }
  }    
}
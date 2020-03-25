package rps.controllers

import scala.concurrent.Future
import wiro.annotation._

import rps.dtos._
import rps.services.GameService
import rps.models.Move

@path("rps")
trait GameController {
  @command
  def play(userMove: Move): Future[Either[Throwable, GameResponse]]
}

class GameControllerImpl() extends GameController {
  import scala.concurrent.ExecutionContext.Implicits.global

  override def play(userMove: Move): Future[Either[Throwable, GameResponse]] = 
    Future(Right(GameResponse.tupled(GameService.play(userMove))))
}

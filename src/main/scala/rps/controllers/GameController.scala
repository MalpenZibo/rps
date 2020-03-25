package rps.controllers

import scala.concurrent.ExecutionContext
import scala.concurrent.Future
import wiro.annotation._

import rps.services.GameService
import rps.models._

@path("rps")
trait GameController {
  @query
  def result(): Future[Either[Throwable, Option[GameResult]]]

  @command
  def play(userMove: Move): Future[Either[Throwable, Unit]]
}

class GameControllerImpl(gameService: GameService)(implicit ec: ExecutionContext) extends GameController {
  override def result(): Future[Either[Throwable, Option[GameResult]]] = {
    println(gameService.getGameResult)
    Future(Right(gameService.getGameResult))
  }

  override def play(userMove: Move): Future[Either[Throwable, Unit]] = {
    Future(Right(gameService.playMove(userMove)))
  }
}

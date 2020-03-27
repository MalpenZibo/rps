package rps.controllers

import java.util.UUID

import scala.concurrent.{ExecutionContext, Future}
import wiro.annotation._

import rps.services.GameService
import rps.models._

@path("rps")
trait GameController {
  @query
  def result(): Future[Either[ApiError, Game]]

  @command
  def play(userMove: Move): Future[Either[ApiError, UUID]]
}

class GameControllerImpl(gameService: GameService)(implicit ec: ExecutionContext) extends GameController {
  override def result(): Future[Either[ApiError, Game]] = ApiErrors.someOrNotFound(gameService.getGameResult)

  override def play(userMove: Move): Future[Either[ApiError, UUID]] = gameService.playMove(userMove)
}

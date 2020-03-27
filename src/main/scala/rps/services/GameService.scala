package rps.services

import java.util.UUID
import java.time.Instant

import scala.concurrent.Future
import scala.util.Random
import scala.concurrent.ExecutionContext

import rps.models._
import rps.models.Result._
import rps.models.Move._

import rps.repositories.GameRepository

trait GameService {
  def getGameResult(): Future[Either[ApiError, Game]]
  def playMove(move: Move): Future[Either[ApiError, UUID]]
}

class GameServiceImpl(gameRepository: GameRepository)(
  implicit ec: ExecutionContext
) extends GameService {
  def getGameResult(): Future[Either[ApiError, Game]] = ApiErrors.someOrNotFound(gameRepository.getGame)
  
  def playMove(userMove: Move): Future[Either[ApiError, UUID]] = {
    val computerMove = getRandomMove
    val result = getResult(userMove, computerMove)

    return gameRepository.saveGame(Game(UUID.randomUUID, userMove, computerMove, result, Instant.now()))
  }

  private def getResult(userMove: Move, computerMove: Move): Result = 
    (userMove, computerMove) match {
      case (x, y) if x == y => Draw
      case (Rock, Scissors) | (Paper, Rock) | (Scissors, Paper) => Win
      case _ => Lose
  }

  private def getRandomMove(): Move =
    Random.shuffle(List(Rock, Paper, Scissors)).head
}
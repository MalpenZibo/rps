package rps.services

import java.util.UUID
import java.time.Instant

import scala.concurrent.Future

import rps.models._
import rps.models.Result._
import rps.models.Move._

import rps.repositories.GameRepository

trait GameService {
  def getGameResult(): Future[Either[ApiError, Option[Game]]]
  def playMove(move: Move): Future[Either[ApiError, UUID]]
}

class GameServiceImpl(gameRepository: GameRepository, moveGenerator: MoveGenerator) extends GameService {
  def getGameResult(): Future[Either[ApiError, Option[Game]]] = gameRepository.getGame
  
  def playMove(userMove: Move): Future[Either[ApiError, UUID]] = {
    val computerMove = moveGenerator.getMove
    val result = getResult(userMove, computerMove)

    gameRepository.saveGame(Game(UUID.randomUUID, userMove, computerMove, result, Instant.now()))
  }

  private def getResult(userMove: Move, computerMove: Move): Result = 
    (userMove, computerMove) match {
      case (x, y) if x == y => Draw
      case (Rock, Scissors) | (Paper, Rock) | (Scissors, Paper) => Win
      case _ => Lose
  }
}
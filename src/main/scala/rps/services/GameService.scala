package rps.services

import scala.util.Random

import io.buildo.enumero.{CaseEnumIndex, CaseEnumSerialization}
import rps.models._
import rps.models.Result._
import rps.models.Move._

import rps.services._
import rps.repositories.GameRepository

trait GameService {
  def getGameResult(): Option[GameResult]
  def playMove(move: Move): Unit
}

class GameServiceImpl(gameRepository: GameRepository) extends GameService {
  def getGameResult(): Option[GameResult] = gameRepository.getGame
  
  def playMove(userMove: Move): Unit = {
    val enemyMove = getRandomMove
    gameRepository.saveGame(
      GameResult.tupled(
        (
          userMove, 
          enemyMove, 
          (userMove, enemyMove) match {
            case (x, y) if x == y => Draw
            case (Rock, Scissors) | (Paper, Rock) | (Scissors, Paper) => Win
            case _ => Lose
          }
        )
      )
    )
  }

  private def getRandomMove(): Move =
    Random.shuffle(List(Rock, Paper, Scissors)).head
}
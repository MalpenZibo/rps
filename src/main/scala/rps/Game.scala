package rps

import scala.util.Random

import io.buildo.enumero.{CaseEnumIndex, CaseEnumSerialization}
import rps.models._
import rps.models.Result._
import rps.models.Move._

object Game {
  def play(userMove: Move): (Move, Move, Result) = {
    val enemyMove = getRandomMove
    val result = getResult(userMove, enemyMove)

    (userMove, enemyMove, result)
  }

  private def getResult(userMove: Move, enemyMove: Move) = 
    (userMove, enemyMove) match {
      case (x, y) if x == y => Draw
      case (Rock, Scissors) | (Paper, Rock) | (Scissors, Paper) => Win
      case _ => Lose
    }

  private def getRandomMove(): Move =
    Random.shuffle(List(Rock, Paper, Scissors)).head
}

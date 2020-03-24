package rps

import scala.util.Random
import scala.util.Try
import scala.util.Success
import scala.util.Failure
import rps.Move.Scissors
import rps.Move.Rock
import rps.Move.Paper

import io.buildo.enumero.{CaseEnumIndex, CaseEnumSerialization}

object Game {
  def play(): Unit = {
    CaseEnumIndex[Move].caseFromIndex(readLine("make your move (0: rock, 1: paper, 2: scissors) ")) match {
      case Some(userMove) => {
        val enemyMove = getRandomMove

        println(s"Your move ${CaseEnumSerialization[Move].caseToString(userMove)}, Computer move ${CaseEnumSerialization[Move].caseToString(enemyMove)}")

        (userMove, enemyMove) match {
          case (x, y) if x == y => println("It's a draw")
          case (Rock, Scissors) | (Paper, Rock) | (Scissors, Paper) => println("you win")
          case _ => println("you lose")
        }
      }
      case None => println("wrong selection")
    }
  }

  private def getRandomMove(): Move =
    Random.shuffle(List(Rock, Paper, Scissors)).head
}

package rps

import scala.util.Random
import scala.util.Try
import scala.util.Success
import scala.util.Failure

object Game {
  private val moves = Array("rock", "paper", "scissors")

  def play(): Unit = {
    val rnd = Random

    var input = readLine("make your move (0: rock, 1: paper, 2: scissors) ")

    val userMove = Try(input.toInt) match {
      case Success(decoded) if (decoded >= 0 && decoded < 3) => {
        decoded
      }
      case _ => {
        println("wrong selection")
        return
      }
    }
    val enemyMove = rnd.nextInt(3)

    println(s"Your move ${moves(userMove)}, Computer move ${moves(enemyMove)}")

    (userMove, enemyMove) match {
      case (x, y) if x == y => println("It's a draw")
      case (x, y) if moves((enemyMove + 1) % 3) == moves(userMove) => println("you win")
      case _ => println("you lose")
    }
  }
}
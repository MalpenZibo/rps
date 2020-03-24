package rps

import scala.util.Random
import scala.util.Try
import scala.util.Success
import scala.util.Failure

object Game {
  def play(): Unit = {
    val rnd = Random

    Try(readLine("make your move (0: rock, 1: paper, 2: scissors) ").toInt) match {
      case Success(userMove) => {
        val enemyMove = rnd.nextInt(3)

        println(s"Your move $userMove, Computer move $enemyMove")

        if (userMove == enemyMove) {
          println("even")
        } else if (((enemyMove + 1) % 3) == userMove) {
          println("you win")
        } else {
         println("you lose")
        }
      }
      case _ => {
        println("wrong selection")
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

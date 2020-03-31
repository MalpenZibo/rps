package rps.services

import rps.models._
import scala.util.Random
import rps.models.Move._

trait MoveGenerator {
  def getMove(): Move
}

class RandomMoveGenerator() extends MoveGenerator {
  def getMove(): Move =
    Random.shuffle(List(Rock, Paper, Scissors)).head
}

class StaticMoveGenerator(move: Move) extends MoveGenerator {
  def getMove(): Move = move
}
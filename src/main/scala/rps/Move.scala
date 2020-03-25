package rps

import scala.util.Random

sealed trait Move
object Move {
  case object Rock extends Move
  case object Paper extends Move
  case object Scissors extends Move

  def read(s: String): Option[Move] = s match {
    case "0" => Some(Rock)
    case "1" => Some(Paper)
    case "2" => Some(Scissors)
    case _ => None
  }

  def print(m: Move) = m match {
    case Rock => "rock"
    case Paper => "paper"
    case Scissors => "scissors"
  }

  // Bad random generation? Why??
  def getRandomMove(): Move =
    Random.shuffle(List(Rock, Paper, Scissors)).head
}
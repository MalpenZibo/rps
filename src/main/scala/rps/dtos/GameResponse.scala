package rps.dtos

import wiro.annotation._
import rps.models._

case class GameResponse(userMove: Move, computerMove: Move, result: Result)
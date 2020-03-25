package rps.dtos

import rps.models._

case class PlayResponse(userMove: Move, computerMove: Move, result: Result)
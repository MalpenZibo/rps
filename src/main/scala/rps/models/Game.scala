package rps.models

import java.util.UUID
import java.sql.Timestamp

case class Game(
  id: UUID,
  userMove: Move, 
  computerMove: Move, 
  result: Result
)
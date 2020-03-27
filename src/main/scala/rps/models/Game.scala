package rps.models

import java.util.UUID
import java.time.Instant

case class Game(
  id: UUID,
  userMove: Move, 
  computerMove: Move, 
  result: Result,
  createdAt: Instant
)
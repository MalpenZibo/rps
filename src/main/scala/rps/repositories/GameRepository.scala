package rps.repositories

import rps.models._
import rps.services.GameService
import java.util.concurrent.atomic.AtomicReference

trait GameRepository {
  def getGame(): Option[GameResult]
  def saveGame(game: GameResult): Unit
}

class GameRepositoryImpl extends GameRepository {
  private val gameStore: AtomicReference[Option[GameResult]] = new AtomicReference[Option[GameResult]](None)

  def getGame(): Option[GameResult] = gameStore.get()

  def saveGame(game: GameResult): Unit = 
    gameStore.set(Some(game))  
}
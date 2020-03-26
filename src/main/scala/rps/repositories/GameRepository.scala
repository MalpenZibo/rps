package rps.repositories

import scala.concurrent.{ExecutionContext, Future}
import java.util.UUID
import java.sql.Timestamp

import slick.driver.H2Driver.backend.DatabaseDef
import slick.driver.H2Driver.api._

import rps.db.Tables.{Games, GameRow}
import rps.models._

trait GameRepository {
  def getGame(): Future[Either[ApiError, Game]]
  def saveGame(game: Game): Future[Either[ApiError, UUID]]
}

class GameRepositoryImpl(
  db: DatabaseDef
)(
  implicit ec: ExecutionContext
) extends GameRepository with SlickRepository {

  def getGame(): Future[Either[ApiError, Game]] = {   
    
    val game = db.run(Games.sortBy(_.createdAt.desc).take(1).result.headOption).map(convertGameRow)

    return futureToApiResult(game)
  } 

  def saveGame(game: Game): Future[Either[ApiError, UUID]] = {
    val newGame = Games += convertGame(game)
    
    return futureToApiResult(db.run(newGame).map(_ => Some(game.id)))
  }

  private val convertGameRow = (r: Option[GameRow]) => r match {
    case Some(r) =>
      (Move.caseFromString(r.userMove), Move.caseFromString(r.computerMove), Result.caseFromString(r.result)) match {
        case (Some(userMove), Some(computerMove), Some(result)) => Some(Game(r.id, userMove, computerMove, result, r.createdAt.toInstant))
        case _ => None
      }
    case None => None 
  }

  private def convertGame(game: Game): GameRow =
    GameRow(
      game.id,
      Move.caseToString(game.userMove),
      Move.caseToString(game.computerMove),
      Result.caseToString(game.result),
      Timestamp.from(game.createdAt)
    )
}
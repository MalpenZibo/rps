package rps.repositories

import scala.concurrent.{ExecutionContext, Future}
import java.util.UUID

import slick.driver.H2Driver.backend.DatabaseDef
import slick.driver.H2Driver.api._

import rps.db.Tables.Games
import rps.models._
import rps.services.GameService
import java.sql.Timestamp

trait GameRepository {
  def getGame(): Future[Either[ApiError, Game]]
  def saveGame(userMove: Move, enemyMove: Move, result: Result): Future[Either[ApiError, UUID]]
}

class GameRepositoryImpl(
  db: DatabaseDef
)(
  implicit ec: ExecutionContext
) extends GameRepository with SlickRepository {

  def getGame(): Future[Either[ApiError, Game]] = {    
    val game = db.run(Games.sortBy(_.createdAt.desc).take(1).result.headOption).map(
      {
        case Some((id, userMove, computerMove, result, createdAt)) =>
          (Move.caseFromString(userMove), Move.caseFromString(computerMove), Result.caseFromString(result)) match {
            case (Some(userMove), Some(computerMove), Some(result)) => Some(Game(id, userMove, computerMove, result))
            case _ => None
          }
        case None => None
      }
    )

    return futureToApiResult(game)
  } 

  def saveGame(userMove: Move, computerMove: Move, result: Result): Future[Either[ApiError, UUID]] = {
    val id = UUID.randomUUID
    val timestamp = new Timestamp(System.currentTimeMillis)

    val newGame = Games += (
      id, 
      Move.caseToString(userMove), 
      Move.caseToString(computerMove), 
      Result.caseToString(result), 
      timestamp
    )
    
    return futureToApiResult(db.run(newGame).map(_ => Some(id)))
  }
}
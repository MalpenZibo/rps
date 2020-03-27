package rps.repositories

import scala.concurrent.{ExecutionContext, Future}
import java.util.UUID
import java.sql.Timestamp

import slick.driver.H2Driver.backend.DatabaseDef
import slick.driver.H2Driver.api._

import rps.db.Tables.{Games, GameRow}
import rps.models._

trait GameRepository {
  def getGame(): Future[Either[ApiError, Option[Game]]]
  def saveGame(game: Game): Future[Either[ApiError, UUID]]
}

class GameRepositoryImpl(
  db: DatabaseDef
)(
  implicit ec: ExecutionContext
) extends GameRepository with SlickRepository {

  def getGame(): Future[Either[ApiError, Option[Game]]] = { 
    val game = db.run(Games.sortBy(_.createdAt.desc).take(1).result.headOption).map(_.flatMap(convertGameRow))

    futureToEither(game)
  } 

  def saveGame(game: Game): Future[Either[ApiError, UUID]] = {
    val newGame = Games += convertGame(game)
    
    futureToEither(db.run(newGame).map(_ => game.id))
  }

  private val convertGameRow = (r: GameRow) => for {
    userMove <- Move.caseFromString(r.userMove)
    computerMove <- Move.caseFromString(r.computerMove)
    result <- Result.caseFromString(r.result)
  } yield Game(r.id, userMove, computerMove, result, r.createdAt.toInstant)

  private def convertGame(game: Game): GameRow =
    GameRow(
      game.id,
      Move.caseToString(game.userMove),
      Move.caseToString(game.computerMove),
      Result.caseToString(game.result),
      Timestamp.from(game.createdAt)
    )
}
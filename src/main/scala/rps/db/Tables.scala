package rps.db

import slick.driver.H2Driver.api._
import java.sql.Timestamp
import slick.sql.SqlProfile.ColumnOption.SqlType
import java.util.UUID
import rps.models._

object Tables {
  case class GameRow(
    id: UUID,
    userMove: String, 
    computerMove: String, 
    result: String,
    createdAt: Timestamp
  )

  class Game(tag: Tag)
    extends Table[GameRow](tag, "GAME") {
  
    def id = column[java.util.UUID]("GAME_ID", O.PrimaryKey)
    def userMove = column[String]("USER_MOVE")
    def computerMove = column[String]("ENEMY_MOVE")
    def result = column[String]("RESULT")
    def createdAt = column[Timestamp]("CREATED_AT")
    
    def * = (id, userMove, computerMove, result, createdAt) <> (GameRow.tupled, GameRow.unapply)
  }
  
  lazy val Games = new TableQuery(tag => new Game(tag))

}
package rps.db

import slick.driver.H2Driver.api._
import java.sql.Timestamp
import slick.sql.SqlProfile.ColumnOption.SqlType

object Tables {
  class Game(tag: Tag)
    extends Table[(java.util.UUID, String, String, String, Timestamp)](tag, "GAME") {
  
    def id = column[java.util.UUID]("GAME_ID", O.PrimaryKey)
    def userMove = column[String]("USER_MOVE")
    def computerMove = column[String]("ENEMY_MOVE")
    def result = column[String]("RESULT")
    def createdAt = column[Timestamp]("CREATED_AT")
    
    def * = (id, userMove, computerMove, result, createdAt)
  }
  
  lazy val Games = new TableQuery(tag => new Game(tag))

}
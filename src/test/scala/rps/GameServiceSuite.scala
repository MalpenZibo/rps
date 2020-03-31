package rps

import org.scalatest.AsyncFunSpec
import org.scalatest.BeforeAndAfterEach
import org.scalatest.BeforeAndAfterAll
import rps.db.AppDbContext

import rps.db.AppDbContext
import scala.concurrent.Await
import scala.concurrent.duration.Duration
import rps.repositories._
import rps.services.GameServiceImpl
import rps.models._

import org.scalatest._
import rps.services.StaticMoveGenerator

class GameServiceSuite 
  extends AsyncFunSpec
  with BeforeAndAfterEach
  with BeforeAndAfterAll
  with OptionValues
  with EitherValues
  with Matchers {

  val db = AppDbContext.getDBRef("h2mem1")
  val gameRepository = new GameRepositoryImpl(db)

  override def beforeEach(): Unit = {
    Await.result(AppDbContext.dropSchema(db), Duration.Inf)
    Await.result(AppDbContext.createSchema(db), Duration.Inf)
  }

  override def afterAll(): Unit = db.close

  describe("GameService") {
    val moveGenerator = new StaticMoveGenerator(Move.Rock)
    val gameService = new GameServiceImpl(gameRepository, moveGenerator)

    it("should not read a game before play a move") {
      (for {
        game <- gameService.getGameResult()
      } yield { 
        game shouldBe Right(None)
      })
    }

    it("should create a move") {
      (for {
        gameId <- gameService.playMove(Move.Paper)
      } yield { 
        gameId.right.value.toString should not be empty 
      })
    }

    it("should read a game after move creation") {
      (for {
        move <- gameService.playMove(Move.Paper)
        result <- gameService.getGameResult()
      } yield { result.right.value.value shouldBe a [Game]})
    }

    describe("should return the right result") {
      def checkUnit(move: Move, computerMove: Move, result: Result): Unit = {
        val gameService = new GameServiceImpl(gameRepository, new StaticMoveGenerator(computerMove))
        it(s"(${Move.caseToString(move)} vs ${Move.caseToString(computerMove)})") {
          (for {
            rock <- gameService.playMove(move)
            game <- gameService.getGameResult()
          } yield { game.right.value.value.result shouldBe result } )
        }
      }
      
      checkUnit(Move.Rock, Move.Rock, Result.Draw)
      checkUnit(Move.Rock, Move.Paper, Result.Lose)
      checkUnit(Move.Rock, Move.Scissors, Result.Win)

      checkUnit(Move.Paper, Move.Rock, Result.Win)
      checkUnit(Move.Paper, Move.Paper, Result.Draw)
      checkUnit(Move.Paper, Move.Scissors, Result.Lose)

      checkUnit(Move.Scissors, Move.Rock, Result.Lose)
      checkUnit(Move.Scissors, Move.Paper, Result.Win)
      checkUnit(Move.Scissors, Move.Scissors, Result.Draw)
    }
  }
}

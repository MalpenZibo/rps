package rps

import org.scalatest.Matchers
import org.scalatest.BeforeAndAfterEach
import org.scalatest.OptionValues
import org.scalatest.BeforeAndAfterAll
import org.scalatest.EitherValues
import org.scalatest.matchers.should.Matchers
import org.scalatest.funspec.AsyncFunSpec

import scala.concurrent.Await
import scala.concurrent.duration.Duration

import wiro.server.akkaHttp._
import wiro.server.akkaHttp.FailSupport._
import wiro.server.akkaHttp.RouterDerivationModule

import io.buildo.enumero.circe._
import io.circe.generic.auto._
import io.circe.syntax._
import de.heikoseeberger.akkahttpcirce.ErrorAccumulatingCirceSupport._ 

import akka.http.scaladsl.testkit.ScalatestRouteTest
import akka.http.scaladsl.server.Route
import akka.http.scaladsl.model._

import rps.db.AppDbContext
import rps.repositories.GameRepositoryImpl
import rps.services.StaticMoveGenerator
import rps.services.GameServiceImpl
import rps.controllers.{ GameController, GameControllerImpl }
import rps.models._
import rps.models.ApiErrors._

class GameControllerSuite 
  extends AsyncFunSpec
  with BeforeAndAfterEach
  with BeforeAndAfterAll
  with OptionValues
  with EitherValues
  with Matchers
  with ScalatestRouteTest 
  with RouterDerivationModule {
    
  val db = AppDbContext.getDBRef("h2mem1")
  val gameRepository = new GameRepositoryImpl(db)

  override def beforeEach(): Unit = {
    Await.result(AppDbContext.dropSchema(db), Duration.Inf)
    Await.result(AppDbContext.createSchema(db), Duration.Inf)
  }

  override def afterAll(): Unit = db.close

  val moveGenerator = new StaticMoveGenerator(Move.Rock)
  val gameService = new GameServiceImpl(gameRepository, moveGenerator)
  val route = deriveRouter[GameController](new GameControllerImpl(gameService)).buildRoute

  describe("GameController") {
    val moveGenerator = new StaticMoveGenerator(Move.Rock)
    val gameService = new GameServiceImpl(gameRepository, moveGenerator)

    /*it("should return 404 when trying to get a game before have played a move") {
      Get("/rps/result") ~> route ~> check {
        status should be (StatusCodes.NotFound)
      }
    }*/

    it("should return 200 when post a move") {
      case class Request(userMove: Move)
      val request = Request(Move.Rock)
      val entity = HttpEntity(MediaTypes.`application/json`, request.asJson.noSpaces)

      Post("/rps/play", entity) ~> Route.seal(route) ~> check {
        status should be (StatusCodes.OK)
        responseAs[String] should not be empty
      }
    }

    it("should return a 200 after post a move") {
      case class Request(userMove: Move)
      val request = Request(Move.Rock)
      val entity = HttpEntity(MediaTypes.`application/json`, request.asJson.noSpaces)

      Post("/rps/play", entity) ~> Route.seal(route) ~> check {
        status should be (StatusCodes.OK)
        responseAs[String] should not be empty

        Get("/rps/result") ~> route ~> check {
          status should be (StatusCodes.OK)
          responseAs[Game].result should be (Result.Draw)
        }
      }
    }
  }
}
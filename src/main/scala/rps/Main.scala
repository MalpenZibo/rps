package rps

import akka.actor.ActorSystem
import akka.stream.ActorMaterializer
import akka.http.scaladsl.model.{HttpResponse, StatusCodes}

import io.circe.generic.auto._
import io.buildo.enumero.circe._

import wiro.Config
import wiro.server.akkaHttp._
import wiro.server.akkaHttp.FailSupport._

// Use H2Driver to connect to an H2 database
import slick.driver.H2Driver.api._

import rps.models.ApiError
import rps.models.ApiErrors
import rps.models.ApiErrors._
import rps.controllers._
import rps.services._
import rps.repositories._
import rps.db.Tables
import scala.concurrent.Await
import scala.concurrent.duration.Duration

object Main extends App with RouterDerivationModule {
  implicit val system = ActorSystem("rps")
  implicit val materializer = ActorMaterializer()
  // needed for the future flatMap/onComplete in the end
  implicit val executionContext = system.dispatcher

  val gameDB = Database.forConfig("h2mem1")

  //db setup
  val setup = DBIO.seq(
    // Create the tables
    (Tables.Games.schema).create
  )
  val DBSetup = gameDB.run(setup)

  val gameRepository = new GameRepositoryImpl(gameDB)
  val gameService = new GameServiceImpl(gameRepository)
  val gameController = new GameControllerImpl(gameService)

  val gameRouter = deriveRouter[GameController](gameController)

  val rpcServer = new HttpRPCServer(
    config = Config("localhost", 8080),
    routers = List(gameRouter)
  ) 
}



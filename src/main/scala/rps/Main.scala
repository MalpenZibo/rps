package rps

import akka.actor.ActorSystem
import akka.stream.ActorMaterializer

import io.circe.generic.auto._
import io.buildo.enumero.circe._

import wiro.Config
import wiro.server.akkaHttp._
import wiro.server.akkaHttp.FailSupport._

import rps.models.ApiErrors._
import rps.controllers._
import rps.services._
import rps.repositories._
import rps.db.AppDbContext

object Main extends App with RouterDerivationModule {
  implicit val system = ActorSystem("rps")
  implicit val materializer = ActorMaterializer()
  implicit val executionContext = system.dispatcher

  val db = AppDbContext.getDBRef("h2mem1")

  AppDbContext.createSchema(db).map(_ => {
    val gameRepository = new GameRepositoryImpl(db)
    val moveGenerator = new RandomMoveGenerator()
    val gameService = new GameServiceImpl(gameRepository, moveGenerator)
    val gameController = new GameControllerImpl(gameService)
  
    val gameRouter = deriveRouter[GameController](gameController)
  
    val rpcServer = new HttpRPCServer(
      config = Config("0.0.0.0", 8080),
      routers = List(gameRouter)
    )
  })
}



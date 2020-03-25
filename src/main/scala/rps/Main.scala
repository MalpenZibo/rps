package rps

import akka.actor.ActorSystem
import akka.stream.ActorMaterializer
import akka.http.scaladsl.model.{HttpResponse, StatusCodes}

import io.circe.generic.auto._
import io.buildo.enumero.circe._

import wiro.Config
import wiro.server.akkaHttp._
import wiro.server.akkaHttp.FailSupport._

import rps.controllers._
import rps.services._
import rps.repositories._

object Main extends App with RouterDerivationModule {
  implicit val system = ActorSystem("rps")
  implicit val materializer = ActorMaterializer()
  // needed for the future flatMap/onComplete in the end
  implicit val executionContext = system.dispatcher

  implicit def throwableResponse: ToHttpResponse[Throwable] = { exc =>
    HttpResponse(
      status = StatusCodes.InternalServerError,
      entity = exc.toString
    )
  }

  val gameRepository = new GameRepositoryImpl()
  val gameService = new GameServiceImpl(gameRepository)
  val gameController = new GameControllerImpl(gameService)

  val gameRouter = deriveRouter[GameController](gameController)

  val rpcServer = new HttpRPCServer(
    config = Config("localhost", 8080),
    routers = List(gameRouter)
  )
}



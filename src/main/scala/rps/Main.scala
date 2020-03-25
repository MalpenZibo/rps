package rps

import akka.actor.ActorSystem
import akka.stream.ActorMaterializer

import io.circe.generic.auto._
import io.buildo.enumero.circe._

import wiro.Config
import wiro.server.akkaHttp._
import wiro.server.akkaHttp.FailSupport._

import rps.controllers.GameController
import rps.controllers.GameControllerImpl

import  rps.dtos._

object Main extends App with RouterDerivationModule {
  implicit val system = ActorSystem("rps")
  implicit val materializer = ActorMaterializer()
  // needed for the future flatMap/onComplete in the end
  implicit val executionContext = system.dispatcher

  implicit def throwableResponse: ToHttpResponse[Throwable] = null
  val gameRouter = deriveRouter[GameController](new GameControllerImpl)

  val rpcServer = new HttpRPCServer(
    config = Config("localhost", 8080),
    routers = List(gameRouter)
  )
}



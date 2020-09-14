package it.scalachikoro.client.views

import it.scalachikoro.client.views.stages.StartupStage
import scalafx.application.JFXApp

import scala.concurrent.{ExecutionContext, ExecutionContextExecutor}

class View(private val app:JFXApp) {
  /** Implicit executor */
  implicit val executionContextExecutor: ExecutionContextExecutor = ExecutionContext.global
  app.stage = StartupStage()
}

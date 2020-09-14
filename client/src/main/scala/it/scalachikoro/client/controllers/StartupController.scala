package it.scalachikoro.client.controllers

import it.scalachikoro.client.views.stages.StartupStage
import scalafx.application.{JFXApp, Platform}

class StartupController extends Controller {
  private var startUpStage: StartupStage = _

  override def start(app: JFXApp): Unit = {
    Platform.runLater({
      startUpStage = StartupStage()
      app.stage = startUpStage
    })
  }

  override def stop(): Unit = {}
}

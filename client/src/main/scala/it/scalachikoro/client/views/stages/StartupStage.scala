package it.scalachikoro.client.views.stages

import scalafx.application.JFXApp.PrimaryStage
import scalafx.beans.property.DoubleProperty
import scalafx.geometry.Insets
import scalafx.scene.Scene
import scalafx.scene.layout.{BorderPane, StackPane}

class StartupScene extends Scene() {
  val mainContent: BorderPane = new BorderPane()

  mainContent.prefWidth <== DoubleProperty(800)
  mainContent.maxHeight <== DoubleProperty(640)
  mainContent.setPadding(Insets(5))

  val rootContent = new StackPane()
  rootContent.getChildren.addAll(mainContent)
  root = rootContent
}

class BaseStage extends PrimaryStage{
  title = "Scala-chikoro"
  resizable = true
  width = 800
  height = 640
}

trait StartupStage extends BaseStage

class StartupStageImpl extends StartupStage {
  private val mainScene = new StartupScene()
  scene = mainScene
  onCloseRequest = _ => { System.exit(0) }
}

object StartupStage {
  def apply(): StartupStage = new StartupStageImpl()
}

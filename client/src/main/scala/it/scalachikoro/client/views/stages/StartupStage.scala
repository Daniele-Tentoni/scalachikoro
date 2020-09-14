package it.scalachikoro.client.views.stages

import scalafx.application.JFXApp.PrimaryStage
import scalafx.beans.property.DoubleProperty
import scalafx.geometry.{Insets, Pos}
import scalafx.scene.Scene
import scalafx.scene.control.Button
import scalafx.scene.layout.{BorderPane, StackPane, VBox}

class StartupScene extends Scene() {
  // TODO: Add a background.

  val btnPublicGame: Button = new Button("Join")

  val center: VBox = new VBox()
  center.alignment = Pos.Center
  center.spacing = 10
  center.setMaxWidth(400)
  center.getChildren.add(btnPublicGame)

  val mainContent: BorderPane = new BorderPane()
  mainContent.prefWidth <== DoubleProperty(800)
  mainContent.maxHeight <== DoubleProperty(640)
  mainContent.setPadding(Insets(5))
  mainContent.center = center

  val rootContent = new StackPane()
  rootContent.getChildren.addAll(mainContent)
  root = rootContent
  content = Seq(mainContent)
}

class BaseStage extends PrimaryStage {
  title = "Scala-chikoro"
  resizable = true
  width = 800
  height = 640
}

trait StartupStage extends BaseStage

class StartupStageImpl extends StartupStage {
  private val mainScene = new StartupScene()
  scene = mainScene
  onCloseRequest = _ => {
    System.exit(0)
  }
}

object StartupStage {
  def apply(): StartupStage = new StartupStageImpl()
}

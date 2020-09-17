package it.scalachikoro.client.views.stages.scenes

import it.scalachikoro.client.views.utils.KoroAlert
import scalafx.beans.property.DoubleProperty
import scalafx.geometry.{Insets, Pos}
import scalafx.scene.Scene
import scalafx.scene.control.{Button, ButtonType, Label, TextField}
import scalafx.scene.layout.{BorderPane, StackPane, VBox}

trait GameScene extends Scene

object GameScene {

  private class GameSceneImpl() extends GameScene {
    // TODO: Add a background.
    val usernameLabel: Label = Label("Username")
    val usernameField: TextField = new TextField()
    val btnDrop: Button = new Button("Hi")
    btnDrop.onAction = _ => drop()

    val center: VBox = new VBox()
    center.alignment = Pos.Center
    center.spacing = 10
    center.setMaxWidth(400)
    center.getChildren.addAll(usernameLabel, usernameField, btnDrop)

    val mainContent: BorderPane = new BorderPane()
    mainContent.prefWidth <== DoubleProperty(800)
    mainContent.maxHeight <== DoubleProperty(640)
    mainContent.setPadding(Insets(5))
    mainContent.center = center

    val rootContent = new StackPane()
    rootContent.getChildren.addAll(mainContent)
    root = rootContent
    content = Seq(mainContent)

    private def drop(): Unit = {
      val response = KoroAlert.confirmation("Drop game", "Are you sure to drop the game?") showAndWait()
      if (response.contains(ButtonType.OK)) {
        KoroAlert.info("Nooo", "Too bad. You can't drop.") showAndWait()
      } else {
        KoroAlert.info("Well", "Well... Very well!") showAndWait()
      }
    }
  }

  def apply(): GameScene = new GameSceneImpl()
}
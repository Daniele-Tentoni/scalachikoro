package it.scalachikoro.client.views.stages.scenes

import it.scalachikoro.client.views.stages.scenes.components.BottomBarBox
import scalafx.beans.property.DoubleProperty
import scalafx.geometry.Insets
import scalafx.scene.Scene
import scalafx.scene.layout.{BorderPane, StackPane}

class BaseScene extends Scene {
  protected val bottomBar: BottomBarBox = new BottomBarBox()

  protected val mainContent: BorderPane = new BorderPane()
  mainContent.prefWidth <== DoubleProperty(800)
  mainContent.maxHeight <== DoubleProperty(640)
  mainContent.setPadding(Insets(5))

  // Don't set the mainContent center here, but in the child.
  // mainContent.center = center
  mainContent.bottom = bottomBar

  protected val rootContent = new StackPane()
  rootContent.getChildren.addAll(mainContent)
  root = rootContent
  content = Seq(mainContent)
}

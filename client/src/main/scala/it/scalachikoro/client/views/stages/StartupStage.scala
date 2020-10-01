package it.scalachikoro.client.views.stages

import it.scalachikoro.client.controllers.listeners.MainViewActorListener
import it.scalachikoro.client.views.stages.scenes.{BaseScene, QueueScene, StartupScene}

trait StartupStage extends BaseStage {
  def goToQueueScene(name: String)

  /**
   * Event for queued Player.
   *
   * @param n Number of other queued players.
   */
  def queued(n: Int)

  /** Event for enqueued Player. */
  def enqueued()

  def goToMainScene()
}

object StartupStage {
  private case class StartupStageImpl(listener: MainViewActorListener) extends StartupStage {
    private[this] val mainScene: BaseScene = StartupScene(listener)
    private[this] var currentScene: BaseScene = mainScene // TODO: Change to a Scene Stack?

    scene = mainScene
    onCloseRequest = _ => System.exit(0)

    override def goToQueueScene(name: String): Unit = currentScene match {
      case QueueScene(_, _) => println("Wrong scenes on.")
      case _ =>
        val queueScene = QueueScene(name, listener) // TODO: Why server name arrive?
        scene = queueScene
        currentScene = queueScene
    }

    override def queued(n: Int): Unit = currentScene match {
      case a: QueueScene => a queued n
      case _ => println("Wrong scenes on.")
    }

    override def enqueued(): Unit = currentScene match {
      case scene: QueueScene => scene enqueued()
      case _ => println("Wrong scenes on.")
    }

    override def goToMainScene(): Unit = currentScene match {
      case StartupScene(_) => println("Wrong scenes on.")
      case _ =>
        scene = mainScene
        currentScene = mainScene
    }
  }

  def apply(listener: MainViewActorListener): StartupStage = StartupStageImpl(listener)
}
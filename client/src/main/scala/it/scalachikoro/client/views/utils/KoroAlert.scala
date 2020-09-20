package it.scalachikoro.client.views.utils

import scalafx.scene.control.Alert
import scalafx.scene.control.Alert.AlertType
import scalafx.stage.Stage

/**
 * Utils for manage alerts.
 */
object KoroAlert {
  def apply(titleText: String, message: String, alertType: AlertType, parent: Stage): Alert = new Alert(alertType) {
    title = titleText
    contentText = message
    initOwner(parent)
  }

  def apply(titleText: String, message: String, alertType: AlertType): Alert = new Alert(alertType) {
    title = titleText
    contentText = message
  }

  /**
   * Provide an alert with the Info Type.
   * @param title Alert Title.
   * @param message Alert Message. Must be explicative.
   * @param parent Parent Stage. Spawn at the center of.
   * @return Alert to show.
   */
  def info(title: String, message: String, parent: Stage): Alert = KoroAlert(title, message, AlertType.Information, parent)

  /**
   * Provide an alert with the Info Type.
   * @param title Alert Title.
   * @param message Alert Message. Must be explicative.
   * @return Alert to show.
   */
  def info(title: String, message: String): Alert = KoroAlert(title, message, AlertType.Information)

  /**
   * Provide an alert with the Confirmation Type.
   * @param title Alert Title.
   * @param message Alert Message. Must be explicative.
   * @param parent Parent Stage. Spawn at the center of.
   * @return Alert to show.
   */
  def confirmation(title: String, message: String, parent: Stage): Alert = KoroAlert(title, message, AlertType.Confirmation, parent)

  /**
   * Provide an alert with the Confirmation Type.
   * @param title Alert Title.
   * @param message Alert Message. Must be explicative.
   * @return Alert to show.
   */
  def confirmation(title: String, message: String): Alert = KoroAlert(title, message, AlertType.Confirmation)

  def error(title: String, message: String, parent: Stage): Alert = KoroAlert(title, message, AlertType.Error, parent)

  def error(title: String, message: String): Alert = KoroAlert(title, message, AlertType.Error)


}

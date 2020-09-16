package it.scalachikoro.client.views

import scalafx.scene.control.Alert
import scalafx.scene.control.Alert.AlertType

object KoroAlert {
  def apply(title: String, message: String, alertType: AlertType): Alert = {
    val alert = new Alert(alertType)
    alert.setTitle(title)
    alert.setHeaderText(null)
    alert.setContentText(message)
    alert
  }

  def info(title: String, message: String): Alert = KoroAlert(title, message, AlertType.Information)

  def confirmation(title: String, message: String): Alert = KoroAlert(title, message, AlertType.Confirmation)
}

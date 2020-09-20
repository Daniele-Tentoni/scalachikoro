package it.scalachikoro.client.views.stages.scenes.components

import java.net.InetAddress

import scalafx.scene.control.TextFormatter
import scalafx.util.StringConverter

trait IpTextFormatter

object IpTextFormatter {

  private def makePartialIPRegex = {
    val partialBlock = "(([01]?[0-9]{0,2})|(2[0-4][0-9])|(25[0-5]))"
    val subsequentPartialBlock = "(\\." + partialBlock + ")"
    val ipAddress = partialBlock + "?" + subsequentPartialBlock + "{0,3}"
    "^" + ipAddress
  }

  private val regex: String = makePartialIPRegex

  private val converter: StringConverter[InetAddress] = new StringConverter[InetAddress] {
    override def fromString(s: String): InetAddress = if (s.matches(regex))
      InetAddress.getByName(s)
    else
      InetAddress.getByName("0.0.0.0")

    override def toString(v: InetAddress): String = v.getHostAddress
  }

  private val ipAddressFilter: scalafx.scene.control.TextFormatter.Change => scalafx.scene.control.TextFormatter.Change = c => {
    val text = c.getControlNewText
    if (text.matches(regex)) c
    else null
  }

  /*
   * Tutorial from: https://codingonthestaircase.wordpress.com/2015/03/07/scalafx-8u40-textformatter-part-2/
   */
  def apply(): TextFormatter[InetAddress] = new TextFormatter[InetAddress](converter, InetAddress.getByName("0.0.0.0"), ipAddressFilter)
}
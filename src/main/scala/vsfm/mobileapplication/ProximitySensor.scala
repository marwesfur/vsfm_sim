package vsfm.mobileapplication

import util.ui.ConsoleUi

class ProximitySensor(name: String, onEnter: String => Unit, onExit: () => Unit) {
  val consoleUi = new ConsoleUi(name + " (Proximity sensor)", handleCommand)
  val gotoDesk = """go to desk at (.+)""".r
  val leaveDesk = """leave desk""".r


  def handleCommand(command: String): Unit = {
    command match {
      case gotoDesk(room) => onEnter(room)
      case leaveDesk() => onExit()
      case _ => consoleUi.appendStatus("beep");
    }
  }
}
